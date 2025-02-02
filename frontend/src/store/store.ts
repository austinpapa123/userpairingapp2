import { defineStore } from "pinia";
import { Ref, watch} from "vue";
import { ref } from "vue";

import type {
  IConversation,
  IContactGroup,
  IMessage, IContact,
} from "@src/types";

import socket, {
  socketJoinChannel, socketJoinConversation,
  socketJoinGobalUpdates,
  socketJoinPrivate,
  socketSendMessage
} from "@src/services/chatService";

import myAxios from "@src/plugins/myAxios";

export const useStore = defineStore("app", () => {
  // local storage, currently never used in project
  const storage = JSON.parse(localStorage.getItem("app") || "{}");

  // app status refs ,  "loading" or "success"
  const status = ref();

  // app data refs
  // data refs
  //@ts-ignore
  const user: Ref<IContact | undefined> = ref(null);

  const contacts = ref<IContact[]>([]);
  const contactGroups = ref<IContactGroup[]>([]);

  // for emoji picker I suspect
  const emojiSkinTone: Ref<string> = ref(storage.emojiSkinTone || "neutral");

  // ui refs
  const activeSidebarComponent: Ref<string> = ref(
    //storage.activeSidebarComponent || "messages"
      "messages"
  );
  const delayLoading = ref(true);

  // watch contacts and update contactGroups
  watch(
      contacts,
      (newContacts) => {
        console.log('Contacts updated:', newContacts); // Debugging statement
        if (newContacts.length > 0) {
          let sortedContacts = [...newContacts].sort((a, b) =>
              a.username.localeCompare(b.username)
          );
          let groups: IContactGroup[] = [];
          let currentLetter: string = '';
          for (let contact of sortedContacts) {
            let firstLetter = contact.username[0].toUpperCase();
            if (firstLetter !== currentLetter) {
              currentLetter = firstLetter;
              groups.push({
                letter: currentLetter,
                contacts: [],
              });
            }
            //add contact to the latest group
            groups[groups.length - 1].contacts.push(contact);
          }
          //update the contactGroups ref
          contactGroups.value = groups;
        } else {
          contactGroups.value = [];
        }
      },
      { immediate: true }
  );


  //loads all user contacts info
  const loadContacts = async () => {
    try {
      const res = await myAxios.get('/list_contacts');
      // @ts-ignore
      if (res.code === 0) {
        // @ts-ignore
        contacts.value = res.contacts;
        console.log(contacts.value.length);
      }
    } catch (error) {
      console.error('Failed to load contacts:', error);
    }
  };

  const testMyaxios = async () => {
    try {
      const res = await myAxios.get('/testcontroller/test');
      if(res) {
        console.log(res);
      }
    } catch (error) {
      console.log('Failed to load test:', error);
    }
  }

  const setLoginUser = async (username: string) => {
    try {
      const res = await myAxios.get(`/mockusers/current/${username}`);
      // @ts-ignore
      if (res.code === 0) {
        // @ts-ignore
        user.value = res.data;
        console.log("set login user to " + res.data.username);
      }
    } catch (error) {
      console.error('Failed to set login user:', error);
    }
  };

  const deleteContact = async (delete_contact_id: number) => {
    console.log("delete_contact_id", delete_contact_id);
    try {
      const res = await myAxios.post('/delete_contact', {
        user_id_1: user.value?.id,
        user_id_2: delete_contact_id
      });
      //@ts-ignore
      if (res.success) {
        console.log('contact deleted successfully');
        // re fetch contacts
        status.value ='loading'
        await loadContacts(); //somehow working but failed rendering
        status.value ='success'
      } else {
        console.error('failed to delete contact1');
      }
    } catch (error) {
      console.error('failed to delete contact2');
    }
  };

  return {
    // status refs
    status,

    // data refs
    user,
    contacts,
    contactGroups,
    emojiSkinTone,

    //load(init) methods
    loadContacts,
    setLoginUser,

    // ui refs
    activeSidebarComponent,
    delayLoading,

    deleteContact,
  };
});

export const useThreadStore = defineStore("thread", () => {
  //@ts-ignore
  const threads: Ref<IThread[]> = ref([
    {
      id: 1,
      category: "Asian",
    },
    {
      id: 2,
      category: "Tall",
    },
    {
      id: 3,
      category: "Skinny",
    },
    {
      id: 4,
      category: "African American",
    },
    {
      id: 5,
      category: "Luv Pets",
    },
  ]);

  const activeThreadId: Ref<number | null> = ref( null);
  // setter for current active thread, just in case
  const setActiveThread = (threadId: number | null) => {
    activeThreadId.value = threadId;
  };
  // ui ref
  const threadOpen: Ref<string | undefined> = ref("");
  return {
    threads,
    activeThreadId,
    setActiveThread,
    threadOpen,
  };
});


