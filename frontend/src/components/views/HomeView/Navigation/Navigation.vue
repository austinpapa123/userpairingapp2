<script setup lang="ts">
import { ref } from "vue";
import {useStore} from "@src/store/store";
import {
  ChatBubbleOvalLeftIcon,
  Cog6ToothIcon,
  UserIcon, FireIcon, TagIcon,
} from "@heroicons/vue/24/solid";
import AccountDropdown from "@src/components/views/HomeView/Navigation/AccountDropdown.vue";
import Logo from "@src/components/views/HomeView/Navigation/Logo.vue";
import NavLink from "@src/components/views/HomeView/Navigation/NavLink.vue";

const store = useStore();

const showDropdown = ref(false);

// (event) change the active sidebar component when clicking on a NavLink
const handleActiveSidebarComponentChange = (value: string) => {
  store.activeSidebarComponent = value;
};
</script>

<template>
  <div
    class="xs:w-full md:w-11 md:h-full md:py-7 xs:py-5 px-5 flex xs:flex-row md:flex-col items-center transition-all duration-500"
  >
    <!--logo-->
    <Logo />

    <!--main navigation-->
    <div class="grow">
      <nav aria-label="Main navigation">
        <ul class="xs:flex md:block xs:justify-between xs:items-center">
          <!--message icon-->
          <li>
            <NavLink
              :icon="ChatBubbleOvalLeftIcon"
              title="Conversations"
              @click="() => handleActiveSidebarComponentChange('messages')"
              :active="store.activeSidebarComponent === 'messages'"
            />
          </li>

          <!--recommend icon-->
          <li>
            <NavLink
                :icon="FireIcon"
                title="Recommend"
                @click="() => handleActiveSidebarComponentChange('recommend')"
                :active="store.activeSidebarComponent === 'recommend'"
            />
          </li>

          <!--categories icon-->
          <li>
            <NavLink
                :icon="TagIcon"
                title="Categories"
                @click="() => handleActiveSidebarComponentChange('categories')"
                :active="store.activeSidebarComponent === 'categories'"
            />
          </li>


          <!--contacts list icon-->
          <li>
            <NavLink
                :icon="UserIcon"
                title="Contacts"
                @click="() => handleActiveSidebarComponentChange('contacts')"
                :active="store.activeSidebarComponent === 'contacts'"
            />
          </li>

        </ul>
      </nav>
    </div>

    <!--secondary navigation-->
    <div>
      <nav aria-label="Extra navigation" class="xs:hidden md:block">
        <ul>
          <!--settings button-->
          <li>
            <NavLink
              :icon="Cog6ToothIcon"
              title="Settings"
              @click="() => handleActiveSidebarComponentChange('settings')"
              :active="store.activeSidebarComponent === 'settings'"
            />
          </li>
        </ul>
      </nav>

      <!--separator-->
      <hr
        class="xs:hidden md:block mb-6 border-gray-100 dark:border-gray-600"
      />

      <!--user avatar-->
      <AccountDropdown
        id="profile-menu"
        class="xs:hidden md:block"
        aria-labelledby="profile-menu-button"
        :show-dropdown="showDropdown"
        :handle-show-dropdown="() => (showDropdown = true)"
        :handle-close-dropdown="() => (showDropdown = false)"
      />
    </div>
  </div>
</template>
