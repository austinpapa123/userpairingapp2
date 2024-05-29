<script setup lang="ts">
import Button from "@src/components/ui/inputs/Button.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import DropFileUpload from "@src/components/ui/inputs/DropFileUpload.vue";
import {ref, Ref, watch} from "vue";

const avatar = ref(null);
const username = ref('');
const emits = defineEmits(['update:username', 'active-section-change', 'update:avatar']);
const showError = ref(false);

const updateUsername = (newUsername: string) => {
  username.value = newUsername;
  emits('update:username', newUsername);
};

const handleNext = () => {
  if(username.value === '') {
    showError.value = true;
  } else {
    showError.value = false;
    emits('active-section-change', {
      sectionName: 'password-section',
      animationName: 'slide-left',
    });
  }
};

watch(avatar, () => {
  //@ts-ignore
  console.log(avatar.value?.name);
  //@ts-ignore
  let filename = avatar.value?.name;
  emits('update:avatar', filename);
});

</script>

<template>
  <div>
    <!--form-->
    <div class="mb-5">
      <TextInput
        label="Username"
        placeholder="Enter your username"
        @input="updateUsername($event.target.value)"
        class="mb-5"
      />
      <DropFileUpload
          label="Choose your profile photo"
          class="mb-7"
          accept="image/*"
          :value="avatar"
          @value-changed="(value) => (avatar = value)"
      />
    </div>

    <div v-if="showError" class="mb-4 text-red-500">
      Fields cannot be empty
    </div>

    <!--local controls-->
    <div class="mb-6">
      <Button
        class="w-full mb-4"
        @click="handleNext"
        >Next</Button
      >
    </div>

    <!--divider-->
    <div class="mb-6 flex items-center">
      <span
        class="w-full border border-dashed border-gray-100 dark:border-gray-600 rounded-[1px]"
      ></span>
      <Typography variant="body-3" class="px-4 text-opacity-75 font-light"
        >or</Typography
      >
      <span
        class="w-full border border-dashed border-gray-100 dark:border-gray-600 rounded-[1px]"
      ></span>
    </div>

    <!--oauth controls-->
    <Button variant="outlined" class="w-full mb-5">
      <span class="flex">
        <img
          src="@src/assets/vectors/google-logo.svg"
          class="mr-3"
          alt="google-logo"
        />
        Sign in with google [Not Implemented]
      </span>
    </Button>
  </div>
</template>
