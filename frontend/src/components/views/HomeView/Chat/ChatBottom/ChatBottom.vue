<script setup lang="ts">
import {Ref} from "vue";
import {IContact, IConversation, IMessage} from "@src/types";
import {useStore, useMessageStore} from "@src/store/store";
import { ref, inject } from "vue";
import {
  FaceSmileIcon,
  PaperAirplaneIcon,
  PaperClipIcon,
  XCircleIcon,
} from "@heroicons/vue/24/outline";
import AttachmentsModal from "@src/components/shared/modals/AttachmentsModal/AttachmentsModal.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import ScaleTransition from "@src/components/ui/transitions/ScaleTransition.vue";
import EmojiPicker from "@src/components/ui/inputs/EmojiPicker/EmojiPicker.vue";
import Textarea from "@src/components/ui/inputs/Textarea.vue";

const store = useStore();
const messageStore = useMessageStore();

const activeConversation = <IConversation>inject("activeConversation");

// the content of the message.
const value: Ref<string> = ref("");

// determines whether the app is recording or not.
const recording = ref(false);

// open emoji picker.
const showPicker = ref(false);

// open modal used to send multiple attachments attachments.
const openAttachmentsModal = ref(false);

// close picker when you click outside.
const handleClickOutside = (event: Event) => {
  let target = event.target as HTMLElement;
  let parent = target.parentElement as HTMLElement;

  if (
    target &&
    !target.classList.contains("toggle-picker-button") &&
    parent &&
    !parent.classList.contains("toggle-picker-button")
  ) {
    showPicker.value = false;
  }
};

const send = () => {
  const sender: IContact = {
    id: store.user?.id || -1,
    username: store.user?.username || '',
    avatar: store.user?.avatar || '',
    status: store.user?.status || '',
    role: store.user?.role || '',
  };
  const messageInstance: IMessage =  {
    room_name: activeConversation.name || '',
    content: value.value,
    sender: sender,
  };
  messageStore.sendMessage(activeConversation.id, messageInstance);
  //empty the input box as it's bonded to Ref<> value
  value.value = "";
};

</script>

<template>
  <div class="w-full">
    <div
      class="h-auto min-h-[84px] p-5 flex items-end"
      v-if="store.status !== 'loading'"
    >
      <div class="min-h-[44px]">
        <!--select attachments button-->
        <IconButton
          title="open select attachments modal"
          aria-label="open select attachments modal"
          @click="openAttachmentsModal = true"
          v-if="!recording"
          class="group w-7 h-7 md:mr-5 xs:mr-4"
        >
          <PaperClipIcon
            class="w-[20px] h-[20px] text-gray-400 group-hover:text-indigo-300"
          />
        </IconButton>
      </div>

      <!--message textarea-->
      <div class="grow md:mr-5 xs:mr-4 self-end" v-if="!recording">
        <div class="relative">
          <Textarea
            v-model="value"
            :value="value"
            class="max-h-[80px] pr-[50px] resize-none scrollbar-hidden"
            auto-resize
            cols="30"
            rows="1"
            placeholder="Write your message here"
            aria-label="Write your message here"
          />

          <!--emojis-->
          <div class="absolute bottom-[13px] right-0">
            <!--emoji button-->
            <IconButton
              title="toggle emoji picker"
              aria-label="toggle emoji picker"
              @click="showPicker = !showPicker"
              class="toggle-picker-button group w-7 h-7 md:mr-5 xs:mr-4"
            >
              <XCircleIcon
                v-if="showPicker"
                class="w-[20px] h-[20px] text-gray-400 group-hover:text-indigo-300"
              />
              <FaceSmileIcon
                v-else
                class="w-[20px] h-[20px] text-gray-400 group-hover:text-indigo-300"
              />
            </IconButton>

            <!--emoji picker-->
            <ScaleTransition>
              <div
                v-click-outside="handleClickOutside"
                v-show="showPicker"
                class="absolute z-10 bottom-[55px] md:right-0 xs:right-[-80px] mt-2"
              >
                <div role="none">
                  <EmojiPicker :show="showPicker" />
                </div>
              </div>
            </ScaleTransition>
          </div>
        </div>
      </div>

      <div class="min-h-[44px] flex">
        <!--send message button-->
        <IconButton
          v-if="!recording"
          class="group w-7 h-7 bg-indigo-300 hover:bg-indigo-400 focus:bg-indigo-400 dark:focus:bg-indigo-300 dark:bg-indigo-400 dark:hover:bg-indigo-400 active:scale-110"
          variant="ghost"
          title="send message"
          aria-label="send message"
          @click="send"
        >
          <PaperAirplaneIcon class="w-[17px] h-[17px] text-white" />
        </IconButton>
      </div>

    <AttachmentsModal
      :open="openAttachmentsModal"
      :close-modal="() => (openAttachmentsModal = false)"
    />
    </div>
  </div>
</template>

<style>
input[placeholder="Search emoji"] {
  background: rgba(0, 0, 0, 0);
}
</style>
