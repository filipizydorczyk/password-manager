<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from "vue";
import { PasswordEntity } from "./types";
import { useStore } from "./app.store";
import { getPasswordByName } from "./api";
// useHead({
//   meta: [{ name: "viewport", content: "width=device-width, initial-scale=1" }],
//   link: [{ rel: "icon", href: "/favicon.ico" }],
//   htmlAttrs: {
//     lang: "en",
//   },
// });

const title = "PassGuard - Modern Password Manager";
const description = "Secure and simple password management for everyone.";

// useSeoMeta({
//   title,
//   description,
//   ogTitle: title,
//   ogDescription: description,
//   ogImage: "https://ui.nuxt.com/assets/templates/nuxt/starter-light.png",
//   twitterCard: "summary_large_image",
// });

const {
  getPasswords,
  getPin,
  setPin,
  getPinLength,
  fetchPinLength,
  getPasswordSecretByName,
  fetchPasswords,
  addPassword,
  removePassword,
} = useStore();

const passwords = getPasswords();

const searchDialogOpen = ref(false);

const showPinModal = ref(true);
const pinError = ref(false);
const pinLen = getPinLength();
const pin = getPin();

const selectedPassword = ref<PasswordEntity | undefined>(undefined);
const showDetailModal = ref(false);

const removePasswordData = ref<PasswordEntity | undefined>(undefined);
const showRemoveModal = ref(false);

const formInitData = ref<PasswordEntity | undefined>(undefined);
const openForm = ref(false);

const openDetail = async (pwd: PasswordEntity) => {
  showDetailModal.value = true;

  const pass = await getPasswordSecretByName(pwd.name);
  const entity: PasswordEntity = { ...pwd, password: pass };

  selectedPassword.value = entity;
};

const openRemovePassword = async (pwd: PasswordEntity) => {
  showRemoveModal.value = true;
  removePasswordData.value = pwd;
};

const handleRefresh = () => {
  if (!pin.value) {
    showPinModal.value = true;
  } else {
    fetchPasswords();
  }
};

const handleAdd = () => {
  formInitData.value = undefined;
  openForm.value = true;
};

const handleEdit = async (pwd: PasswordEntity) => {
  openForm.value = true;

  const pass = await getPasswordSecretByName(pwd.name);
  const entity: PasswordEntity = { ...pwd, password: pass };

  formInitData.value = entity;
};

const handleRemove = (pwd: PasswordEntity) => {
  openRemovePassword(pwd);
};

const handleRemoveConfirmed = async () => {
  if (removePasswordData.value) {
    await removePassword(removePasswordData.value.name).then(() => {
      showRemoveModal.value = false;
      removePasswordData.value = undefined;
    });
  }
};

const handlePinSubmit = async (pin: string) => {
  const response = await setPin(pin);

  if (!response) {
    pinError.value = true;
  } else {
    showPinModal.value = false;
  }
};

const handleFormSubmit = async (body: { name: string; password: string }) => {
  await addPassword(body.name, body.password).then(() => {
    openForm.value = false;
    formInitData.value = undefined;
  });
};

onMounted(() => {
  fetchPinLength();

  document.addEventListener("keydown", function (e) {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "p") {
      e.preventDefault();
      searchDialogOpen.value = true;
    }
  });
});
</script>

<template>
  <UApp>
    <div class="min-h-screen bg-neutral-50 dark:bg-neutral-950 font-sans">
      <AppHeader @refresh="handleRefresh" @add="handleAdd" />

      <PasswordList
        :passwords="passwords"
        @select="openDetail"
        @edit="handleEdit"
        @remove="handleRemove"
        @add="handleAdd"
      />

      <PinDialog v-model:open="showPinModal" :pinlength="pinLen" :pinerror="pinError" @submit="handlePinSubmit" />

      <PasswordDetailDialog
        v-model:showDetailModal="showDetailModal"
        :password="selectedPassword"
        @edit="handleEdit"
        @remove="handleRemove"
      />

      <PasswordFormDialog v-model:open="openForm" :initData="formInitData" @submit="handleFormSubmit" />

      <ActionConfirmDialog
        v-model:open="showRemoveModal"
        text="Do you want to remove this password?"
        @confirm="handleRemoveConfirmed"
      />

      <SearchDialog v-model:open="searchDialogOpen" :passwords="passwords" @addnew="handleAdd" @open="openDetail" />
    </div>
  </UApp>
</template>

<style>
@import "tailwindcss";
@import "@nuxt/ui";

@theme static {
  --font-sans: "Public Sans", sans-serif;

  --color-green-50: #effdf5;
  --color-green-100: #d9fbe8;
  --color-green-200: #b3f5d1;
  --color-green-300: #75edae;
  --color-green-400: #00dc82;
  --color-green-500: #00c16a;
  --color-green-600: #00a155;
  --color-green-700: #007f45;
  --color-green-800: #016538;
  --color-green-900: #0a5331;
  --color-green-950: #052e16;
}

/* Custom styles for PIN input appearance if needed */
input[type="password"] {
  text-align: center;
}
</style>
