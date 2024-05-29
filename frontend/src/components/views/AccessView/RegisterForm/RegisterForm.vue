<script setup lang="ts">
import {computed, Ref, ref} from "vue";
import { RouterLink } from "vue-router";
import SlideTransition from "@src/components/ui/transitions/SlideTransition.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import PasswordSection from "@src/components/views/AccessView/RegisterForm/PasswordSection.vue";
import PersonalSection from "@src/components/views/AccessView/RegisterForm/PersonalSection.vue";
import myAxios from "@src/plugins/myAxios";

// state for form data
const formData : Ref<{username: string, password: string, avatar: string}> = ref({
  username: '',
  password: '',
  avatar: '',
});

// update the form data
const updateFormData = (field: string, value: string) => {
  if(field === "username") formData.value.username = value;
  if(field === "password") formData.value.password = value;
  //just the filename though
  if(field === "avatar") formData.value.avatar = value;
  console.log(field, value);
};

// submit the sign-up request
const onSubmit = async () => {
  console.log("Submitting form data:", formData.value);

  const res = await myAxios.post('/signup/user', {
    username: formData.value.username,
    password: formData.value.password,
    avatar: formData.value.avatar,
  });
  console.log("@@@ sign up", res);

  //todo check response, interceptor returns response.data fyi.
  //@ts-ignore
  if(res.code === 0) {
    //if success re-route to login page
    window.location.href = "/access/sign-in/"
  }else{
    console.log("signup failed");
  }

};

// determines what form section to use.
const activeSectionName = ref("personal-section");

// determines what direction the slide animation should use.
const animation = ref("slide-left");

// get the active section component from the section name
const ActiveSection = computed(() => {
  if (activeSectionName.value === "personal-section") {
    return PersonalSection;
  } else if (activeSectionName.value === "password-section") {
    return PasswordSection;
  }
});

// (event) to move between modal pages
const changeActiveSection = (event: {
  sectionName: string;
  animationName: string;
}) => {
  animation.value = event.animationName;
  activeSectionName.value = event.sectionName;
};
</script>

<template>
  <div
    class="p-5 md:basis-1/2 xs:basis-full flex flex-col justify-center items-center"
  >
    <div class="w-full md:px-[26%] xs:px-[10%]">
      <!--header-->
      <div class="mb-2 flex flex-col">
        <img
          src="@src/assets/vectors/logo_thumbsup.svg"
          class="w-[22px] h-[18px] mb-5 opacity-70"
          alt=""
        />
        <Typography variant="heading-2" class="mb-4"
          >Get started with ⬇⬇⬇⬇</Typography
        >
      </div>

      <!--form section-->
      <SlideTransition :animation="animation">
        <component
          @active-section-change="changeActiveSection"
          :is="ActiveSection"
          @update:username="updateFormData('username', $event)"
          @update:avatar="updateFormData('avatar', $event)"
          @update:password="updateFormData('password', $event)"
          @submit:form="onSubmit"
        />
      </SlideTransition>

      <!--bottom text-->
      <div class="flex justify-center">
        <Typography variant="body-2"
          >Already have an account ?
          <RouterLink to="/access/sign-in/" class="text-indigo-400 opacity-100">
            Sign in
          </RouterLink>
        </Typography>
      </div>
    </div>
  </div>
</template>
