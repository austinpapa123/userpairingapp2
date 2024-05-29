<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import {useStore, useMessageStore} from "@src/store/store";
import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";

const store = useStore();
const messageStore = useMessageStore();

// update localStorage with state changes
store.$subscribe((mutate, state) => {
  localStorage.setItem("app", JSON.stringify(state));
});

// update localStorage with state changes
messageStore.$subscribe((mutate, state) => {
  localStorage.setItem("message", JSON.stringify(messageStore));
});

// here we load the data from the server.
onMounted(async () => {
  store.status = "loading";
  // fake server call
  setTimeout(() => {
    store.delayLoading = false;
  });
});

// the app height
const height = ref(`${window.innerHeight}px`);

// change the app height to the window hight.
const resizeWindow = () => {
  height.value = `${window.innerHeight}px`;
};

// and add the resize event when the component mounts.
onMounted(() => {
  window.addEventListener("resize", resizeWindow);
});

// remove the event when un-mounting the component.
onUnmounted(() => {
  window.removeEventListener("resize", resizeWindow);
});
</script>

<template>
  <div :class="{ dark: true }">
    <div
      class="bg-white dark:bg-gray-800 transition-colors duration-500"
      :style="{ height: height }"
    >
      <router-view v-slot="{ Component }">
        <FadeTransition>
          <component :is="Component" />
        </FadeTransition>
      </router-view>
    </div>
  </div>
</template>
