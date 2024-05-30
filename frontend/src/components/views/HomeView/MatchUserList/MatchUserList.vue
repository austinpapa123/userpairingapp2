<script setup lang="ts">
import {useThreadStore} from "@src/store/store";
import MatchUserListHeader from "@src/components/views/HomeView/MatchUserList/MatchUserListHeader.vue";
const threadStore = useThreadStore();
</script>

<template>
  <div class="h-full flex flex-col scrollbar-hidden">
    <MatchUserListHeader/>
    <div>Content</div>
    <div>Comments: [......]</div>
  </div>
</template>

<style scoped>

</style>