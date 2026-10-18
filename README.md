# API de Agendamento de Horários

API REST desenvolvida com Java e Spring Boot para o gerenciamento de agendamentos de serviços. O projeto tem como objetivo
aplicar boas práticas de desenvolvimento backend, utilizando DTOs, persistência de dados, documentação interativa, tratamento de datas e horários e autenticação segura com **JWT próprio** e **login com Google**.

## Tecnologias utilizadas

- **Java 21** — OpenJDK 21
- **Spring Boot**
- **Spring Security** — autenticação e autorização (JWT)
- **Spring Data JPA**
- **PostgreSQL 17** — banco de dados utilizado no Docker
- **JWT** — geração e validação do JWT da aplicação
- **Google API Client** — verificação do token de login com Google (OpenID Connect)
- **Lombok**
- **Springdoc OpenAPI** — documentação interativa da API
- **Docker e Docker Compose** — containerização da aplicação e do banco de dados

## Autenticação

A API utiliza duas camadas de JWT:

1. **Google JWT** — o frontend (ex.: Google Sign-In) envia o token do Google (`credential`) para a aplicação;
2. **JWT próprio** — a aplicação valida o token do Google e devolve um JWT assinado por ela mesma (JWT, HS256), usado nas demais requisições.

### Fluxo de login

1. O usuário se autentica no frontend com o Google e obtém um `credential` (ID token);
2. `POST /google/login` valida o token do Google, cria o usuário automaticamente no banco no primeiro acesso e retorna os dados do usuário + o JWT da aplicação;
3. O `token` retornado deve ser enviado no header das demais requisições:

```http
Authorization: Bearer <token>
```

O token do JWT próprio expira em **1 hora** (`jwt.expiration-ms=3600000`).

### Roles e admin

- Todo usuário cadastrado recebe a role `USER`;
- A role `ADMIN` é atribuída automaticamente quando o e-mail do usuário Google (com e-mail verificado) está presente na lista `ADMIN_EMAILS` (`app.admin-emails`);
- Os endpoints de agendamento permitem o próprio usuário (`#sub == authentication.name`) ou um `ADMIN`.

### Endpoints públicos

- `POST /google/login`
- `/swagger-ui/**`
- `/v3/api-docs/**`

Todos os demais endpoints exigem autenticação.

## Variáveis de ambiente

Configure o arquivo `envs/.env` (um modelo está em `envs/.env.example`):

| Variável | Descrição | Exemplo |
|---|---|---|
| `DATABASE_HOSTNAME` | Host do PostgreSQL | `db` (Docker) ou `localhost` (local) |
| `DATABASE_PORT` | Porta do PostgreSQL | `5432` |
| `DATABASE_NAME` | Nome do banco | `agendador` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `sua_senha` |
| `POSTGRES_DB` | Banco criado pelo container | `agendador` |
| `POSTGRES_USER` | Usuário criado pelo container | `postgres` |
| `POSTGRES_PASSWORD` | Senha criada pelo container | `sua_senha` |
| `SERVER_PORT` | Porta da aplicação | `8080` |
| `GOOGLE_CLIENT_ID` | Client ID do OAuth do Google Cloud | `xxx.apps.googleusercontent.com` |
| `JWT_SECRET` | Chave secreta para assinar o JWT | `uma_chave_secreta_forte` |
| `ADMIN_EMAILS` | E-mails com permissão de ADMIN (separados por vírgula) | `admin@email.com,outra@email.com` |

