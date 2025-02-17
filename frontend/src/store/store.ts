import { defineStore } from "pinia";
import { Ref, watch} from "vue";
import { ref } from "vue";

import type {
  IConversation,
  IContactGroup,
  IMessage, IContact,
} from "@src/types";


import myAxios from "@src/plugins/myAxios";
import {chatServices} from "@src/services/chatServices";

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


  const setLoginUser = () => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      user.value = JSON.parse(storedUser);
    }
  }

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

  const onMessageReceived = (message: IMessage) => {
    const currConversation = conversations.value.find(c => c.id === activeConversationId.value);
    if (currConversation) {
      console.log(message);
      currConversation.messages.push(message);
    }
  };

  const connectChatRoom = () => {
    console.log("connectChatRoom");
    chatServices.connect(onMessageReceived, store.user?.username, store.user?.avatar, "alice_bob");
  };

  // function to send a new message
  const sendMessage = (conversationId: number, message: IMessage) => {
    const conversation = conversations.value.find(c => c.id === conversationId);
    if (conversation) {
      console.log(message);
      conversation.messages.push(message);

      chatServices.sendMessage(message?.content, message.sender.username, message.sender.avatar, "alice_bob");

    }


  };

  const fetchConversations = async () => {
    console.log("fetching conversations for login user ...");

    const res = await myAxios.get('/convo/fetch_conversations', {
      params: { username: store.user?.username }
    });

    if(res && res.data) {
      const conversationData = res.data.data as IConversation[];
      console.log("conversationData ", conversationData);
      //@ts-ignore
      for(let i = 0; i < conversationData.length; i++) {
        //@ts-ignore
        let room = conversationData[i];
        let tempList = room.roomName?.split("_") || [];
        let room_display_name = tempList[0] === store.user?.username ? tempList[1] : tempList[0];
        console.log("room_display_name: " + room_display_name);

        const user_avatar = "src/assets/images/cover5.png";
        let room_avatar = room.avatar === store.user?.avatar ? user_avatar : room.avatar;
        let conversation = {
          id: room.id,
          type: room.type,
          name: room.roomName,
          displayName: room_display_name,
          avatar: room_avatar,
          contacts: [],
          messages: [],
        };

        conversations.value.push(conversation);
        // fetch messages for the room
        const messageRes = await myAxios.get('/convo/fetch_message_history', {
          params: { conversationId: room.id }
        });

        if (messageRes && messageRes.data) {
          console.log("messageRes: ", messageRes);
          const messageData = messageRes.data.data as IMessage[];
          //@ts-ignore
          for(let i = 0; i < messageData.length; i++) {
            //@ts-ignore
            conversation.messages.push({
              room_name: room.roomName,
              //@ts-ignore
              content: messageData[i].content,
              sender: messageData[i].sender,
            });
          }
        }
      }
    } else {
      console.log("fetch conversations fail")
    }
    //@ts-ignore
    store.status = "success";
  };


  // ui ref
  const conversationOpen: Ref<string | undefined> = ref(
      storage.conversationOpen
  );

  return {
    conversations,
    activeConversationId,
    setActiveConversation,
    fetchConversations,
    conversationOpen,
    sendMessage,
  };
});