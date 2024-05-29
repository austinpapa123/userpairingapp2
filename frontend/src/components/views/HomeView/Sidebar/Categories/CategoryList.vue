<script setup lang="ts">
import type { IThread } from "@src/types";
import Thread from "./Category.vue";

const props = defineProps<{
  filteredThreads?: IThread[];
  handleThreadChange: (threadId: number) => void;
  activeId?: number;
}>();
</script>

<template>
  <div>
    <Thread
      v-for="thread in filteredThreads"
      :thread="thread"
      :key="thread.id"
      role="listitem"
      :handle-thread-change="props.handleThreadChange"
      :is-active="activeId === thread.id"
    />
  </div>
</template>
