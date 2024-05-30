<script setup lang="ts">
import {useThreadStore} from "@src/store/store";
const threadStore = useThreadStore();
</script>

<template>
  <div class="w-full min-h-[84px] px-5 py-6">
    TitleID {{threadStore.activeThreadId}}
  </div>
</template>

<style scoped>

</style>