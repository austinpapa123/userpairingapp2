<script setup lang="ts">
import { ref } from "vue";
import {useStore} from "@src/store/store";
import { ForwardIcon } from "@heroicons/vue/24/outline";

let SkipLinkFocused = ref(false);

const store = useStore();
</script>

<template>
  <div class="mb-7 h-7 xs:hidden md:block">
    <a
      href="#mainContent"
      title="Skip to main content"
      @blur="SkipLinkFocused = false"
      @focus="SkipLinkFocused = true"
      class="outline-none"
      :class="{
        absolute: !SkipLinkFocused,
        'right-[10000px]': !SkipLinkFocused,
      }"
    >
      <ForwardIcon class="w-7 h-6 text-indigo-300" />
    </a>

    <button
      aria-label="thumbsup logo"
      :class="{ hidden: SkipLinkFocused }"
      class="outline-none"
    >
      <img
        src="@src/assets/vectors/logo_thumbsup.svg"
        class="w-8 h-7"
        alt=""
      />
    </button>
  </div>
</template>
