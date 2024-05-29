<script setup lang="ts">
import Button from "@src/components/ui/inputs/Button.vue";
import Modal from "@src/components/ui/utils/Modal.vue";

const props = defineProps<{
  open: boolean;
  closeModal: () => void;
}>();
</script>

<template>
  <Modal :open="props.open" :close-modal="props.closeModal">
    <template v-slot:content>
      <div class="w-[400px] bg-white dark:bg-gray-800 rounded py-6">
        <h1 class="bg-blue-200">not implemented</h1>
        <br/>
        <!--Action buttons-->
        <div class="flex w-full px-5">
          <div class="grow flex justify-start">
            <Button variant="ghost"> Add </Button>
          </div>

          <Button variant="ghost" @click="props.closeModal" class="mr-4">
            Cancel
          </Button>

          <Button> Send </Button>
        </div>
      </div>
    </template>
  </Modal>
</template>
