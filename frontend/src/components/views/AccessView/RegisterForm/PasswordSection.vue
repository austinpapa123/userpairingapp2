<script setup lang="ts">
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import {ref} from "vue";

const emits = defineEmits(['update:password', "active-section-change", "submit:form"]);
const showError = ref(false);
const pwd = ref('');

const updatePassword = (newPassword: string) => {
  pwd.value = newPassword;
  emits('update:password', newPassword);
};

const handleSubmit = () => {
  if (pwd.value === '') {
    showError.value = true;
  } else {
    showError.value = false;
    emits('submit:form');
  }
};


</script>

<template>
  <div>
    <div class="mb-5">
      <!--form-->
      <TextInput
        label="Password"
        placeholder="Enter your password"
        class="pr-[40px] mb-5"
        @input="updatePassword($event.target.value)"
        type="password"
      >
      </TextInput>
      <div v-if="showError" class="ml-1 mb-1 text-red-500">
        Fields cannot be empty
      </div>
    </div>

    <!--controls-->
    <div class="mb-5">
      <Button class="w-full mb-4" @click="handleSubmit">
        Sign up</Button>
      <Button
        variant="outlined"
        class="w-full"
        @click="
          $emit('active-section-change', {
            sectionName: 'personal-section',
            animationName: 'slide-right',
          })
        "
      >
        Back
      </Button>
    </div>
  </div>
</template>
