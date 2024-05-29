<script setup lang="ts">
import {computed, onMounted} from "vue";
import {useStore, useMessageStore, useThreadStore} from "@src/store/store";
import Chat from "@src/components/views/HomeView/Chat/Chat.vue";
import Navigation from "@src/components/views/HomeView/Navigation/Navigation.vue";
import Sidebar from "@src/components/views/HomeView/Sidebar/Sidebar.vue";
import NoChatSelected from "@src/components/states/empty-states/NoChatSelected.vue";
import Loading3 from "@src/components/states/loading-states/Loading3.vue";
import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";
import NoThreadSelected from "@src/components/states/empty-states/NoThreadSelected.vue";
import MatchUserList from "@src/components/views/HomeView/MatchUserList/MatchUserList.vue";
import NoCategorySelected from "@src/components/states/empty-states/NoCategorySelected.vue";

const store = useStore();
const messageStore = useMessageStore();
const threadStore = useThreadStore();

// the active chat component or loading component.
const activeChatComponent = computed(() => {
  if (store.status === "loading" || store.delayLoading) {
    return Loading3;
  } else if (messageStore.activeConversationId) {
    return Chat;
  } else {
    return NoChatSelected;
  }
});

onMounted(async () => {
  //load the contacts info of current login user
  console.log("contacts: ", store.contacts);
  if(store.contacts.length === 0){await store.loadContacts();}
  //set login user
  console.log("login user: ", store.user);
  await store.setLoginUser();
  //set up socket listeners
  messageStore.setupListeners();
  //fetch login user related conversations
  console.log("conversations: ", messageStore.conversations);
  if(messageStore.conversations.length === 0) {await messageStore.fetchConversations();}
  //register to global updates
  messageStore.joinGlobalUpdates();
});

</script>

<template>
  <KeepAlive>
    <div
      class="xs:relative md:static h-full flex xs:flex-col md:flex-row overflow-hidden"
    >
      <!--navigation-bar-->
      <Navigation class="xs:order-1 md:-order-none" />
      <!--sidebar-->
      <Sidebar
        class="xs:grow-1 md:grow-0 xs:overflow-y-scroll md:overflow-visible scrollbar-hidden"
      />
      <!--chat-->
      <div v-if="store.activeSidebarComponent !== 'knowledge'"
        id="mainContent"
        class="xs:absolute xs:z-10 md:static grow h-full xs:w-full md:w-fit scrollbar-hidden bg-white dark:bg-gray-800 transition-all duration-500"
        :class="
          messageStore.conversationOpen === 'open'
            ? ['xs:left-[0px]', 'xs:static']
            : ['xs:left-[1000px]']
        "
        role="region"
      >
        <FadeTransition name="fade" mode="out-in">
          <component
            :is="activeChatComponent"
            :key="messageStore.activeConversationId"
          />
        </FadeTransition>
      </div>
      <div v-else>
        <div v-if="threadStore.activeThreadId">
          <FadeTransition name="fade" mode="out-in">
            <component
                :is="MatchUserList"
            />
          </FadeTransition>
        </div>
        <div v-else>
          <FadeTransition name="fade" mode="out-in">
            <component
                :is="NoCategorySelected"
            />
          </FadeTransition>
        </div>
      </div>
    </div>
  </KeepAlive>
</template>
