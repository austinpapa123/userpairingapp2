<script setup lang="ts">
import type { Component } from "vue";

const props = defineProps<{
  icon: Component;
  title: string;
  notifications?: number;
  active?: boolean;
}>();
</script>

<template>
  <div class="xs:mb-0 md:mb-6">
    <button
      class="group relative focus:outline-none"
      :title="props.title"
      :aria-label="
        props.notifications
          ? props.title + ' ' + props.notifications + ' new notifications'
          : props.title
      "
    >
      <!--icon-->
      <component
        :is="props.icon"
        class="w-7 h-6 group-focus:text-indigo-400 hover:text-indigo-400 active:text-indigo-400 active:scale-110 dark:text-gray-500 transition ease-out duration-200"
        :class="(props.active as boolean) ? ['text-indigo-400', 'dark:text-indigo-400'] : [' text-gray-300']"
      />
    </button>
  </div>
</template>
