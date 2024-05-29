<script setup lang="ts">
import { computed, ref } from "vue";

import GroupInfo from "@src/components/shared/modals/ComposeModal/GroupInfo.vue";
import SlideTransition from "@src/components/ui/transitions/SlideTransition.vue";

// used to determine whether to slide left or right
const animation = ref("slide-left");

// name of the active modal page
const activePageName = ref("group-info");

// the active page component
const ActivePage = computed(() => {
  if (activePageName.value === "group-info") return GroupInfo;
});

</script>

<template>
  <div>
    <!--content-->
    <div class="overflow-x-hidden">
      <SlideTransition :animation="animation">
        <component
          :is="ActivePage"
          :key="activePageName"
        />
      </SlideTransition>
    </div>
  </div>
</template>
