<script setup lang="ts">
import type { IConversation, IMessage } from "@src/types";
import type { Ref } from "vue";
import { inject, onMounted, ref } from "vue";

import {useStore} from "@src/store/store";

import Message from "@src/components/views/HomeView/Chat/ChatMiddle/Message/Message.vue";

const props = defineProps<{
  handleSelectMessage: (messageId: number) => void;
  handleDeselectMessage: (messageId: number) => void;
  selectedMessages: number[];
}>();

const store = useStore();

const container: Ref<HTMLElement | null> = ref(null);
const activeConversation = <IConversation>inject("activeConversation");

// checks whether the previous message was sent by the same user.
const isFollowUp = (index: number, previousIndex: number): boolean => {
  if (previousIndex < 0) {
    return false;
  } else {
    let previousSender = activeConversation.messages[previousIndex].sender.id;
    let currentSender = activeConversation.messages[index].sender.id;
    return previousSender === currentSender;
  }
};

// checks whether the message is sent by the authenticated user.
const isSelf = (message: IMessage): boolean => {
  //console.log("ChatMiddle login user id: " + store.user?.id);
  //console.log("ChatMiddle sender id: " + message.sender.id);
  const loginUserId = String(store.user?.id);
  const senderId = String(message.sender.id);
  return Boolean(store.user && senderId === loginUserId);
};

// scroll messages to bottom.
onMounted(() => {
  (container.value as HTMLElement).scrollTop = (
    container.value as HTMLElement
  ).scrollHeight;
});
</script>

<template>
  <div
    ref="container"
    class="grow px-10 py-5 flex flex-col overflow-y-scroll scrollbar-hidden"
  >
    <div
      v-if="store.status !== 'loading'"
      v-for="(message, index) in activeConversation.messages"
      :key="index"
    >

      <Message
        :message="message"
        :self="isSelf(message)"
        :follow-up="isFollowUp(index, index - 1)"
      />
    </div>
  </div>
</template>
