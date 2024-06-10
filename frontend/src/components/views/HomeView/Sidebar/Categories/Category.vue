<script setup lang="ts">
import type { IThread } from "@src/types";
import type { Ref } from "vue";
import { ref } from "vue";
import {useStore} from "@src/store/store";
import { TagIcon } from "@heroicons/vue/24/outline";
import Typography from "@src/components/ui/data-display/Typography.vue";
import 'vue3-status-indicator/dist/style.css'

const props = defineProps<{
  thread: IThread;
  isActive?: boolean;
  handleThreadChange?: (threadId: number) => void;
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

// (event) select this thread.
const handleSelectThread = () => {
  showContextMenu.value = false;

  if (props.handleThreadChange)
    props.handleThreadChange(props.thread.id);
};

// display 💛🧡🤍💙🤎🖤❤ under category name
const heartNotation = " 💛🧡🤍💙🤎🖤❤";

</script>

<template>
  <div class="select-none">
    <button
      :aria-label="props.thread.category"
      tabindex="0"
      v-click-outside="contextConfig"
      @contextmenu.prevent="handleShowContextMenu"
      @click="
        ($event) => {
          handleSelectThread();
        }
      "
      class="w-full h-[92px] px-5 py-6 mb-3 flex rounded focus:bg-indigo-50 dark:active:bg-gray-600 dark:focus:bg-gray-600 dark:hover:bg-gray-600 hover:bg-indigo-50 active:bg-indigo-100 focus:outline-none transition duration-500 ease-out"
      :class="{
        'md:bg-indigo-50': props.isActive,
        'md:dark:bg-gray-600': props.isActive,
      }"
    >
      <!--tag icon-->
      <div :class="['mb-3']">
        <div
            class="w-7 h-7 mr-4 flex justify-center items-center rounded-full bg-gray-50 dark:bg-gray-700 transition duration-500"
        >
          <TagIcon
              class="w-5 h-5 text-gray-500 dark:text-white dark:opacity-70"
          />
        </div>
      </div>

      <div class="w-full flex flex-col">
        <div class="w-full">
          <!--thread name-->
          <div class="flex items-start">
            <div class="grow mb-4 text-start">
              <Typography variant="heading-2">
                {{ props.thread.category }}
              </Typography>
            </div>

            <Typography variant="body-1">
              ▶
            </Typography>
          </div>
        </div>
        <div class="flex justify-between">
          <div>
            <!--thread hearts notation -->
            <Typography
                variant="body-2"
                class="flex justify-start items-center text-indigo-400"
            >
                <span class="text-indigo-400">
                  {{ heartNotation }}
                </span>
            </Typography>
          </div>
<!--          <Typography variant="body-1" no-color>💛</Typography>-->
        </div>
      </div>
    </button>
  </div>
</template>
