<script lang="ts" setup>
const props = defineProps<{
  text: string;
}>();

const open = defineModel<boolean>("open", { default: false });

const emit = defineEmits<{
  (e: "confirm"): void;
}>();

const handleConfirm = () => {
  emit("confirm");
  open.value = false;
};

const handleCancel = () => {
  open.value = false;
};
</script>

<template>
  <UModal v-model:open="open" :close="false">
    <template #body>
      <div>
        <div class="flex items-center gap-4 mb-6">
          <div class="p-3 bg-red-100 dark:bg-red-900/30 rounded-full">
            <UIcon name="i-lucide-alert-triangle" class="w-6 h-6 text-red-600 dark:text-red-400" />
          </div>
          <div>
            <h3 class="text-lg font-bold text-neutral-900 dark:text-white">Confirm Action</h3>
            <p class="text-neutral-500 dark:text-neutral-400 text-sm">
              {{ text }}
            </p>
          </div>
        </div>

        <div class="flex justify-end gap-3">
          <UButton color="neutral" variant="ghost" @click="handleCancel"> Cancel </UButton>
          <UButton color="neutral" variant="ghost" @click="handleConfirm"> Confirm </UButton>
        </div>
      </div>
    </template>
  </UModal>
</template>
