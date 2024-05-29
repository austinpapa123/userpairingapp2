<script setup lang="ts">
import type { IThread } from "@src/types";
import type { Ref } from "vue";
import { ref, watch } from "vue";
import {useStore, useThreadStore} from "@src/store/store";
import { PencilSquareIcon } from "@heroicons/vue/24/outline";
import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";
import ThreadsList from "@src/components/views/HomeView/Sidebar/KnowledgeRepos/ThreadsList.vue";
import SidebarHeader from "@src/components/views/HomeView/Sidebar/SidebarHeader.vue";
import NoThread from "@src/components/states/empty-states/NoThread.vue";
import AddThreadModal from "@src/components/shared/modals/AddThreadModal.vue";

const store = useStore();
const threadStore = useThreadStore();

const keyword: Ref<string> = ref("");

const composeOpen = ref(false);

// determines whether the archive is open or not
const openArchive = ref(false);

// the filtered list of threads.
const filteredThreads: Ref<IThread[]> = ref(threadStore.threads);

// filter the list of thread based on search text.
watch([keyword, openArchive], () => {
    // search archived
    filteredThreads.value =
        threadStore.threads?.filter((thread) =>
        thread?.title
          ?.toLowerCase()
          .includes(keyword.value.toLowerCase())
      ) || [];
  }
);

// (event) switch between the rendered threads.
const handleThreadChange = (threadId: number) => {
  console.log("###", threadId, typeof threadId);
  //@ts-ignore
  threadStore.activeThreadId = threadId;
  threadStore.threadOpen = "open";
};

// (event) close the compose modal.
const closeComposeModal = () => {
  composeOpen.value = false;
};

</script>

<template>
  <div>
    <SidebarHeader>
      <!--title-->
      <template v-slot:title>Threads</template>

      <!--side actions-->
      <template v-slot:actions>
        <IconButton
          @click="composeOpen = true"
          aria-label="compose thread"
          title="compose thread"
          class="w-7 h-7"
        >
          <PencilSquareIcon
            class="w-[20px] h-[20px] text-indigo-300 hover:text-indigo-400"
          />
        </IconButton>
      </template>
    </SidebarHeader>

    <!--search bar-->
    <div class="px-5 xs:pb-6 md:pb-5">
      <SearchInput v-model="keyword" />
    </div>

    <!--threads-->
    <div
      role="list"
      aria-label="threads"
      class="w-full h-full scroll-smooth scrollbar-hidden"
      style="overflow-x: visible; overflow-y: scroll"
    >
      <Loading1
          v-if="store.status === 'loading' || store.delayLoading"
          v-for="item in 6"
      />
      <div v-else>
        <div
          v-if="
            store.status === 'success' &&
            !store.delayLoading &&
            filteredThreads.length > 0
          "
        >
          <FadeTransition>
            <component
              :is="ThreadsList"
              :filtered-threads="filteredThreads"
              :active-id="(threadStore.activeThreadId as number)"
              :handle-thread-change="handleThreadChange"
              :key="openArchive ? 'archive' : 'active'"
            />
          </FadeTransition>
        </div>
        <div v-else>
          <NoThread v-if="threadStore.threads.length === 0" />
        </div>
      </div>
    </div>

    <!--compose modal-->
    <AddThreadModal
        :open-modal="composeOpen"
        :close-modal="closeComposeModal"
    />
  </div>
</template>
