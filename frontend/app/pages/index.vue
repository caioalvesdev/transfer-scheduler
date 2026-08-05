<script setup lang="ts">
import type { TableColumn } from '@nuxt/ui'
import { useTransferScheduleCreate } from '~/composables/transferSchedule/create'

interface TransferScheduleModel {
  id: string
  sourceAccount: string
  destinationAccount: string
  amount: number
  fee: number
  transferDate: Date
  schedulingDate: Date
}

const currencyFormatter = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const dateFormatter = new Intl.DateTimeFormat('pt-BR')

const columns: TableColumn<TransferScheduleModel>[] = [
  { accessorKey: 'sourceAccount', header: 'Conta de origem' },
  { accessorKey: 'destinationAccount', header: 'Conta de destino' },
  { accessorKey: 'amount', header: 'Valor', cell: ({ row }) => currencyFormatter.format(row.original.amount) },
  { accessorKey: 'fee', header: 'Taxa', cell: ({ row }) => currencyFormatter.format(row.original.fee) },
  { accessorKey: 'transferDate', header: 'Data da transferência', cell: ({ row }) => dateFormatter.format(new Date(row.original.transferDate)) },
  { accessorKey: 'schedulingDate', header: 'Data de agendamento', cell: ({ row }) => dateFormatter.format(new Date(row.original.schedulingDate)) }
]

definePageMeta({
  layout: 'default',
  scrollToTop: true
})

const toast = useToast()
const apiUrl = useRuntimeConfig().public.apiUrl

const isOpenUploadModal = ref<boolean>(false)

const {
  data,
  refresh,
  pending
} = await useFetch<TransferScheduleModel[]>(apiUrl + '/api/transfer-schedule', {
  key: 'transfer-schedule',
  method: 'GET',
  default: () => [],
  onRequestError({ error }) {
    toast.add({ title: 'Algo deu errado' })
    console.error(error)
  }
})

const {
  formState,
  validationSchema,
  isLoading,
  isSuccess,
  handleSubmit
} = useTransferScheduleCreate()

watch(isSuccess, (success) => {
  if (success) {
    isOpenUploadModal.value = false
    isSuccess.value = false
    refresh()
  }
})
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
        </template>
      </UDashboardNavbar>
      <UDashboardToolbar :ui="{ right: 'gap-3' }">
        <template #right>
          <UButton
            label="Recarregar"
            variant="soft"
            icon="i-lucide-refresh-cw"
            @click="() => refresh()"
          />
          <UModal
            v-model:open="isOpenUploadModal"
            :ui="{ footer: 'justify-end' }"
            title="Adicioanar Transferência"
            description="Adicione uma nova transferência para agendamento."
          >
            <template #body>
              <UForm
                :disabled="isLoading"
                :state="formState"
                :schema="validationSchema"
                class="space-y-4"
                @submit="handleSubmit"
              >
                <UFormField label="Conta de origem" name="sourceAccount">
                  <UInput v-model="formState.sourceAccount" class="w-full"/>
                </UFormField>
                <UFormField label="Conta de destino" name="destinationAccount">
                  <UInput v-model="formState.destinationAccount" class="w-full"/>
                </UFormField>
                <UFormField label="Valor" name="amount">
                  <UInputNumber v-model="formState.amount" class="w-full"/>
                </UFormField>
                <UFormField label="Data da transferência" name="transferDate">
                  <UInput v-model="formState.transferDate" type="date" class="w-full"/>
                </UFormField>
                <UButton
                  class="mt-5"
                  block
                  size="lg"
                  type="submit"
                  :loading="isLoading"
                >
                  Adicionar Transferência
                </UButton>
              </UForm>
            </template>
            <UButton label="Nova Transferência" variant="solid" icon="i-lucide-upload-cloud"/>
          </UModal>
        </template>
      </UDashboardToolbar>
    </template>
    <template #body>
      <UTable
        :data="data"
        :columns="columns"
        :loading="pending"
        class="flex-1"
      />
    </template>
  </UDashboardPanel>
</template>
