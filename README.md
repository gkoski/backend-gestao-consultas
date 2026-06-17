# Vita Odonto - Backend

Sistema de gestão de consultas odontológicas. API REST desenvolvida em Spring Boot para gerenciar usuários, pacientes, dentistas, especialidades e consultas de uma clínica odontológica.

Projeto desenvolvido como parte do programa Wise Start.

## Tecnologias utilizadas

- Java 21+
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token) para autenticação
- MySQL
- Docker e Docker Compose
- Maven

## Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

- JDK 21 ou superior
- Maven (ou usar o `mvnw` incluído no projeto)
- Docker e Docker Desktop
- Uma IDE de sua preferência (recomendado: IntelliJ IDEA)
- Postman ou Insomnia, para testar os endpoints

## Configuração do banco de dados

O banco de dados MySQL é executado em um container Docker, configurado pelo `docker-compose.yml` na raiz do projeto.

Para subir o banco de dados:

```bash
docker compose up -d
```

Esse comando cria automaticamente o banco `sistema_gestao_consultas` e executa os scripts SQL localizados na pasta `db/`, que criam as tabelas e inserem dados de exemplo.

Para verificar se o container está rodando:

```bash
docker compose ps
```

Para parar o container:

```bash
docker compose down
```

Se precisar recriar o banco do zero (por exemplo, após alterar os scripts SQL):

```bash
docker compose down -v
docker compose up -d
```

O comando acima apaga os dados existentes e reexecuta os scripts SQL.

## Configuração da aplicação

O arquivo `application.properties` contém dados sensíveis (senha do banco e chave secreta do JWT) e por isso não é versionado no repositório. Em vez disso, é fornecido um arquivo de exemplo.

Para configurar:

1. Copie o arquivo `src/main/resources/application.properties.example`
2. Renomeie a cópia para `application.properties`, na mesma pasta
3. Preencha os valores reais, seguindo o modelo abaixo:

```properties
spring.application.name=gestao-consultas

spring.datasource.url=jdbc:mysql://localhost:3306/sistema_gestao_consultas
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=sua_chave_secreta_aqui
jwt.expiration=3600000
```

Para gerar uma chave secreta segura para o `jwt.secret`, execute no PowerShell:

```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { [byte](Get-Random -Max 256) }))
```

## Como executar o projeto

1. Clone o repositório:

```bash
git clone https://github.com/Gkoski/backend-gestao-consultas.git
cd backend-gestao-consultas
```

2. Suba o banco de dados com Docker:

```bash
docker compose up -d
```

3. Configure o `application.properties` conforme a seção acima

4. Execute a aplicação pela sua IDE, ou via terminal:

```bash
./mvnw spring-boot:run
```

A aplicação inicia por padrão na porta `8080`.

## Autenticação

A API utiliza autenticação via JWT. O fluxo é o seguinte:

1. O usuário envia email e senha para `POST /auth/login`
2. A API valida as credenciais e retorna um token JWT
3. O token deve ser enviado no header `Authorization` de todas as demais requisições, no formato:

```
Authorization: Bearer {token}
```

4. O token expira após o tempo definido em `jwt.expiration` (em milissegundos), sendo necessário um novo login após a expiração

## Perfis de usuário

O sistema possui dois perfis:

- **ADMIN**: acesso completo, incluindo o gerenciamento de usuários e visualização de todas as consultas
- **DENTISTA**: acesso aos próprios dados e às consultas que ele mesmo registrou

Endpoints de gerenciamento de usuários são restritos exclusivamente ao perfil ADMIN.

## Documentação dos endpoints

Todas as rotas, exceto `/auth/login`, exigem o token JWT no header `Authorization`.

### Autenticação

#### Login

`POST /auth/login`

Corpo da requisição:
```json
{
    "email": "admin@email.com",
    "senha": "123456"
}
```

