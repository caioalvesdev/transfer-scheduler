# Transfer Scheduler Frontend

Front-end para o sistema de agendamento de transferências financeiras, desenvolvido como desafio técnico.

## Versões e ferramentas

- **Nuxt 4** (Vue 3, TypeScript)
- **Nuxt UI 4** (componentes)
- **Tailwind CSS 4**
- **Zod** (validação de formulário)
- **pnpm** (gerenciador de pacotes)

## Decisões arquiteturais

- **SPA (`ssr: false`)**: a aplicação consome uma API externa, então não há necessidade de renderização no servidor.
- **Composables por feature** (`app/composables/transferSchedule/create`): schema de validação (Zod), store e lógica de submissão do formulário de agendamento ficam isolados por funcionalidade, em vez de centralizados em um único arquivo genérico.
- **URL da API configurável via env** (`NUXT_PUBLIC_API_URL`, ver `nuxt.config.ts`), lida em runtime pelo servidor Nitro — permite apontar para ambientes diferentes (local, produção) sem rebuild da imagem.

## Como rodar

Pré-requisito: Node 22, pnpm.

```bash
pnpm install
pnpm dev
```

A aplicação sobe em `http://localhost:3000`.

Por padrão a API é buscada em `http://localhost:8080`. Para apontar para outro endereço:

```bash
NUXT_PUBLIC_API_URL=http://localhost:8080 pnpm dev
```

## Build de produção

```bash
pnpm build
pnpm preview
```
