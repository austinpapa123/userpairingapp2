<script setup lang="ts">
import Button from "@src/components/ui/inputs/Button.vue";
import DropFileUpload from "@src/components/ui/inputs/DropFileUpload.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import {useMessageStore} from "@src/store/store";
import {ref} from "vue";

const messageStore = useMessageStore();
const avatar = ref('');
const channelName = ref('');
const updateChannelName = (newChannelName: string) => {
  channelName.value = newChannelName;
}
const createChannel = () => {
  messageStore.generateNewChannelConversation(channelName.value, "/src/assets/images/cover1.png");
};

</script>

<template>
  <div class="px-5">
    <!--inputs-->
    <div class="mb-5">
      <div class="mb-5">
        <TextInput type="text" placeholder="Channel name" @input="updateChannelName($event.target.value)"label="Name" />
      </div>

      <div>
        <DropFileUpload
            label="Avatar"
            accept="image/*"
            :value="avatar"
            @value-changed="(value) => (avatar = value)"
        />
      </div>
    </div>

    <!--next button-->
    <div class="flex pb-6">
      <div class="grow"></div>
      <Button
        @click="createChannel"
        class="px-5 bg-indigo-400 hover:bg-indigo-500 active:bg-indigo-500"
      >
        Create
      </Button>
    </div>
  </div>
</template>
