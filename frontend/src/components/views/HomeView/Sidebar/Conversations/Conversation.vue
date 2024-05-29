<script setup lang="ts">
import type { IConversation } from "@src/types";
import type { Ref } from "vue";
import { computed, ref } from "vue";

import {
  getAvatar,
  getName,
  shorten,
} from "@src/utils";
import {useStore} from "@src/store/store";

import {
  ArchiveBoxArrowDownIcon,
  InformationCircleIcon,
  TrashIcon,
} from "@heroicons/vue/24/outline";
import Typography from "@src/components/ui/data-display/Typography.vue";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";
import { Vue3StatusIndicator } from 'vue3-status-indicator'
import 'vue3-status-indicator/dist/style.css'

const props = defineProps<{
  conversation: IConversation;
  isActive?: boolean;
  handleConversationChange?: (conversationId: number) => void;
}>();

const store = useStore();

const showContextMenu = ref(false);

const contextMenuCoordinations: Ref<{ x: number; y: number } | undefined> =
  ref();

// open context menu.
const handleShowContextMenu = (event: any) => {
  showContextMenu.value = true;
  contextMenuCoordinations.value = {
    x:
      window.innerWidth - 205 <= event.pageX
        ? window.innerWidth - 220
        : event.pageX,
    y:
      window.innerHeight - 125 <= event.pageY
        ? window.innerHeight - 200
        : event.pageY,
  };
};

// (event) closes the context menu
const handleCloseContextMenu = () => {
  showContextMenu.value = false;
};

// (event) close context menu when opening a new one.
const contextConfig = {
  handler: handleCloseContextMenu,
  events: ["contextmenu"],
};

// (event) select this conversation.
const handleSelectConversation = () => {
  showContextMenu.value = false;

  if (props.handleConversationChange)
    props.handleConversationChange(props.conversation.id);
};

// last message in conversation to display
const lastMessage = computed(
    () => props.conversation.messages[props.conversation.messages.length - 1]
);

</script>

<template>
  <div class="select-none">
    <button
      :aria-label="'conversation with' + getName(props.conversation)"
      tabindex="0"
      v-click-outside="contextConfig"
      @contextmenu.prevent="handleShowContextMenu"
      @click="
        ($event) => {
          handleSelectConversation();
        }
      "
      class="w-full h-[92px] px-5 py-6 mb-3 flex rounded focus:bg-indigo-50 dark:active:bg-gray-600 dark:focus:bg-gray-600 dark:hover:bg-gray-600 hover:bg-indigo-50 active:bg-indigo-100 focus:outline-none transition duration-500 ease-out"
      :class="{
        'md:bg-indigo-50': props.isActive,
        'md:dark:bg-gray-600': props.isActive,
      }"
    >
      <!--profile image-->
      <div class="mr-4">
        <div
          :style="{ backgroundImage: `url(${getAvatar(props.conversation)})` }"
          class="w-7 h-7 rounded-full bg-cover bg-center"
        ></div>
      </div>

      <div class="w-full flex flex-col">
        <div class="w-full">
          <!--conversation name-->
          <div class="flex items-start">
            <div class="grow mb-4 text-start">
              <Typography variant="heading-2">
                {{ getName(props.conversation) }}
              </Typography>
            </div>

            <!--last message date not implemented-->
            <Typography variant="body-1">
              ?:?? pm
            </Typography>
          </div>
        </div>
        <div class="flex justify-between">
          <div>
            <!--last message content -->
            <Typography
                variant="body-2"
                class="flex justify-start items-center text-indigo-400"
            >
                <span class="text-indigo-400">
                  {{ shorten(lastMessage) }}
                </span>
            </Typography>
          </div>
<!--          <Typography variant="body-1" no-color>💛</Typography>-->
          <Vue3StatusIndicator type="primary" class="w-3 h-3 mt-2"/>
        </div>
      </div>
    </button>

    <!--custom context menu-->
    <Dropdown
      :close-dropdown="() => (showContextMenu = false)"
      :show="showContextMenu"
      :handle-close="handleCloseContextMenu"
      :handle-click-outside="handleCloseContextMenu"
      :coordinates="{
        left: contextMenuCoordinations?.x + 'px',
        top: contextMenuCoordinations?.y + 'px',
      }"
      :position="['top-0']"
    >
      <DropdownLink :handle-click="handleCloseContextMenu">
        <InformationCircleIcon class="h-5 w-5 mr-3" />
        Conversation info
      </DropdownLink>

      <DropdownLink :handle-click="handleCloseContextMenu">
        <ArchiveBoxArrowDownIcon class="h-5 w-5 mr-3" />
        Archive conversation
      </DropdownLink>

      <DropdownLink :handle-click="handleCloseContextMenu" color="danger">
        <TrashIcon class="h-5 w-5 mr-3" />
        Delete conversation
      </DropdownLink>
    </Dropdown>
  </div>
</template>
