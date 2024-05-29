<script setup lang="ts">
import Typography from "@src/components/ui/data-display/Typography.vue";
import SakuraMOMO from "@src/assets/images/cover1.png";
</script>

<template>
  <div
    class="py-[50px] px-[50px] h-full basis-1/2 md:flex xs:hidden items-center justify-center"
  >
    <div
      class="w-full h-3/5 px-7 flex items-center bg-no-repeat bg-center bg-cover rounded-[12px]"
      :style="{ 'background-image': `url(${SakuraMOMO})` }"
    >
      <div
        class="px-9 py-[45px] rounded-[5px] bg-cover bg-no-repeat"
        :style="{ 'background-image': `url(${SakuraMOMO})` }"
      >
        <Typography variant="display" class="mb-5 text-white" no-color>
          <span class="text-gradient">
            INFO2222-Usability-Project <br>
            CC12-05
          </span>
        </Typography>
      </div>
    </div>
  </div>
</template>

<!-- Add this custom CSS to your project -->
<style scoped>
.text-gradient {
  background: -webkit-linear-gradient(left top, #782eab, #e051c8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>
