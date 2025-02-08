<script setup lang="ts">
import Typography from "@src/components/ui/data-display/Typography.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import { RouterLink } from "vue-router";
import {ref} from "vue";
import myAxios from "@src/plugins/myAxios";
import {useStore} from "@src/store/store";

const store = useStore();

// references for form data
const username = ref('');
const password = ref('');
const updateUsername = (newUsername: string) => {
  username.value = newUsername;
};
const updatePassword = (newPassword: string) => {
  password.value = newPassword;
};

const onSubmit = async () => {
  console.log("submit login request");
  try {
    // Make sure you receive the full Axios response (including headers)
    const res = await myAxios.post(
        'auth/login',
        {
          mockUserAccount: username.value,
          mockUserPassword: password.value,
        }
    );
    console.log("sign in", res);

    // Extract the Authorization header (it may be lowercase or uppercase depending on configuration)
    console.log("res.headers: " + res.headers);
    const authHeader = res.headers['authorization'] || res.headers['Authorization'];
    console.log('authHeader: ' + authHeader);
    if (authHeader && authHeader.startsWith("Bearer ")) {
      // Remove the "Bearer " prefix to get the token string.
      const token = authHeader.substring(7);
      try {
        // Since our token is a Base64-encoded JSON string, decode it.
        const decodedPayload = JSON.parse(atob(token));
        console.log("Decoded token payload:", decodedPayload);
        // Save the user information (or token) to your store.
        store.user = decodedPayload;
        // Optionally, you might want to store the token itself for future authenticated requests.
        // localStorage.setItem("authToken", token);
        // window.location.href = '/';
      } catch (e) {
        console.error("Failed to decode token:", e);
      }
    } else {
      console.error("Login failed: No token returned in Authorization header");
    }
  } catch (error) {
    console.error("Login error:", error);
  }
};


</script>

<template>
  <div
    class="p-5 md:basis-1/2 xs:basis-full flex flex-col justify-center items-center"
  >
    <div class="w-full md:px-[26%] xs:px-[10%]">
      <!--header-->
      <div class="mb-6 flex flex-col">
        <img
          src="@src/assets/vectors/logo_thumbsup.svg"
          class="w-[22px] h-[18px] mb-4 opacity-70"
          alt="thumbs up"
        />
        <Typography variant="heading-2" class="mb-4">Welcome back</Typography>
        <Typography variant="body-3" class="text-opacity-75 font-light">
          Create an account and start messaging now!
        </Typography>
      </div>

      <!--form-->
      <div class="mb-6">
        <TextInput label="Username" placeholder="Enter your username" @input="updateUsername($event.target.value)" class="mb-5" />
        <TextInput
          label="Password"
          placeholder="Enter your password"
          @input="updatePassword($event.target.value)"
          class="pr-[40px]"
          type="password"
        >
        </TextInput>
      </div>

      <!--ours-->
      <div class="mb-6">
        <Button class="w-full mb-4" :link="false" @click="onSubmit">Sign in</Button>
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

      <!--others-->
      <div>
        <Button variant="outlined" class="w-full mb-5">
          <span class="flex">
            <img
              src="@src/assets/vectors/google-logo.svg"
              class="mr-3"
              alt="google logo"
            />
            Sign in with google [Not Implemented]
          </span>
        </Button>

        <!--bottom text-->
        <div class="flex justify-center">
          <Typography variant="body-2"
            >Don’t have an account ?
            <RouterLink
              to="/access/sign-up/"
              class="text-indigo-400 opacity-100"
            >
              Sign up
            </RouterLink>
          </Typography>
        </div>
      </div>
    </div>
  </div>
</template>
