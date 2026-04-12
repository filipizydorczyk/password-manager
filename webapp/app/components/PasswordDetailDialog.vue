<script lang="ts" setup>
import { ref } from "vue";
import type { PasswordEntity } from "../types";

const props = defineProps<{
  password?: PasswordEntity;
}>();

const showDetailModal = defineModel<boolean>("showDetailModal", { default: false });

const emit = defineEmits<{
  (e: "edit", password: PasswordEntity): void;
  (e: "remove", password: PasswordEntity): void;
}>();

const toast = useToast();
const showPasswordValue = ref(true);

const handleEdit = (pwd: PasswordEntity) => {
  emit("edit", pwd);
};

const handleRemove = (pwd: PasswordEntity) => {
  emit("remove", pwd);
};

const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text);
  toast.add({
    title: "Copied",
    color: "success",
  });
};
</script>

<template>
  <UModal v-model:open="showDetailModal" :close="false">
    <template #body>
      <div v-if="password">
        <div class="flex items-center justify-between mb-8">
          <div class="flex items-center gap-4">
            <div class="p-3 bg-neutral-100 dark:bg-neutral-800 rounded-xl">
              <span class="font-bold m-2">{{ password.name[0]?.toUpperCase() }}</span>
            </div>
            <div>
              <h3 class="text-2xl font-bold text-neutral-900 dark:text-white">
                {{ password.name }}
              </h3>
              <!-- <p class="text-neutral-500 dark:text-neutral-400">
                {{ password.username }}
              </p> -->
            </div>
          </div>
          <div class="flex gap-2">
            <UButton icon="i-lucide-pencil" variant="ghost" color="neutral" size="sm" @click="handleEdit(password)" />
            <UButton
              icon="i-lucide-trash-2"
              variant="ghost"
              color="neutral"
              size="sm"
              @click="handleRemove(password)"
            />
          </div>
        </div>

        <div class="space-y-6">
          <div>
            <label class="text-xs font-semibold text-neutral-400 uppercase tracking-wider block mb-2"
              >Username / Email</label
            >
            <div class="flex items-center gap-2 p-3 bg-neutral-100 dark:bg-neutral-800 rounded-lg group">
              <span class="flex-grow font-medium text-neutral-800 dark:text-neutral-200">{{ password.name }}</span>
              <UButton
                icon="i-lucide-copy"
                variant="ghost"
                color="neutral"
                size="sm"
                @click="copyToClipboard(password.name)"
              />
            </div>
          </div>

          <div>
            <label class="text-xs font-semibold text-neutral-400 uppercase tracking-wider block mb-2">Password</label>
            <div class="flex items-center gap-2 p-3 bg-neutral-100 dark:bg-neutral-800 rounded-lg">
              <span class="flex-grow font-mono tracking-wider text-neutral-800 dark:text-neutral-200">
                {{ showPasswordValue ? password.password : "••••••••••••" }}
              </span>
              <UButton
                :icon="showPasswordValue ? 'i-lucide-eye-off' : 'i-lucide-eye'"
                variant="ghost"
                color="neutral"
                size="sm"
                @click="showPasswordValue = !showPasswordValue"
              />
              <UButton
                icon="i-lucide-copy"
                variant="ghost"
                color="neutral"
                size="sm"
                @click="copyToClipboard(password.password || '')"
              />
            </div>
          </div>
        </div>

        <div class="mt-8">
          <UButton block color="neutral" variant="ghost" @click="showDetailModal = false">Close</UButton>
        </div>
      </div>
      <div v-else class="flex items-center justify-center p-12">
        <UIcon name="i-lucide-loader-2" class="w-8 h-8 animate-spin text-neutral-400" />
      </div>
    </template>
  </UModal>
</template>
