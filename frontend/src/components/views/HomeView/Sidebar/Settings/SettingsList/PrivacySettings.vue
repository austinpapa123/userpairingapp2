<script setup lang="ts">
import {useStore} from "@src/store/store";

import AccordionButton from "@src/components/ui/data-display/AccordionButton.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import Collapse from "@src/components/ui/utils/Collapse.vue";

// Variables
const props = defineProps<{
  collapsed: boolean;
  handleToggle: () => void;
}>();

const store = useStore();
</script>

<template>
  <!--privacy settings-->
  <AccordionButton
    id="privacy-settings-toggler"
    class="w-full flex px-5 py-6 mb-3 rounded focus:outline-none"
    :collapsed="props.collapsed"
    chevron
    aria-controls="privacy-settings-collapse"
    @click="props.handleToggle()"
  >
    <Typography variant="heading-2" class="mb-4"> Privacy & Safety [Not Implemented]</Typography>
    <Typography variant="body-2"> Control your privacy settings</Typography>
  </AccordionButton>

  <Collapse id="privacy-settings-collapse" :collapsed="props.collapsed">
  </Collapse>
</template>
