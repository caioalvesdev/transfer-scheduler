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

interface Page<T> {
  content: T[]
  totalElements: number
}

const currencyFormatter = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const dateFormatter = new Intl.DateTimeFormat('pt-BR')

const columns: TableColumn<TransferScheduleModel>[] = [
  { accessorKey: 'sourceAccount', header: 'Conta de origem' },
  { accessorKey: 'destinationAccount', header: 'Conta de destino' },
  { accessorKey: 'amount', header: 'Valor', cell: ({ row }) => currencyFormatter.format(row.original.amount) },
  { accessorKey: 'fee', header: 'Taxa', cell: ({ row }) => currencyFormatter.format(row.original.fee) },
  {
    accessorKey: 'transferDate',
    header: 'Data da transferência',
    cell: ({ row }) => dateFormatter.format(new Date(row.original.transferDate))
  },
  {
    accessorKey: 'schedulingDate',
    header: 'Data de agendamento',
    cell: ({ row }) => dateFormatter.format(new Date(row.original.schedulingDate))
  }
]

definePageMeta({
  layout: 'default',
  scrollToTop: true
})

const toast = useToast()
const apiUrl = useRuntimeConfig().public.apiUrl

const isOpenUploadModal = ref<boolean>(false)
const transferDateInput = useTemplateRef('transferDateInput')
const page = ref<number>(1)
const pageSize = 10

const {
  data,
  refresh,
  pending
} = await useFetch<Page<TransferScheduleModel>>(apiUrl + '/api/transfer-schedule', {
  key: 'transfer-schedule',
  method: 'GET',
  query: { page: computed(() => page.value - 1), size: pageSize },
  default: () => ({ content: [], totalElements: 0 }),
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
    page.value = 1
    refresh()
  }
})
</script>

<template>
  <UDashboardPanel id="home">
    <template #header>
      <UDashboardNavbar
        title="Home"
        :ui="{ right: 'gap-3' }"
      >
        <template #leading>
          <UDashboardSidebarCollapse />
        </template>

        <template #right>
          <UTooltip
            text="Notifications"
            :shortcuts="['N']"
          >
            <UButton
              color="neutral"
              variant="ghost"
              square
            >
              <UChip
                color="error"
                inset
              >
                <UIcon
                  name="i-lucide-bell"
                  class="size-5 shrink-0"
                />
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
                <UFormField
                  label="Conta de origem"
                  name="sourceAccount"
                >
                  <UInput
                    v-model="formState.sourceAccount"
                    class="w-full"
                  />
                </UFormField>
                <UFormField
                  label="Conta de destino"
                  name="destinationAccount"
                >
                  <UInput
                    v-model="formState.destinationAccount"
                    class="w-full"
                  />
                </UFormField>
                <UFormField
                  label="Valor"
                  name="amount"
                >
                  <UInputNumber
                    v-model="formState.amount"
                    class="w-full"
                  />
                </UFormField>
                <UFormField
                  label="Data da transferência"
                  name="transferDate"
                >
                  <UInputDate
                    ref="transferDateInput"
                    v-model="formState.transferDate"
                    class="w-full"
                  >
                    <template #trailing>
                      <UPopover :reference="transferDateInput?.inputsRef[3]?.$el">
                        <UButton
                          color="neutral"
                          variant="link"
                          size="sm"
                          icon="i-lucide-calendar"
                          aria-label="Selecionar data"
                          class="px-0"
                        />

                        <template #content>
                          <UCalendar v-model="formState.transferDate" class="p-2" />
                        </template>
                      </UPopover>
                    </template>
                  </UInputDate>
                </UFormField>
                <UButton
                  class="mt-5"
                  block
                  size="lg"
                  type="submit"
                  icon="i-lucide-plus"
                  :loading="isLoading"
                >
                  Adicionar Transferência
                </UButton>
              </UForm>
            </template>
            <UButton
              label="Nova Transferência"
              variant="solid"
              icon="i-lucide-calendar-plus"
            />
          </UModal>
        </template>
      </UDashboardToolbar>
    </template>
    <template #body>
      <UCard class="mt-10">
        <template #header>
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold">
              Transferências Agendadas
            </h2>
            <span class="text-sm text-muted-foreground">{{ data.totalElements }} transferências</span>
          </div>
        </template>
        <div class="flex flex-col gap-16">
          <UTable
            :data="data.content"
            :columns="columns"
            :loading="pending"
            loading-color="primary"
            loading-animation="carousel"
            class="flex-1 h-96"
          />
          <UPagination
            v-model:page="page"
            :sibling-count="2"
            class="mx-auto"
            :total="data.totalElements"
            :items-per-page="pageSize"
          />
        </div>
      </UCard>
    </template>
  </UDashboardPanel>
</template>
