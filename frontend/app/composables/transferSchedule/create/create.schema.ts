import * as z from 'zod'

export const validadeSchema = z.object({
  sourceAccount: z.string().min(1, 'A conta de origem é obrigatória.')
    .regex(/^\d{10}$/, 'A conta de origem deve possuir exatamente 10 dígitos.'),
  destinationAccount: z.string().min(1, 'A conta de destino é obrigatória.')
    .regex(/^\d{10}$/, 'A conta de destino deve possuir exatamente 10 dígitos.'),
  amount: z.number({ error: 'O valor da transferência é obrigatório.' })
    .min(0.01, 'O valor da transferência deve ser maior que zero.'),
  transferDate: z.coerce.date({ error: 'A data da transferência é obrigatória.' })
    .min(new Date(new Date().toDateString()), 'A data da transferência deve ser hoje ou uma data futura.')
})

export type Schema = z.output<typeof validadeSchema>
export type SchemaInput = z.input<typeof validadeSchema>
