# 🛠️ Plataforma Fly

Este repositório representa o **projeto principal** que centraliza os microserviços que compõem a Plataforma Fly — uma arquitetura modular baseada em Spring Boot, voltada para autenticação, usuários e comunicação assíncrona.

---

## 📦 Estrutura do Projeto

```bash
    plataforma-fly/
├── config-server/       # Servidor de configuração centralizado (Spring Cloud Config)
├── gateway-api/         # API Gateway para roteamento e segurança das requisições
├── usuario-api/         # (em implementação) CRUD de usuários
├── auth-api/            # (em implementação) Autenticação com JWT e controle de sessões
├── email-producer-api/  # (a implementar) Envia mensagens para o RabbitMQ
├── email-consumer-api/  # (a implementar) Consome mensagens e envia e-mails
```

# ☁️ Configurações Externas (Spring Cloud Config)
As configurações de todos os microserviços estão centralizadas no repositório:
🔗 [plataforma-fly-configs](https://github.com/Marcklen/plataforma-fly-configs)
- Os arquivos .yml representam os profiles (dev, prod, etc).
- O config-server é responsável por buscar essas configurações no momento da inicialização.

# 🔐 Repositório Privado e Autenticação
Se o repositório de configurações estiver privado, defina variáveis de ambiente para o config-server:

- GITHUB_USERNAME: seu usuário do GitHub
- GITHUB_TOKEN: um token pessoal com acesso de leitura ao repositório

# 📝 Observações
- Por enquanto, apenas os serviços config-server e gateway-api estão implementados.
- Os demais serviços serão adicionados gradualmente conforme o avanço do projeto.
- Toda orquestração será feita via docker-compose (a ser configurado).