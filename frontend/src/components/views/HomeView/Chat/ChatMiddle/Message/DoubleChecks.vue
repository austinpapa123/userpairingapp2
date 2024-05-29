<script setup lang="ts">
import GreenChecks from "@src/assets/vectors/green_checks.svg";

const props = defineProps<{
  state: string;  //like sent, delievered, read ...
}>();
</script>

<template>
  <img
    :src="GreenChecks"
    class="w-[14px] h-[14px]"
    :alt="GreenChecks"
  />
</template>
