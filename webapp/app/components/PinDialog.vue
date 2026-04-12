<script lang="ts" setup>
import { ref, computed } from "vue";

const props = defineProps<{
  pinlength: number;
  pinerror?: boolean;
}>();

const open = defineModel<boolean>("open", { required: true });

const emit = defineEmits<{
  (e: "submit", value: string): void;
}>();

const pin = ref<number[]>([]);

const handleSubmit = () => {
  emit("submit", pin.value.map(String).join(""));
};
</script>

<template>
  <UModal v-model:open="open" :close="false" :prevent-close="true" :ui="{ width: 'sm:max-w-sm' }">
    <template #body>
      <div class="text-center">
        <div class="mb-4 flex justify-center">
          <div class="p-3 bg-green-100 dark:bg-green-900/30 rounded-full">
            <UIcon name="i-lucide-fingerprint" class="w-10 h-10 text-green-600 dark:text-green-400" />
          </div>
        </div>
        <h3 class="text-xl font-bold text-neutral-900 dark:text-white mb-2">Security Check</h3>
        <p class="text-neutral-500 dark:text-neutral-400 mb-6 text-sm">
          Please enter your security PIN to access your vault.
        </p>

        <div class="flex flex-col gap-4">
          <div v-if="!pinlength" class="flex justify-center py-4">
            <UIcon name="i-lucide-loader-2" class="w-8 h-8 animate-spin text-green-600 dark:text-green-400" />
          </div>
          <template v-else>
            <UPinInput class="m-auto" v-model="pin" type="number" :length="pinlength" @complete="handleSubmit" />
            <p v-if="pinerror" class="text-sm text-red-500">Invalid PIN. Try again (Hint: 12345)</p>
            <UButton block size="lg" color="green" @click="handleSubmit">Unlock Vault</UButton>
          </template>
        </div>
      </div>
    </template>
  </UModal>
</template>
