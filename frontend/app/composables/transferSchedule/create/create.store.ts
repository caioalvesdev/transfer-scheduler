import { validadeSchema, type Schema, type SchemaInput } from '.'
import type { FormSubmitEvent } from '@nuxt/ui'
import { getLocalTimeZone, today } from '@internationalized/date'

const apiUrl = useRuntimeConfig().public.apiUrl
const toast = useToast()

export function useTransferScheduleCreate() {
  const validationSchema = validadeSchema
  const isLoading = ref<boolean>(false)
  const isSuccess = ref<boolean>(false)
  const initialFormState = (): Partial<SchemaInput> => ({
    sourceAccount: '',
    destinationAccount: '',
    amount: undefined,
    transferDate: today(getLocalTimeZone())
  })
  const formState = reactive<Partial<SchemaInput>>(initialFormState())

  async function handleSubmit(event: FormSubmitEvent<Schema>): Promise<void> {
    try {
      isLoading.value = true
      await $fetch(apiUrl + '/api/transfer-schedule', {
        method: 'POST',
        body: event.data
      })
      toast.add({ title: 'Transferência agendada com sucesso' })
      Object.assign(formState, initialFormState())
      isSuccess.value = true
    } catch (error) {
      console.error(error)
      toast.add({ title: 'Algo deu errado. Tente novamente mais tarde.' })
    } finally {
      isLoading.value = false
    }
  }

  return {
    isLoading,
    isSuccess,
    formState,
    validationSchema,
    handleSubmit
  }
}
