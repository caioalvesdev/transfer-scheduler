<script setup lang="ts">
import type { DropdownMenuItem } from "@nuxt/ui/components/DropdownMenu.vue"

definePageMeta({
  layout: 'default',
  scrollToTop: true
})

const items = [
  [
    {
      label: "New mail",
      icon: "i-lucide-send",
      to: "/inbox",
    },
    {
      label: "New customer",
      icon: "i-lucide-user-plus",
      to: "/customers",
    },
  ],
] satisfies DropdownMenuItem[][]

const toast = useToast()

const isOpenUploadModal = ref<boolean>(false)

async function handleRefreshData() {
  toast.add({
    title: "Atualizado",
    description: "Os dados foram atualizados com sucesso.",
    duration: 2500
  })
}
const data = ref([
  {
    id: '4600',
    date: '2024-03-11T15:30:00',
    status: 'paid',
    email: 'james.anderson@example.com',
    amount: 594
  },
  {
    id: '4599',
    date: '2024-03-11T10:10:00',
    status: 'failed',
    email: 'mia.white@example.com',
    amount: 276
  },
  {
    id: '4598',
    date: '2024-03-11T08:50:00',
    status: 'refunded',
    email: 'william.brown@example.com',
    amount: 315
  },
  {
    id: '4597',
    date: '2024-03-10T19:45:00',
    status: 'paid',
    email: 'emma.davis@example.com',
    amount: 529
  },
  {
    id: '4596',
    date: '2024-03-10T15:55:00',
    status: 'paid',
    email: 'ethan.harris@example.com',
    amount: 639
  }
])
console.log(useRuntimeConfig())
</script>

<template>
  <UDashboardPanel id="home">
    <template #header>
      <UDashboardNavbar title="Home" :ui="{ right: 'gap-3' }">
        <template #leading>
          <UDashboardSidebarCollapse/>
        </template>

        <template #right>
          <UTooltip text="Notifications" :shortcuts="['N']">
            <UButton color="neutral" variant="ghost" square>
              <UChip color="error" inset>
                <UIcon name="i-lucide-bell" class="size-5 shrink-0"/>
              </UChip>
            </UButton>
          </UTooltip>
          <UDropdownMenu :items="items">
            <UButton icon="i-lucide-plus" size="md" class="rounded-full" />
          </UDropdownMenu>
        </template>
      </UDashboardNavbar>
      <UDashboardToolbar :ui="{ right: 'gap-3' }">
        <template #left>
        </template>

        <template #right>
          <UButton
            label="Recarregar"
            variant="soft"
            icon="i-lucide-refresh-cw"
            @click="handleRefreshData"
          />
          <UModal
            v-model:open="isOpenUploadModal"
            :ui="{ footer: 'justify-end' }"
            title="Adicioanar Transferência"
            description="Adicione uma nova transferência para agendamento."
          >
            <UButton label="Nova Transferência" variant="solid" icon="i-lucide-upload-cloud"/>
          </UModal>
        </template>
      </UDashboardToolbar>

    </template>
    <template #body>
        <UTable :data="data" class="flex-1" />
    </template>
  </UDashboardPanel>
</template>
