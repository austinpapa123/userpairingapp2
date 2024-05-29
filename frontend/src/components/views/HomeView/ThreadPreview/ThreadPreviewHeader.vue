<script setup lang="ts">
import {useThreadStore} from "@src/store/store";
const threadStore = useThreadStore();
</script>

<template>
  <div>
    TitleID {{threadStore.activeThreadId}}
  </div>
</template>

<style scoped>

</style>