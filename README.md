# API de Agendamento de Horários

Projeto desenvolvido para o gerenciamento de serviços, construído com foco em boas práticas de backend, utilizando DTOs, documentação interativa e tratamento de datas locais.

## Tecnologias Utilizadas
- **Java 21** (Open JDK 21)
- **Spring Boot**
- **Spring Data JPA**
- **Banco de Dados H2** (Em memória)
- **Lombok**
- **Springdoc OpenAPI (Swagger)**

---

###  Como Rodar o Projeto

- No terminal, na raiz do projeto:

- **Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run

### GET
- data que você deseja ver -> 2026-10-16
- data = 2026-10-16

### POST 
- Remover o Z da hora no Swagger
  -  ``` EXEMPLO ``` 
- {
-  "id": 0,
-  "servico": "cabelo",
-  "dataHoraAgendamento": "2026-10-16T00:15:30.00",
-  "profissional": "luiz zati",
-  "cliente": "gustavo",
-  "telefoneCliente": "65115184515181",
-  "dataInsercao": "2026-10-16T00:21:24.792Z"
-  }
### PUT
- {
-   "cliente": "string",
-  "dataHoraAntiga": "2026-10-16T13:30:00",
-  "servico": "string",
-  "dataHoraNova": "2026-10-16T15:30:00",
-  "profissional": "string",
-  "telefoneCliente": "string"
-  }
### DELETE
- Cliente = Nome
- dataHoraAgendamento = 2026-10-16T15:30:00

### SWAGGER
Link; http://localhost:8080/swagger-ui/index.html
