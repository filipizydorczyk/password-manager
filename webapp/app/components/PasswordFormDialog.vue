<script lang="ts" setup>
import { ref, watch } from "vue";
import type { PasswordEntity } from "../types";

const props = defineProps<{
  initData?: PasswordEntity;
}>();

const open = defineModel<boolean>("open", { default: false });

const emit = defineEmits<{
  (e: "submit", value: PasswordEntity): void;
}>();

const form = ref<PasswordEntity>({
  name: "",
  password: "",
});

watch(() => props.initData, () => {
  if (props.initData) {
    form.value = {
      name: props.initData.name || "",
      password: props.initData.password || "",
    };
  } else {
    form.value = { name: "", password: "" };
  }
});

const handleSubmit = () => {
  emit("submit", { ...form.value });
  open.value = false;
};

const handleCancel = () => {
  open.value = false;
};
</script>

<template>
  <UModal v-model:open="open" :title="initData ? 'Edit Password' : 'Add Password'">
    <template #body>
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <UFormField label="Name" name="name">
          <UInput v-model="form.name" placeholder="Name" class="w-full" />
        </UFormField>

        <UFormField label="Password" name="password">
          <UInput v-model="form.password" placeholder="Password" class="w-full" />
        </UFormField>

        <div class="flex justify-end gap-3 mt-6">
          <UButton color="neutral" variant="ghost" @click="handleCancel"> Cancel </UButton>
          <UButton type="submit" color="neutral" variant="ghost">
            {{ initData ? "Save Changes" : "Add Password" }}
          </UButton>
        </div>
      </form>
    </template>
  </UModal>
</template>
