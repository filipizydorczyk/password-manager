<script lang="ts" setup>
const emit = defineEmits<{
  (e: "refresh"): void;
  (e: "add"): void;
}>();

const handleRefresh = () => {
  emit("refresh");
};

const handleAdd = () => {
  emit("add");
};
</script>

<template>
  <header
    class="sticky top-0 z-50 w-full border-b border-neutral-200 dark:border-neutral-800 bg-white/80 dark:bg-neutral-900/80 backdrop-blur-md"
  >
    <div class="max-w-4xl mx-auto px-4 h-16 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <div class="p-2 bg-green-500 rounded-lg">
          <UIcon name="i-lucide-shield-check" class="w-6 h-6 text-white" />
        </div>
        <h1 class="text-xl font-bold text-neutral-900 dark:text-white">Password Manager</h1>
      </div>

      <div class="flex items-center gap-2">
        <UButton
          icon="i-lucide-refresh-cw"
          color="neutral"
          variant="ghost"
          @click="handleRefresh"
          aria-label="Refresh"
        />
        <UButton icon="i-lucide-plus" color="neutral" variant="ghost" @click="handleAdd" aria-label="Add new" />
      </div>
    </div>
  </header>
</template>
