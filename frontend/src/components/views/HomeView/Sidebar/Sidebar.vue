<script setup lang="ts">
import { computed } from "vue";

import {useStore} from "@src/store/store";

import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";
import Contacts from "@src/components/views/HomeView/Sidebar/Contacts/Contacts.vue";
import Conversations from "@src/components/views/HomeView/Sidebar/Conversations/Conversations.vue";
import Settings from "@src/components/views/HomeView/Sidebar/Settings/Settings.vue";
import Threads from "@src/components/views/HomeView/Sidebar/KnowledgeRepos/Threads.vue";

const store = useStore();

// the selected sidebar component (e.g message/notifications/settings)
const ActiveComponent = computed(() => {
  if (store.activeSidebarComponent === "messages") {
    return Conversations;
  } else if (store.activeSidebarComponent === "contacts") {
    return Contacts;
  } else if (store.activeSidebarComponent === "settings") {
    return Settings;
  } else if (store.activeSidebarComponent === "knowledge") {
    //todo knowledge threads here
    return Threads;
  }
});
</script>

<template>
  <aside
    class="xs:w-full md:w-[290px] h-full xs:px-5 md:p-0 flex flex-col overflow-visible transition-all duration-500"
  >
    <FadeTransition>
      <component :is="ActiveComponent" class="h-full flex flex-col" />
    </FadeTransition>
  </aside>
</template>
