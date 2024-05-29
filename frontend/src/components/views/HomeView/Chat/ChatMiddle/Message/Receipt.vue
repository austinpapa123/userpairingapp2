<script setup lang="ts">
import DoubleChecks from "@src/components/views/HomeView/Chat/ChatMiddle/Message/DoubleChecks.vue";

const props = defineProps<{
  state: string;
}>();
</script>

<template>
  <DoubleChecks :state="props.state" />
</template>
