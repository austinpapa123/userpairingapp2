<script setup lang="ts">
import {onMounted, onUnmounted, Ref} from "vue";

import { computed, provide, ref } from "vue";
import {useStore, useMessageStore} from "@src/store/store";

import ChatBottom from "@src/components/views/HomeView/Chat/ChatBottom/ChatBottom.vue";
import ChatMiddle from "@src/components/views/HomeView/Chat/ChatMiddle/ChatMiddle.vue";
import ChatTop from "@src/components/views/HomeView/Chat/ChatTop/ChatTop.vue";

const store = useStore();
const messageStore = useMessageStore();

// search the selected conversation using activeConversationId.
const activeConversation = computed(() => {
  let activeConversation = messageStore.conversations.find(
    (conversation) => conversation.id === messageStore.activeConversationId
  );
  console.log("activeConversation: ", activeConversation);
  console.log("in Chat messageStore: ", messageStore.conversations);  //could access the info
  return activeConversation;
});

// provide the active conversation to all children.
provide("activeConversation", activeConversation.value);

// determines whether select mode is enabled.
const selectMode = ref(false);

// determines whether all the messages are selected or not.
const selectAll = ref(false);

// holds the selected conversations.
const selectedMessages: Ref<number[]> = ref([]);

// (event) add message to select messages.
const handleSelectMessage = (messageId: number) => {
  selectedMessages.value.push(messageId);

  if (
    activeConversation.value &&
    selectedMessages.value.length === activeConversation.value.messages.length
  ) {
    selectAll.value = true;
  }

  if (!selectMode.value) {
    selectMode.value = true;
  }
};

// (event) remove message from select messages.
const handleDeselectMessage = (messageId: number) => {
  selectAll.value = false;
  selectedMessages.value = selectedMessages.value.filter(
    (item) => item !== messageId
  );

  if (activeConversation.value && selectedMessages.value.length === 0) {
    selectMode.value = false;
  }
};

onMounted(() => {
  console.log("Chat is mounted");
  console.log("Chat starts connecting websocket");
  messageStore.connectChatRoom();

});

onUnmounted(() => {
  console.log("Chat is unmounted");
  messageStore.disconnectChatRoom();
})

</script>

<template>
  <div v-if="activeConversation" class="h-full flex flex-col scrollbar-hidden">
    <ChatTop/>
    <ChatMiddle
      :selected-messages="selectedMessages"
      :handle-select-message="handleSelectMessage"
      :handle-deselect-message="handleDeselectMessage"
    />
    <ChatBottom />
  </div>
</template>
