<script setup lang="ts">
import { EyeSlashIcon } from "@heroicons/vue/24/outline";

import IconButton from "@src/components/ui/inputs/IconButton.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import myAxios from "@src/plugins/myAxios";


const sendTestRequest = async () => {
  try {
    const res = await myAxios.get("/testcontroller/test2", {
      params: { payload: "test data" } // Ensure payload is sent correctly
    });
    if (res) {
      console.log(res);
    }
  } catch (e) {
    console.log(e);
  }
};

const sendTestPostRequest = async () => {
  try {
    const res = await myAxios.post("/testcontroller/test", {
      payload: "Hello from Vue!"
    });

    if (res) {
      console.log("Response from backend:", res);
    }
  } catch (e) {
    console.error("Error making POST request:", e);
  }
};


</script>

<template>
  <div class="w-full p-5 flex flex-col justify-center items-center">
    <div class="mb-6 w-[350px]">
      <!--header-->
      <div class="mb-6 flex flex-col">
        <img
          src="@src/assets/vectors/logo_thumbsup.svg"
          class="w-[22px] h-[18px] mb-4 opacity-70"
          alt="thumbs up"
        />
        <Typography variant="heading-2" class="mb-4"
          >Reset Your Password [Not Implemented]</Typography
        >
        <Typography variant="body-3" class="text-opacity-75 font-light">
          Enter your old password and your new password below to reset your
          password
        </Typography>
      </div>

      <!--form-->
      <div class="mb-6">
        <TextInput
          variant="bordered"
          label="Old Password"
          placeholder="Enter your password"
          class="mb-5"
        >
          <template v-slot:endAdornment>
            <IconButton
              variant="ghost"
              class="p-4"
              title="toggle show password"
              aria-label="toggle show password"
            >
              <EyeSlashIcon
                class="w-w-5 h-5 text-black opacity-50 dark:text-white dark:opacity-60"
              />
            </IconButton>
          </template>
        </TextInput>

        <TextInput
          variant="bordered"
          label="New Password"
          placeholder="Enter your password"
          class="mb-5"
        >
          <template v-slot:endAdornment>
            <IconButton
              variant="ghost"
              class="p-4"
              title="toggle show password"
              aria-label="toggle show password"
            >
              <EyeSlashIcon
                class="w-5 h-5 text-black opacity-50 dark:text-white dark:opacity-60"
              />
            </IconButton>
          </template>
        </TextInput>

        <TextInput
          variant="bordered"
          label="Confirm New Password"
          placeholder="Enter your password"
        >
          <template v-slot:endAdornment>
            <IconButton
              variant="ghost"
              class="p-4"
              title="toggle show password"
              aria-label="toggle show password"
            >
              <EyeSlashIcon
                class="w-5 h-5 text-black opacity-50 dark:text-white dark:opacity-60"
              />
            </IconButton>
          </template>
        </TextInput>
      </div>

      <!--controls-->
      <div>
        <Button class="w-full" @click="sendTestRequest">Reset Password [Not Implemented]</Button>
        <Button class="w-full" @click="sendTestPostRequest">Reset Password [Not Implemented]</Button>
      </div>
    </div>
  </div>
</template>
