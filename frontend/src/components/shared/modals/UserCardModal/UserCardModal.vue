<script setup lang="ts">
import Modal from "@src/components/ui/utils/Modal.vue";
import Button from "@src/components/ui/inputs/Button.vue";

const props = defineProps<{
  openModal: boolean;
  closeModal: () => void;
  // name?: string;
  // descrip?: string;
  // imageUrl?: string;
}>();
</script>

<template>
  <Modal :open="props.openModal" :closeModal="props.closeModal">
    <template v-slot:content>
      <div class="w-[500px] rounded py-6">
        <div class="card" style="background-image: url('/grid_design/images/jenny.png');">
          <div class="info">
            <h2>Jenny</h2>
            <p>19 · 3 km away · active 3 days ago</p>
            <div class="flex justify-center items-center gap-6 mt-4">
              <button class="w-8 h-8 bg-center bg-cover rounded-full focus:outline-none transform transition-transform duration-300 hover:scale-105" style="background-image: url('/grid_design/images/heart.png');"></button>
              <button class="w-9 h-9 bg-center bg-cover rounded-full focus:outline-none transform transition-transform duration-300 hover:scale-105" style="background-image: url('/grid_design/images/star.png');"></button>
              <button class="w-8 h-8 bg-center bg-cover rounded-full focus:outline-none transform transition-transform duration-300 hover:scale-105" style="background-image: url('/grid_design/images/cross.png');"></button>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<style scoped>
.card {
  position: relative;
  height: 300px;
  background-size: cover;
  background-position: center;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  align-items: flex-end;
}

.info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 20px;
  box-sizing: border-box;
  text-align: left;
}

.info h2 {
  margin: 0;
  font-size: 24px;
}

.info p {
  margin: 5px 0 0;
  font-size: 14px;
}
</style>