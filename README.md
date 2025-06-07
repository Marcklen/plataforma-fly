# 🛠️ Plataforma Fly

Este repositório representa o **projeto principal** que centraliza os microserviços que compõem a Plataforma Fly — uma arquitetura modular baseada em Spring Boot, voltada para autenticação, usuários e comunicação assíncrona.

> Este projeto é fruto do **"Treino Desenvolvedor"**, um desafio técnico com foco em arquitetura em microsserviços, autenticação, mensageria assíncrona, cache distribuído e configuração externa.
>
> 📄 Documento original disponível [neste Gist](https://gist.github.com/Marcklen/7bd61084e9561e5be02d0b0c1d36650d)
---
## 📦 Estrutura do Projeto

```bash
    plataforma-fly/
├── eureka/               # ✅ Serviço de registro e descoberta (Spring Cloud Eureka)
├── config-server/        # ✅ Servidor de configuração centralizado (Spring Cloud Config)
├── gateway-api/          # ✅ API Gateway para roteamento e segurança das requisições
├── usuario-api/          # ✅ CRUD de usuários com controle de acesso
├── auth-api/             # ✅ Autenticação com JWT e controle de sessões (validação no REDIS)
├── email-producer-api/   # ✅ Envia mensagens (emails) para o RabbitMQ 
├── email-consumer-api/   # ✅ Consome mensagens e envia e-mails (Kafka)
└── startup.py            # ❌ Script de inicialização em Python (não iniciado)
```
---
# ☁️ Configurações Externas (Spring Cloud Config)
As configurações de todos os microserviços estão centralizadas no repositório:
🔗 [plataforma-fly-configs](https://github.com/Marcklen/plataforma-fly-configs)
- Os arquivos .yml representam os profiles (dev, prod, etc).
- O config-server é responsável por buscar essas configurações no momento da inicialização.
---
# 🔐 Repositório Privado e Autenticação
Se o repositório de configurações estiver privado, defina variáveis de ambiente para o config-server:

- GITHUB_USERNAME: seu usuário do GitHub
- GITHUB_TOKEN: um token pessoal com acesso de leitura ao repositório
---
# 📝 Funcionalidades Já Implementadas
- 🔐 Autenticação JWT com Spring Security
- ✅ CRUD completo de usuários
- 🧠 Regras de autorização baseadas em campo admin
- 💾 Sessão de usuário armazenada via Spring Session Redis
- 🔗 Integração entre serviços (auth-api → usuario-api) via RestTemplate
- 🎯 Segurança reforçada com filtros, permissões e exceções customizadas
- 🚫 Respostas claras para erros como 403, 401, 404 e conflitos de login

---

## 🧩 Roadmap de Implementação

| Funcionalidade                                | Status       |
|-----------------------------------------------|--------------|
| Frontend Angular 15+                          | ✅ Integrado |
| `/email` (envio via RabbitMQ)                 | ✅ Integrado|
| Kafka (consumidor de mensagens assíncronas)   | ✅ Integrado |
| Config Server externo e dinâmico              | ✅ Integrado |
| Java Config (ConnectionFactory, etc.)         | ✅ Integrado |
| Swagger / OpenAPI                             | ✅ Integrado |
| WebService SOAP (bônus)                       | ❌ Não iniciado |
| Perfis `dev` e `prod` com `application-*.yml` | ✅ Parcial |

---

## 🐳 Docker

Cada módulo será conteinerizado individualmente. O arquivo `docker-compose.yml` orquestrará:

- Redis
- RabbitMQ
- Kafka
- Todos os serviços (`auth`, `usuario`, `email`, etc)

--- 
## ✅ Ordem correta de inicialização dos serviços

A sequência de inicialização garante que as configurações externas estejam disponíveis **antes** de qualquer serviço que dependa delas.

### 1️⃣ config-server
- 🔹 **Função**: Central de configuração externa via Spring Cloud Config
- 🔹 **Motivo**: Fornece arquivos como `*-dev.yml` para os serviços via `bootstrap.yml`
- 🔹 **Dependências**: Nenhuma (deve ser o primeiro)

---

### 2️⃣ eureka-server
- 🔹 **Função**: Serviço de registro e descoberta (Service Discovery)
- 🔹 **Motivo**: Só pode ser inicializado **após** o `config-server`, pois também usa `bootstrap.yml` com `import: configserver`
- 🔹 **Dependências**: `config-server`

---

### 3️⃣ gateway-api
- 🔹 **Função**: Roteador central de APIs via Spring Cloud Gateway
- 🔹 **Depende de**:
    - `config-server` → para importar configurações (via `bootstrap.yml`)
    - `eureka-server` → para localizar os serviços registrados

---

### 4️⃣ Serviços de negócio

- `auth-api`
- `usuario-api`
- `email-producer-api`
- `email-consumer-api`

🔹 **Dependências**:
- `config-server` → para suas configurações
- `eureka-server` → para se registrarem
- Recursos externos conforme o caso:
    - `Redis`: `auth-api`, `usuario-api`
    - `Kafka` e `RabbitMQ`: `email-consumer-api` e `email-producer-api`
---

### ✅ Observação importante
Todos os serviços devem ser iniciados com:

```bash

-Dspring.profiles.active=dev
```

---
## 🧠 Observação Final

A plataforma foi pensada com escalabilidade, segurança e modularidade em mente. Cada microserviço tem responsabilidade única e comunicação clara com os demais.
Em breve subo o documento que detalha cada uma das tecnologias utilizadas, suas versões e como cada uma se encaixa na arquitetura geral, e também a razão(motivo) de ter criado esse projeto.
