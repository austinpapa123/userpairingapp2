<script setup lang="ts">
import type { IConversation } from "@src/types";
import {useStore, useMessageStore} from "@src/store/store";
import { getAvatar, getName } from "@src/utils";
import { inject, ref } from "vue";

import {
  ChevronLeftIcon,
  EllipsisVerticalIcon,
  InformationCircleIcon,
} from "@heroicons/vue/24/outline";
import Typography from "@src/components/ui/data-display/Typography.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";

const props = defineProps<{
  handleOpenInfo: () => void;
}>();

const store = useStore();
const messageStore = useMessageStore();

const activeConversation = <IConversation>inject("activeConversation");

const showDropdown = ref(false);

// (event) close dropdown menu when click item
const handleCloseDropdown = () => {
  showDropdown.value = false;
};

// (event) close dropdown menu when clicking outside the menu.
const handleClickOutside = (event: Event) => {
  let target = event.target as HTMLElement;
  let parentElement = target.parentElement as HTMLElement;

  if (
    target &&
    !(target.classList as Element["classList"]).contains("open-top-menu") &&
    parentElement &&
    !(parentElement.classList as Element["classList"]).contains("open-top-menu")
  ) {
    handleCloseDropdown();
  }
};

// (event) close the selected conversation
const handleCloseConversation = () => {
  messageStore.conversationOpen = "close";
  messageStore.activeConversationId = null;
};

</script>

<template>
  <!--conversation info-->
  <div class="w-full flex justify-center items-center">
    <!-- left arrow close conversation button -->
    <div class="group mr-4">
      <IconButton
        class="w-7 h-7"
        @click="handleCloseConversation"
        title="close conversation"
        aria-label=""
      >
        <ChevronLeftIcon
          aria-label="close conversation"
          class="w-[20px] h-[20px] text-gray-300 group-hover:text-indigo-300"
        />
      </IconButton>
    </div>

    <div v-if="store.status !== 'loading'" class="flex grow">
      <!--avatar-->
      <button
        class="mr-5 outline-none"
        aria-label="profile avatar"
        @click="props.handleOpenInfo"
      >
        <div
          :style="{
            backgroundImage: `url(${getAvatar(activeConversation)})`,
          }"
          class="w-[36px] h-[36px] rounded-full bg-cover bg-center"
        ></div>
      </button>

      <!--name and last seen-->
      <div class="flex flex-col">
        <Typography
          variant="heading-2"
          class="mb-2 default-outline cursor-pointer"
          tabindex="0"
        >
          {{ getName(activeConversation) }}
        </Typography>

        <Typography
          variant="body-2"
          class="font-extralight default-outline rounded-[4px]"
          tabindex="0"
          aria-label="Last seen ??? ??, ????"
        >
          Last seen ??? ??, ????
        </Typography>
      </div>
    </div>

    <div class="flex" :class="{ hidden: store.status === 'loading' }">

      <div class="relative">
        <!--dropdown menu button-->
        <IconButton
          id="open-conversation-menu"
          @click="showDropdown = !showDropdown"
          tabindex="0"
          class="open-top-menu group w-7 h-7"
          :aria-expanded="showDropdown"
          aria-controls="conversation-menu"
          title="toggle conversation menu"
          aria-label="toggle conversation menu"
        >
          <EllipsisVerticalIcon
            class="open-top-menu w-[20px] h-[20px] text-gray-400 group-hover:text-indigo-300"
          />
        </IconButton>

        <!--dropdown menu-->
        <Dropdown
          id="conversation-menu"
          :close-dropdown="() => (showDropdown = false)"
          :show="showDropdown"
          :position="['right-0']"
          :handle-click-outside="handleClickOutside"
          aria-labelledby="open-conversation-menu"
        >
          <DropdownLink :handle-click="handleCloseDropdown">
            <InformationCircleIcon
              class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
            />
            Not Implemented
          </DropdownLink>
          <DropdownLink :handle-click="handleCloseDropdown">
            <InformationCircleIcon
                class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
            />
            Not Implemented
          </DropdownLink>
          <DropdownLink :handle-click="handleCloseDropdown">
            <InformationCircleIcon
                class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
            />
            Not Implemented
          </DropdownLink>
        </Dropdown>
      </div>
    </div>
  </div>
</template>
