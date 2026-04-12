<script lang="ts" setup>
import { computed } from "vue";
import type { PasswordEntity } from "../types";

const props = defineProps<{
  passwords: PasswordEntity[];
}>();

const open = defineModel<boolean>("open", { default: false });

const emit = defineEmits<{
  (e: "open", password: PasswordEntity): void;
  (e: "addnew"): void;
}>();

const groups = computed(() => {
  return [
    {
      id: "passwords",
      label: "Passwords",
      items: props.passwords.map((p) => ({
        id: p.name,
        label: p.name,
        icon: "i-lucide-key",
        entity: p,
      })),
    },
    {
      id: "actions",
      label: "Actions",
      items: [
        {
          id: "add-new",
          label: "Add new password",
          icon: "i-lucide-plus",
          type: "action",
        },
      ],
    },
  ];
});

const handleSelect = (item: any) => {
  if (!item) return;

  if (item.type === "action" && item.id === "add-new") {
    emit("addnew");
  } else if (item.entity) {
    emit("open", item.entity);
  }
  open.value = false;
};
</script>

<template>
  <UModal v-model:open="open" :close="false">
    <template #content>
      <UCommandPalette
        placeholder="Search for a password..."
        :groups="groups"
        :autofocus="true"
        @update:model-value="handleSelect"
      />
    </template>
  </UModal>
</template>
