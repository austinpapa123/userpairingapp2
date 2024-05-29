<script setup lang="ts">
import {useMessageStore, useStore} from "@src/store/store";
import NoContacts from "@src/components/states/empty-states/NoContacts.vue";
import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import ContactItem from "@src/components/shared/blocks/ContactItem.vue";
import ScrollBox from "@src/components/ui/utils/ScrollBox.vue";

const store = useStore();
const messageStore = useMessageStore();
const generateCoupleConversation = (contact: any) => {
  console.log("in modals/Contact.vue", contact);
  console.log(contact.username, contact.avatar);
  messageStore.generateNewCoupleConversation(contact.username, contact.avatar);
};
</script>

<template>
  <div class="pb-6">
    <!--contacts-->
    <ScrollBox class="overflow-y-scroll max-h-[200px]">
      <Loading1
        v-if="store.status === 'loading' || store.delayLoading"
        v-for="item in 3"
      />

      <ContactItem
        v-else-if="
          store.status === 'success' &&
          !store.delayLoading &&
          store.user &&
          store.contacts.length > 0
        "
        v-for="(contact, index) in store.contacts"
        :key="index"
        @contact-selected="generateCoupleConversation(contact)"
        :contact="contact"
      />

      <NoContacts vertical v-else />
    </ScrollBox>
  </div>
</template>
