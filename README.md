# API de Agendamento de Horários

API REST desenvolvida com Java e Spring Boot para o gerenciamento de agendamentos de serviços. O projeto tem como objetivo
aplicar boas práticas de desenvolvimento backend, utilizando DTOs, persistência de dados, documentação interativa e tratamento de datas e horários.

## Tecnologias utilizadas

- **Java 21** — OpenJDK 21
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL 17** — banco de dados utilizado no Docker
- **Lombok**
- **Springdoc OpenAPI** — documentação interativa da API
- **Docker e Docker Compose** — containerização da aplicação e do banco de dados

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

**Importante:** antes de iniciar localmente, configure as variáveis de ambiente para que a aplicação consiga acessar o 
banco de dados. No seu Docker Compose atual, o PostgreSQL está disponível no computador pela porta `5438`; dentro do 
Docker, a aplicação utiliza a porta interna `5432`.

## Documentação da API — Swagger

Com a aplicação em execução, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## Exemplos de requisições

Os exemplos abaixo ilustram os dados utilizados nas operações. Os nomes dos campos e os parâmetros devem corresponder 
aos DTOs e endpoints implementados no projeto.

### GET — Consultar agendamentos por data

Exemplo de data:

```text
2026-10-16
```

### POST — Criar um agendamento

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

Exemplo dos valores utilizados no projeto:

```json
{
  "cliente": "gustavo",
  "dataHoraAgendamento ": "2026-10-16T15:30:00"

}
```

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

Praticar o desenvolvimento de APIs REST com Spring Boot, organização do código, validação de dados, documentação com OpenAPI e execução da aplicação em containers Docker.