import * as z from 'zod'
import { pt } from 'zod/locales'

export default defineNuxtPlugin(() => {
  z.config(pt())
})
