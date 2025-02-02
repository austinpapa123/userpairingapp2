<script setup lang="ts">
import {useStore} from "@src/store/store";

import {
  ArrowLeftOnRectangleIcon,
  ArrowPathIcon,
} from "@heroicons/vue/24/outline";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";
import { RouterLink } from "vue-router";
import myAxios from "@src/plugins/myAxios";

const props = defineProps<{
  showDropdown: boolean;
  handleCloseDropdown: () => void;
  handleShowDropdown: () => void;
  id: string;
}>();

const store = useStore();

// (event) close dropdown menu when clicking outside
const handleCloseOnClickOutside = (event: Event) => {
  if (
    !["user-avatar", "profile-menu-button"].includes(
      (event.target as HTMLButtonElement).id
    )
  ) {
    props.handleCloseDropdown();
  }
};

const logout = async () => {
  console.log("logout");
  await myAxios.get('/logout');
  console.log("avatar: " + store.user?.avatar);
};

</script>

<template>
  <div class="relative">
    <!--toggle dropdown button-->
    <button
      :id="props.id + '-button'"
      @click="handleShowDropdown"
      class="bg-white rounded-full active:scale-110 focus:outline-none focus:scale-110 transition duration-200 ease-out"
      :style="{
        'box-shadow': '0 2px 5px rgba(193, 202, 255, 0.5),2px 0 5px rgba(193, 202, 255, 0.5),-2px 0 5px rgba(193, 202, 255, 0.5),0 -2px 5px rgba(193, 202, 255, 0.5)',
      }"
      :aria-expanded="showDropdown"
      aria-controls="profile-menu"
      aria-label="toggle profile menu"
    >
      <div
        id="user-avatar"
        :style="{ backgroundImage: `url(${store.user?.avatar})` }"
        class="w-7 h-7 rounded-full bg-cover bg-center"
      ></div>
    </button>

    <!--dropdown menu-->
    <Dropdown
      :id="props.id + '-dropdown'"
      :aria-labelledby="props.id + '-button'"
      :show="props.showDropdown"
      :position="[
        'md:bottom-0',
        'md:left-[40px]',
        'md:top-[auto]',
        'bottom-[50px]',
        'left-[-77px]',
      ]"
      :handle-click-outside="handleCloseOnClickOutside"
      :close-dropdown="props.handleCloseDropdown"
    >
      <DropdownLink
          label="Account Role"
      >
        <div class="w-full flex items-center justify-start">
          {{"💚Account Role: " + store.user?.role}}
        </div>
      </DropdownLink>

      <DropdownLink
        label="Password Change"
        :handle-click="props.handleCloseDropdown"
      >
        <RouterLink to="/reset/" class="w-full flex items-center justify-start">
          <ArrowPathIcon
            class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
          />
          Password Change<br>
          [Not Implemented]
        </RouterLink>
      </DropdownLink>

      <DropdownLink
        label="Logout"
        @click="logout"
        :handle-click="props.handleCloseDropdown"
        color="danger"
      >
        <RouterLink
          to="/access/sign-in/"
          class="w-full flex items-center justify-start"
        >
          <ArrowLeftOnRectangleIcon class="h-5 w-5 mr-3" />
          Logout
        </RouterLink>
      </DropdownLink>
    </Dropdown>
  </div>
</template>
