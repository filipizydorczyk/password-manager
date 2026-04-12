<script lang="ts" setup>
import { PasswordEntity } from "../types";

defineProps<{
  passwords: PasswordEntity[];
}>();

const emit = defineEmits<{
  (e: "select", password: PasswordEntity): void;
  (e: "edit", password: PasswordEntity): void;
  (e: "remove", password: PasswordEntity): void;
  (e: "add"): void;
}>();

const openDetail = (pwd: PasswordEntity) => {
  emit("select", pwd);
};

const handleEdit = (pwd: PasswordEntity) => {
  emit("edit", pwd);
};

const handleRemove = (pwd: PasswordEntity) => {
  emit("remove", pwd);
};

const handleAdd = () => {
  emit("add");
};
</script>

<template>
  <main class="max-w-4xl mx-auto px-4 py-8">
    <div v-if="passwords.length > 0" class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      <UCard
        v-for="pwd in passwords"
        :key="pwd.name"
        class="group hover:ring-2 hover:ring-green-500 transition-all cursor-pointer"
        @click="openDetail(pwd)"
      >
        <div class="flex items-start justify-between gap-4">
          <div class="flex items-center gap-3 overflow-hidden">
            <div
              class="w-10 h-10 flex-shrink-0 flex items-center justify-center rounded-lg bg-neutral-100 dark:bg-neutral-800"
            >
              <span class="font-bold m-2">{{ pwd.name[0]?.toUpperCase() }}</span>
            </div>
            <div class="overflow-hidden">
              <h3 class="font-semibold text-neutral-900 dark:text-white truncate">
                {{ pwd.name }}
              </h3>
              <!-- <p class="text-sm text-neutral-500 dark:text-neutral-400 truncate">
                {{ pwd.username }}
              </p> -->
            </div>
          </div>

          <div class="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity" @click.stop>
            <UButton icon="i-lucide-pencil" variant="ghost" color="neutral" size="sm" @click="handleEdit(pwd)" />
            <UButton icon="i-lucide-trash-2" variant="ghost" color="neutral" size="sm" @click="handleRemove(pwd)" />
          </div>
        </div>
      </UCard>
    </div>

    <!-- Empty State -->
    <div v-else class="flex flex-col items-center justify-center py-20 text-center">
      <UIcon name="i-lucide-lock-keyhole" class="w-16 h-16 text-neutral-300 dark:text-neutral-700 mb-4" />
      <h2 class="text-xl font-semibold text-neutral-900 dark:text-white">No passwords found</h2>
      <p class="text-neutral-500 dark:text-neutral-400 mt-2">Start by adding your first secure entry.</p>
      <UButton class="mt-6" color="green" icon="i-lucide-plus" @click="handleAdd">Add Password</UButton>
    </div>
  </main>
</template>
