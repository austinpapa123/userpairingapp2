<script setup lang="ts">
import ThreadPreviewHeader from "@src/components/views/HomeView/ThreadPreview/ThreadPreviewHeader.vue";
import {useThreadStore} from "@src/store/store";
const threadStore = useThreadStore();
</script>

<template>
  <ThreadPreviewHeader/>
  <div>Content</div>
  <div>Comments: [......]</div>
</template>

<style scoped>

</style>