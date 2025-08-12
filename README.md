
# ✅ API REST – To-Do-List (Tarefas & Subtarefas)

## 📌 Objetivo
API RESTful para gerenciamento de **tarefas** e **subtarefas**, com regras de negócio:
- Criar, listar (com filtros), atualizar (titulo da tarefa, descrição, data de vencimento e a prioridade) e deletar tarefas
- **Impede concluir** tarefa se houver **subtarefas pendentes**
- CRUD de **subtarefas** associado à tarefa (segue o mesmo padrão, a diferença é que pode-se listar todas as subtarefas associadas a uma tarefa)
- **Impede criar/alterar subtarefas** se a tarefa está **CONCLUÍDA/CANCELADA**

## 🚀 Tecnologias
- **Java 21**, **Spring Boot** (Web, Data JPA)
- **Oracle XE** (ojdbc11)
- **Flyway** (migrações)
- **MapStruct**
- **Lombok**
- **Gradle**
- **Docker & Docker Compose**
- **Springdoc OpenAPI** (Swagger)

## ✅ Pré-requisitos
- Java 21
- Docker + Docker Compose
- (Opcional) Gradle — o projeto já tem `./gradlew`

---

## ▶️ Como executar

### 1. Clone o repositório

```bash
  git clone https://github.com/seu-usuario/desafio-to-to-list
```

### Opção 1: Docker Compose (recomendada)
```bash

docker compose up -d --build
```
A aplicação sobe em: **http://localhost:8080**

### Opção 2: Local (sem Docker)
1) Suba um Oracle XE local e crie usuário/schema compatível (informações no docker-compose)
2) Configure `src/main/resources/application.properties` com a URL/usuário/senha
3) Rode:
```bash
./gradlew clean build -x test
./gradlew bootRun
```

---

## 🗄️ Migrações (Flyway)
Local dos scripts: `src/main/resources/db/migration/`

- `V1__create_tables.sql` → cria **sequences** e tabelas `TAREFA` e `SUB_TAREFA`
- `V2__seed_data.sql` → insere **4 tarefas** e **3 subtarefas** (2 na mesma tarefa e 1 em outra)


---

## 🔗 Endpoints

### 📝 Tarefas
| Método | URL                                                                             | Descrição                                                  |
|---|---------------------------------------------------------------------------------|------------------------------------------------------------|
| **POST** | `http://localhost:8080/tarefas`                                                 | Criar tarefa                                               |
| **GET** | `http://localhost:8080/tarefas/{idTarefa}`                                      | Buscar por ID                                              |
| **GET** | `http://localhost:8080/tarefas?status=…&prioridade=…&dataVencimento=YYYY-MM-DD` | Listar com filtros (todos opcionais)                       |
| **PATCH** | `http://localhost:8080/tarefas/{idTarefa}`                                      | Atualização **parcial** (título/descrição/data/prioridade) |
| **PATCH** | `http://localhost:8080/tarefas/{idTarefa}/status`                               | Atualizar **apenas o status**                              |
| **DELETE** | `http://localhost:8080/tarefas/{idTarefa}`                                      | Remover tarefa                                             |
| **GET** | `http://localhost:8080/tarefas/paginado?status=PENDENTE&prioridade=ALTA&dataVencimento=2025-08-22&page=0&size=5&sort=prioridade,desc&sort=tituloTarefa,asc` | Listar paginado com filtros e ordenação (todos opcionais)  |

#### Exemplos (JSON)
Criar:
```json
{
  "tituloTarefa": "Título tarefa",
  "descricao": "Descrição tarefa",
  "dataVencimento": "2025-08-22",
  "status": "PENDENTE",
  "prioridade": "ALTA"
}
```

Patch parcial:
```json
{
  "tituloTarefa": "API de tarefas v2",
  "dataVencimento": "2025-08-30",
  "prioridade": "MEDIA"
}
```

Atualizar status:
```json
{ "status": "CONCLUIDA" }
```

> Regra: **não permite** `CONCLUIDA` se existir **subtarefa** com status ≠ `CONCLUIDA`.

---

### ✅ Subtarefas
| Método | URL | Descrição |
|---|---|---|
| **POST** | `http://localhost:8080/tarefas/{idTarefa}/subtarefas` | Criar subtarefa ligada à tarefa |
| **GET** | `http://localhost:8080/tarefas/subtarefas/{idSubTarefa}` | Buscar subtarefa por ID |
| **PATCH** | `http://localhost:8080/tarefas/subtarefas/{idSubTarefa}` | Atualização **parcial** (título/status) |
| **PATCH** | `http://localhost:8080/tarefas/subtarefas/{idSubTarefa}/status` | Atualizar **apenas o status** |
| **DELETE** | `http://localhost:8080/tarefas/subtarefas/{idSubTarefa}` | Remover subtarefa |
| **GET** | `http://localhost:8080/tarefas/{idTarefa}/subtarefas` | Listar subtarefas por tarefa |

Criar subtarefa:
```json
{
  "tituloSubTarefa": "Título SubTarefa",
  "status": "PENDENTE"
}
```

> Regras:
> - **Não cria/atualiza** subtarefa se a tarefa-pai estiver **CONCLUÍDA/CANCELADA**.
> - Atualização de status é **idempotente** (se não mudou, não faz nada).

---

## 🔒 Regras de Negócio (resumo)
- Tarefa só pode ser marcada como **CONCLUIDA** se **todas** as suas subtarefas estiverem **CONCLUIDAS**.
- Não é permitido **criar/alterar** subtarefas se a tarefa-pai estiver **CONCLUIDA** ou **CANCELADA**.
- Status padrão ao criar tarefa/subtarefa, se ausente ou inválido: **PENDENTE**.
- Nos endpoints de atualização parcial (PATCH), os campos enviados serão atualizados e os não enviados permanecerão com os valores já existentes.