**Importante:** crie um projeto no [Google Cloud Console](https://console.cloud.google.com/) com a **Google Identity** habilitada e copie o Client ID para o frontend (que gera o `credential` de login).

## Como executar o projeto

### Opção 1 — Docker Compose

Pré-requisitos: Docker e Docker Compose v2 instalados.

Na raiz do projeto, execute:

```bash
docker compose up -d --build
```

Verifique os containers:

```bash
docker compose ps
```

Acompanhe os logs da aplicação:

```bash
docker compose logs -f app
```

Para parar os containers:

```bash
docker compose down
```

O comando `docker compose down` não remove o volume persistente do banco de dados.

### Opção 2 — Execução local

No Linux ou macOS, execute na raiz do projeto:

```bash
./mvnw spring-boot:run
```

**Importante:** o `application.properties` não é versionado (configuração local). Na primeira execução, copie o modelo antes de iniciar:

```bash
cp src/main/resources/application.properties-modelo src/main/resources/application.properties
```

Configure também as variáveis de ambiente (seção acima) para que a aplicação consiga acessar o
banco de dados e validar os tokens. No seu Docker Compose atual, o PostgreSQL está disponível no computador pela porta `5438`; dentro do
Docker, a aplicação utiliza a porta interna `5432`.

## Documentação da API — Swagger

Com a aplicação em execução, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## Exemplos de requisições

Os exemplos abaixo ilustram os dados utilizados nas operações. Os nomes dos campos e os parâmetros devem corresponder
aos DTOs e endpoints implementados no projeto.

### POST — Login com Google

Endpoint público. Recebe o `credential` (ID token do Google) e retorna o usuário + JWT da aplicação:

```json
{
  "credential": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Resposta:

```json
{
  "usuarioDTO": {
    "nome": "Gustavo",
    "email": "gustavo@email.com",
    "sub": "104875134921395821",
    "role": "USER"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use o `token` retornado no header `Authorization: Bearer <token>` nas requisições abaixo. O `{sub}` do caminho deve ser o mesmo `sub` do usuário autenticado.

### GET — Consultar agendamentos por data

```text
GET /agendamentos/usuarios/{sub}?data=2026-10-16
Authorization: Bearer <token>
```

Exemplo de data:

```text
2026-10-16
```

### POST — Criar um agendamento

```text
POST /agendamentos/usuarios/{sub}
Authorization: Bearer <token>
```

Exemplo de corpo JSON:

```json
{
  "servico": "cabelo",
  "dataHoraAgendamento": "2026-10-16T13:30:00",
  "profissional": "luiz zati",
  "cliente": "gustavo",
  "telefoneCliente": "65115184515181"
}
```

### PUT — Atualizar um agendamento

Exemplo de corpo JSON:

```json
{
  "cliente": "gustavo",
  "dataHoraAntiga": "2026-10-16T13:30:00",
  "servico": "cabelo",
  "dataHoraNova": "2026-10-16T15:30:00",
  "profissional": "luiz zati",
  "telefoneCliente": "65115184515181"
}
```

### DELETE — Excluir um agendamento

O delete não recebe um corpo JSON: o endpoint é implementado com `@RequestParam`, ou seja, os dados vão na **query string** da URL. No Swagger, os campos aparecem como parâmetros de consulta — basta preencher os valores e clicar em *Execute*.

```text
PUT /agendamentos/usuarios/{sub}?cliente=gustavo&dataHoraAgendamento=2026-10-16T15:30:00
Authorization: Bearer <token>
```

| Parâmetro | Tipo | Exemplo |
|---|---|---|
| `cliente` | query param | `gustavo` |
| `dataHoraAgendamento` | query param (ISO) | `2026-10-16T15:30:00` |

## Docker — Endereços e portas

- **API:** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui/index.html
- **PostgreSQL no computador:** `localhost:5438`
- **PostgreSQL dentro da rede Docker:** `db:5432`

O endereço `db:5432` é utilizado pela aplicação dentro da rede Docker Compose. A porta `5438` permite acessar o banco a partir do computador.

## Comandos úteis

```bash
# Consultar os containers
docker compose ps

# Acompanhar os logs da API
docker compose logs -f app

# Acompanhar os logs do PostgreSQL
docker compose logs -f db

# Reconstruir e iniciar os serviços
docker compose up -d --build

# Parar os serviços
docker compose down
```

## Objetivo do projeto

Praticar o desenvolvimento de APIs REST com Spring Boot, organização do código, validação de dados, autenticação com JWT e login social com Google, documentação com OpenAPI e execução da aplicação em containers Docker.
