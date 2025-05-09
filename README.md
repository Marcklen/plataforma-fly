# 🛠️ Plataforma Fly

Este repositório representa o **projeto principal** que centraliza os microserviços que compõem a Plataforma Fly — uma arquitetura modular baseada em Spring Boot, voltada para autenticação, usuários e comunicação assíncrona.

---
## 📦 Estrutura do Projeto

```bash
    plataforma-fly/
├── config-server/        # ✅ Servidor de configuração centralizado (Spring Cloud Config)
├── gateway-api/          # ✅ API Gateway para roteamento e segurança das requisições
├── usuario-api/          # ✅ CRUD de usuários com controle de acesso
├── auth-api/             # ✅ Autenticação com JWT e controle de sessões (validação no REDIS)
├── email-producer-api/   # 🔜 Envia mensagens (emails) para o RabbitMQ  (A implementar)
├── email-consumer-api/   # 🔜 Consome mensagens e envia e-mails (Kafka) (A implementar)
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
|----------------------------------------------|--------------|
| Frontend Angular 7+                          | ❌ Não iniciado |
| `/email` (envio via RabbitMQ)                | 🔜 Em breve |
| Kafka (consumidor de mensagens assíncronas)  | 🔜 Em breve |
| Config Server externo e dinâmico             | ✅ Integrado |
| Java Config (ConnectionFactory, etc.)        | 🔜 Em breve |
| Swagger / OpenAPI                            | 🔜 Em breve |
| WebService SOAP (bônus)                      | ❌ Não iniciado |
| Perfis `dev` e `prod` com `application-*.yml`| ✅ Parcial |

---

## 🐳 Docker

Cada módulo será conteinerizado individualmente. Um `docker-compose.yml` orquestrará:

- Redis
- RabbitMQ
- Kafka
- Todos os serviços (`auth`, `usuario`, `email`, etc)

---
## 🧠 Observação Final

A plataforma foi pensada com escalabilidade, segurança e modularidade em mente. Cada microserviço tem responsabilidade única e comunicação clara com os demais.
Em breve subo o documento que detalha cada uma das tecnologias utilizadas, suas versões e como cada uma se encaixa na arquitetura geral, e também a razão(motivo) de ter criado esse projeto.
