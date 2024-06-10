<script setup lang="ts">
import UserCardModal from "@src/components/shared/modals/UserCardModal/UserCardModal.vue";
import {ref} from "vue";
const composeOpen = ref(false);

// (event) close the compose modal.
const closeComposeModal = () => {
  composeOpen.value = false;
};

</script>

<template>
  <div class="h-full flex flex-col scrollbar-hidden overflow-auto">
    <div class="container">
      <div class="grid">
        <div class="card" @click="composeOpen = true" style="background-image: url('/grid_design/images/aisa.png');">
          <div class="info">
            <h2>Aisa</h2>
            <p>20 · 3 km away · active 2 hours ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/jenny.png');">
          <div class="info">
            <h2>Jenny</h2>
            <p>19 · 3 km away · active 3 days ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/destiny.png');">
          <div class="info">
            <h2>Destiny</h2>
            <p>18 · 12 km away · active 43 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <div class="card" style="background-image: url('/grid_design/images/samantha.png');">
          <div class="info">
            <h2>Samantha</h2>
            <p>18 · active 49 minutes ago</p>
          </div>
        </div>
        <!-- Add more cards as needed -->
      </div>
    </div>
  </div>
  <!--user card modal-->
  <UserCardModal
      :open-modal="composeOpen"
      :close-modal="closeComposeModal"
  />
</template>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.card {
  position: relative;
  height: 300px;
  background-size: cover;
  background-position: center;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  align-items: flex-end;
  transition: transform 0.5ms;
}

.card:hover {
  transform: scale(1.02);
}

.info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(1px);
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