export const useMessageStore = defineStore("message", () => {
  // local storage, currently never used in project
  const storage = JSON.parse(localStorage.getItem("message") || "{}");
  const store = useStore();

  //@ts-ignore
  const conversations: Ref<IConversation[]> = ref([]);

  const activeConversationId: Ref<number | null> = ref( null);
  // setter for current active conversation, just in case
  const setActiveConversation = (conversationId: number | null) => {
    activeConversationId.value = conversationId;
  };

  // function to send a new message
  const sendMessage = (conversationId: number, message: IMessage) => {
    const conversation = conversations.value.find(c => c.id === conversationId);
    if (conversation) {
      console.log(message);
      conversation.messages.push(message);
      console.log("sfsfsfss", conversation.name);
      socketSendMessage({room_name: conversation.name, sender_name: message.sender.username, content: message.content});
    }
  };

  //perhaps set up socket.on(...)
  const setupListeners =  () => {
    socket.on('chat_msg_incoming', async function(data) {
        console.log("chat_msg_incoming", data);
        const parseData = JSON.parse(data);
        //@ts-ignore
        const conversation = conversations.value.find(c => c.id === parseData.room_id);
        if(parseData.sender.id !== store.user?.id) {
          //@ts-ignore
          conversation?.messages.push({room_name: conversation?.name, content: parseData.content, sender: parseData.sender});
        }

    });

    socket.on('channel_created', function(data) {
      console.log('channel_created signal received', JSON.parse(data));
      const parseData = JSON.parse(data);
      let newChannel = {
        id: parseData.room.id,
        type: 'group',
        name: parseData.room.room_name,
        displayName: parseData.room.room_name,
        avatar: parseData.room.avatar,
        admins: [],
        contacts: [],
        messages: [],
      };
      conversations.value.push(newChannel);

    });

    socket.on('private_created', async function(data) {
      console.log('private_created signal received', data);
      const parseData = JSON.parse(data);
      if(parseData.sender_name === store.user?.username || parseData.receiver_name === store.user?.username) {
        let conversation_name = parseData.sender_name === store.user?.username ? parseData.receiver_name : parseData.sender_name;
        const sender_user_avatar = await myAxios.get('/get_user_avatar', {
          params: { username: parseData.sender_name }
        });
        let conversation_avatar = parseData.sender_name === store.user?.username ? parseData.room.avatar : sender_user_avatar;
        let newPrivate = {
          id: parseData.room.id,
          type: 'couple',
          name: parseData.room?.name,
          displayName: conversation_name,
          avatar: conversation_avatar,
          contacts: [],
          messages: [],
        };
        conversations.value.push(newPrivate);
      }
    });
  }

  //generate new conversations through compose modal
  const generateNewChannelConversation = (conversation_name: string, avatar: string) => {
    console.log("start generating channel...");
    socketJoinChannel({name: conversation_name, avatar: avatar});
  };

  const generateNewCoupleConversation = (the_other_username: string, avatar: string) => {
    console.log("start generating couple...");
    socketJoinPrivate({sender_name: store.user?.username, receiver_name: the_other_username, avatar: avatar});

  };

  const joinGlobalUpdates = () => {
    socketJoinGobalUpdates();
  };

  const fetchConversations = async () => {
    console.log("fetching conversations for login user ...");
    const res = await myAxios.get('/fetch_conversations', {
      params: { username: store.user?.username }
    });
    if(res) {
      //@ts-ignore
      for(let i = 0; i < res.length; i++) {
        //@ts-ignore
        let room = res[i];
        let tempList = room.room_name.split("_");
        let room_display_name = tempList[0] === store.user?.username ? tempList[1] : tempList[0];
        const user_avatar = await myAxios.get('/get_user_avatar', {
          params: { username: room_display_name }
        });
        let room_avatar = room.avatar === store.user?.avatar ? user_avatar : room.avatar;
        let conversation = {
          id: room.id,
          type: room.type,
          name: room.room_name,
          displayName: room_display_name,
          avatar: room_avatar,
          contacts: [],
          messages: [],
        };
        conversations.value.push(conversation);
        // fetch messages for the room
        const messageRes = await myAxios.get('/fetch_message_history', {
          params: { room_id: room.id }
        });
        if (messageRes) {
          console.log("messageRes: ", messageRes);
          //@ts-ignore
          for(let i = 0; i < messageRes.length; i++) {
            //@ts-ignore
            const sender_data = await myAxios.get('/get_user_by_name', {
              //@ts-ignore
              params: { username: messageRes[i].sender_name }
            });
            console.log("sender_data", sender_data);
            //@ts-ignore
            conversation.messages.push({
              room_name: room.room_name,
              //@ts-ignore
              content: messageRes[i].message_text,
              sender: sender_data,
            });
          }
        }
      }
    }else {
      console.log("fetch conversations fail")
    }
    //@ts-ignore
    store.status = "success";
  };

  const joinConversation = () => {

    console.log("conversations: ", conversations);
    console.log("activeConversationID: ", activeConversationId.value);
    //@ts-ignore
    const conversation = conversations.value.find(c => c.id === activeConversationId.value);
    console.log("conversation: ", conversation);
    socketJoinConversation({room_name: conversation?.name});
  }

  // ui ref
  const conversationOpen: Ref<string | undefined> = ref(
      storage.conversationOpen
  );

  return {
    conversations,
    joinConversation,
    activeConversationId,
    setActiveConversation,
    fetchConversations,
    conversationOpen,
    sendMessage,
    joinGlobalUpdates,
    setupListeners,
    generateNewChannelConversation,
    generateNewCoupleConversation,
  };
});