Resposta (200 OK):
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20i...
```

---

### Usuários

Acesso restrito ao perfil ADMIN.

#### Listar usuários

`GET /usuarios/listar`

Resposta (200 OK):
```json
[
    {
        "id": 1,
        "nome": "Admin",
        "cpf": "000.000.000-00",
        "email": "admin@email.com",
        "perfil": "ADMIN",
        "ativo": true
    }
]
```

#### Buscar usuário por id

`GET /usuarios/{id}`

Resposta (200 OK):
```json
{
    "id": 1,
    "nome": "Admin",
    "cpf": "000.000.000-00",
    "email": "admin@email.com",
    "perfil": "ADMIN",
    "ativo": true
}
```

#### Criar usuário

`POST /usuarios`

Corpo da requisição:
```json
{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "senha": "123456",
    "perfil": "ADMIN"
}
```

Resposta (201 Created):
```json
{
    "id": 2,
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "perfil": "ADMIN",
    "ativo": true
}
```

#### Editar usuário

`PUT /usuarios/{id}`

Corpo da requisição:
```json
{
    "nome": "João Silva Atualizado",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "senha": "123456",
    "perfil": "ADMIN"
}
```

Resposta (200 OK): retorna o usuário atualizado, no mesmo formato acima.

#### Remover usuário

`DELETE /usuarios/{id}`

Resposta (204 No Content)

---

### Pacientes

Acesso liberado para qualquer usuário autenticado.

#### Listar pacientes

`GET /pacientes/listar`

Resposta (200 OK):
```json
[
    {
        "id": 1,
        "nome": "Maria Silva",
        "cpf": "987.654.321-00",
        "email": "maria@email.com",
        "telefone": "(41) 99999-9999"
    }
]
```

#### Buscar paciente por id

`GET /pacientes/{id}`

Resposta (200 OK): retorna um paciente, no mesmo formato acima.

#### Criar paciente

`POST /pacientes`

Corpo da requisição:
```json
{
    "nome": "Maria Silva",
    "cpf": "987.654.321-00",
    "email": "maria@email.com",
    "telefone": "(41) 99999-9999"
}
```

Resposta (201 Created): retorna o paciente criado, incluindo o `id`.

#### Editar paciente

`PUT /pacientes/{id}`

Corpo da requisição: igual ao de criação.

Resposta (200 OK): retorna o paciente atualizado.

---

### Dentistas

Acesso liberado para qualquer usuário autenticado.

#### Listar dentistas

`GET /dentistas/listar`

Resposta (200 OK):
```json
[
    {
        "id": 1,
        "nome": "Dr. Carlos",
        "cpf": "111.222.333-44",
        "email": "carlos@email.com",
        "cro": "CRO-12345",
        "ativo": true
    }
]
```

#### Buscar dentista por id

`GET /dentistas/{id}`

Resposta (200 OK): retorna um dentista, no mesmo formato acima.

#### Criar dentista

`POST /dentistas`

Corpo da requisição:
```json
{
    "nome": "Dr. Carlos",
    "cpf": "111.222.333-44",
    "email": "carlos@email.com",
    "cro": "CRO-12345"
}
```

Resposta (201 Created): retorna o dentista criado, com `ativo: true` por padrão.

#### Editar dentista

`PUT /dentistas/{id}`

Corpo da requisição: igual ao de criação.

Resposta (200 OK): retorna o dentista atualizado.

#### Desativar dentista

`PATCH /dentistas/{id}/desativar`

Resposta (200 OK): retorna o dentista com `ativo: false`.

---

### Especialidades

Acesso liberado para qualquer usuário autenticado.

#### Listar especialidades

`GET /especialidades/listar`

Resposta (200 OK):
```json
[
    {
        "id": 1,
        "nome": "Ortodontia"
    }
]
```

#### Criar especialidade

`POST /especialidades`

Corpo da requisição:
```json
{
    "nome": "Ortodontia"
}
```

Resposta (201 Created): retorna a especialidade criada.

---

### Consultas

Acesso liberado para qualquer usuário autenticado. Usuários com perfil ADMIN visualizam todas as consultas; usuários com perfil DENTISTA visualizam apenas as consultas que eles mesmos registraram.

#### Listar consultas

`GET /consultas/listar`

Resposta (200 OK):
```json
[
    {
        "id": 1,
        "nomePaciente": "Maria Silva",
        "nomeDentista": "Dr. Carlos",
        "nomeUsuario": "Admin",
        "descricao": "Consulta de rotina",
        "dataInicio": "2026-06-10T09:00:00",
        "dataFim": "2026-06-10T10:00:00",
        "dataRegistro": "2026-06-01T14:30:00",
        "motivoCancelamento": null,
        "status": "AGENDADA"
    }
]
```

#### Buscar consulta por id

`GET /consultas/{id}`

Resposta (200 OK): retorna uma consulta, no mesmo formato acima.

#### Criar consulta

`POST /consultas`

Corpo da requisição:
```json
{
    "idPaciente": 1,
    "idDentista": 1,
    "idUsuario": 1,
    "descricao": "Consulta de rotina",
    "dataInicio": "2026-06-10T09:00:00",
    "dataFim": "2026-06-10T10:00:00"
}
```

Resposta (201 Created): retorna a consulta criada, com `status: "AGENDADA"`.

Regras de validação aplicadas na criação:
- A data de início deve ser no futuro
- A data de fim deve ser posterior à data de início
- O dentista não pode ter outra consulta com conflito de horário

#### Cancelar consulta

`PATCH /consultas/{id}/cancelar?motivo={motivo}`

Exemplo: `PATCH /consultas/1/cancelar?motivo=Paciente desmarcou`

Resposta (200 OK): retorna a consulta com `status: "CANCELADA"` e o motivo preenchido.

O parâmetro `motivo` é obrigatório.

#### Finalizar consulta

`PATCH /consultas/{id}/finalizar`

Resposta (200 OK): retorna a consulta com `status: "FINALIZADA"`.

#### Relatórios de consultas

`GET /consultas/relatorios`

Todos os parâmetros são opcionais e podem ser combinados.

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `status` | String | Filtra por status (AGENDADA, CANCELADA, FINALIZADA) |
| `idDentista` | Integer | Filtra pelas consultas de um dentista específico |
| `idPaciente` | Integer | Filtra pelas consultas de um paciente específico |
| `idUsuario` | Integer | Filtra pelas consultas registradas por um usuário específico |
| `dataInicio` | LocalDateTime | Filtra consultas a partir desta data |
| `dataFim` | LocalDateTime | Filtra consultas até esta data |

Exemplo: `GET /consultas/relatorios?status=AGENDADA&idDentista=1`

Resposta (200 OK): retorna uma lista de consultas filtrada, no mesmo formato do endpoint de listagem.

## Estrutura do banco de dados

O banco de dados é composto pelas seguintes tabelas:

- **usuarios**: contas de acesso ao sistema (perfis ADMIN ou DENTISTA)
- **pacientes**: cadastro dos pacientes da clínica
- **dentistas**: cadastro dos profissionais
- **especialidades**: especialidades odontológicas disponíveis
- **dentista_especialidade**: tabela associativa entre dentistas e especialidades (relação muitos-para-muitos)
- **consultas**: consultas agendadas, vinculadas a um paciente, um dentista e o usuário que realizou o agendamento

Os scripts de criação das tabelas (DDL) e de inserção de dados de exemplo (DML) estão na pasta `db/` e são executados automaticamente pelo Docker na primeira inicialização do container.

## Regras de negócio

- Não é permitido conflito de horário para o mesmo dentista em consultas marcadas
- Não é permitido agendar consultas em datas passadas
- O cancelamento de uma consulta exige a informação de um motivo
- Um dentista pode possuir várias especialidades, e uma especialidade pode pertencer a vários dentistas
- Apenas o perfil ADMIN pode gerenciar usuários
- Um dentista só pode visualizar as consultas que ele mesmo registrou
- O perfil ADMIN pode visualizar todas as consultas do sistema
- O horário final de uma consulta deve ser posterior ao horário inicial

## Testando a API

Recomenda-se o uso do Postman ou Insomnia para testar os endpoints.

1. Faça login em `POST /auth/login` para obter o token
2. Em todas as demais requisições, adicione o token no header `Authorization`, usando o tipo **Bearer Token**
3. Siga a documentação de endpoints acima para o corpo e formato de cada requisição
