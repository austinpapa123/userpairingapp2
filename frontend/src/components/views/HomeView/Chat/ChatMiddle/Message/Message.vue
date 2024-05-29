<script setup lang="ts">
import type {
  IMessage,
} from "@src/types";
import type { Ref } from "vue";
import linkifyStr from "linkify-string";
import { ref } from "vue";
import { getFullName } from "@src/utils";
import Typography from "@src/components/ui/data-display/Typography.vue";
import Receipt from "@src/components/views/HomeView/Chat/ChatMiddle/Message/Receipt.vue";

const props = defineProps<{
  message: IMessage;
  followUp: boolean;
  self: boolean;
}>();

const showContextMenu = ref(false);
const contextMenuCoordinations: Ref<{ x: number; y: number }> = ref({
  x: 0,
  y: 0,
});

// open context menu.
const handleShowContextMenu = (event: any) => {
  showContextMenu.value = true;
  contextMenuCoordinations.value = {
    x:
        window.innerWidth - 220 <= event.pageX
            ? window.innerWidth - 250
            : event.pageX,
    y:
        window.innerHeight - 300 <= event.pageY
            ? window.innerHeight - 250
            : event.pageY,
  };
};

// closes the context menu
const handleCloseContextMenu = () => {
  showContextMenu.value = false;
};

// close context menu when opening a new one.
const contextConfig = {
  handler: handleCloseContextMenu,
  events: ["contextmenu"],
};

// decide whether to show or hide avatar next to the image.
const hideAvatar = () => {
  if(props.self) {
    return true;
  }
  if(props.followUp && !props.self) {
    return true;
  }
  return false;
};

</script>

<template>
  <div class="select-none">
    <div class="xs:mb-6 md:mb-5 flex" :class="{ 'justify-end': props.self }">
      <!--avatar-->
      <div class="mr-4" :class="{ 'ml-[36px]': props.followUp }">
        <div
            v-if="!hideAvatar()"
            :aria-label="getFullName(props.message.sender)"
            class="outline-none"
        >
          <!--              :style="{ backgroundImage: `url(${props.message.sender.avatar})` }"-->
          <div
              :style="{ backgroundImage: `url(${props.message.sender.avatar})` }"
              class="w-[36px] h-[36px] bg-cover bg-center rounded-full"
          ></div>
        </div>
      </div>

      <div class="flex items-end">
        <!--bubble-->
        <div
            @click="handleCloseContextMenu"
            v-click-outside="contextConfig"
            @contextmenu.prevent="handleShowContextMenu"
            class="group max-w-[500px] p-5 rounded-b transition duration-500"
            :class="{

            'rounded-tr mr-4 bg-gray-50 dark:bg-gray-600':
              !props.self,

            'rounded-tl ml-4 order-2 bg-indigo-200 dark:bg-indigo-500':
              props.self,

          }"
        >
          <!--content-->
          <Typography
              variant="body-2"
              noColor
              v-if="props.message.content"
              class="outline-none text-black opacity-60 dark:text-white dark:opacity-70"
              v-html="linkifyStr((props.message.content as string), {
                            className: props.self ? 'text-black opacity-50' : 'text-indigo-500 dark:text-indigo-300',
                            format: { url: (value) => value.length > 50 ? value.slice(0, 50) + `…` : value }
                        })"
              tabindex="0"
          >
          </Typography>

        </div>

        <!--date-->
        <div :class="props.self ? ['ml-4', 'order-1'] : ['mr-4']">
          <Typography variant="body-1" class="whitespace-pre">
            ?:?? pm
          </Typography>
        </div>

        <!--just the green double check stuff there-->
        <Receipt v-if="props.self" state="sent" />
      </div>
    </div>
  </div>
</template>