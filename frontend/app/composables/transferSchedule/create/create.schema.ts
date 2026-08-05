import * as z from 'zod'
import { CalendarDate, getLocalTimeZone, today } from '@internationalized/date'

export const validadeSchema = z.object({
  sourceAccount: z.string().min(1, 'A conta de origem é obrigatória.')
    .regex(/^\d{10}$/, 'A conta de origem deve possuir exatamente 10 dígitos.'),
  destinationAccount: z.string().min(1, 'A conta de destino é obrigatória.')
    .regex(/^\d{10}$/, 'A conta de destino deve possuir exatamente 10 dígitos.'),
  amount: z.number({ error: 'O valor da transferência é obrigatório.' })
    .min(0.01, 'O valor da transferência deve ser maior que zero.'),
  transferDate: z.instanceof(CalendarDate, { error: 'A data da transferência é obrigatória.' })
    .refine(date => date.compare(today(getLocalTimeZone())) >= 0, 'A data da transferência deve ser hoje ou uma data futura.')
    .transform(date => date.toDate(getLocalTimeZone()))
})

export type Schema = z.output<typeof validadeSchema>
export type SchemaInput = z.input<typeof validadeSchema>
