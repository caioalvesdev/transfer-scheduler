# Transfer Scheduler

Sistema de agendamento de transferências financeiras, desenvolvido como desafio técnico. O usuário agenda uma transferência informando conta de origem, conta de destino, valor e data da transferência; a taxa é calculada automaticamente com base no prazo até a data escolhida.

## Deploy

- Frontend: https://desafio-frontend.realmtech.cloud
- Backend: https://desafio-backend.realmtech.cloud

## Estrutura do projeto

- [`backend/`](backend/README.md) — API REST em Java 11 + Spring Boot, responsável pelas regras de negócio, cálculo de taxa e persistência.
- [`frontend/`](frontend/README.md) — interface em Nuxt (Vue 3) para cadastrar e listar os agendamentos.

Cada pasta tem seu próprio README com decisões arquiteturais, versões e instruções de subida específicas.

## Rodando localmente

Backend (porta `8080`):

```bash
cd backend
./mvnw spring-boot:run
```

Frontend (porta `3000`), em outro terminal:

```bash
cd frontend
pnpm install
pnpm dev
```

O frontend já aponta para `http://localhost:8080` por padrão (configurável via `NUXT_PUBLIC_API_URL`).

## CI/CD

- `.github/workflows/test.yml` — roda os testes do backend a cada push/PR.
- `.github/workflows/deploy.yml` — ao término bem-sucedido dos testes na branch `master`, builda as imagens Docker de backend e frontend, publica no Docker Hub e sobe os containers na VPS via SSH.
- **Docker**: cada app tem seu próprio `Dockerfile` (`backend/Dockerfile`, `frontend/Dockerfile`), gerando imagens independentes.
- **Dokploy**: usado na VPS para expor os containers nos domínios de produção (`desafio-frontend.realmtech.cloud`, `desafio-backend.realmtech.cloud`).
