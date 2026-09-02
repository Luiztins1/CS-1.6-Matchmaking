# CS 1.6 Matchmaking 🎮

## Sistema de gerenciamento de partidas online de Counter Strike 1.6 (CS1.6).
Esse projeto moderniza a experiência do CS 1.6, trazendo funcionalidades de plataformas modernas, mas com a essência de Lan House.

# 🛠 Tecnologias e ferramentas
* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot 3.3.4 (Spring Data JPA, Spring Web, Spring Validation, Spring Security, Spring DevTools)
* **Banco de dados:** PostgreSQL
* **Ferramentas de Suporte:** Docker (para criar uma imagem que possa rodar em qualquer máquina), Postman (um cliente para testar as requisições HTTP)

# 🏗 Arquitetura
* **Feature Based Pattern:** Padrão organizacional de responsabilidades.
* **DTO Pattern:** Padrão para transferência de dados.
* **Mapper Pattern:** Padrão para converter entidades.
* **SOLID:** SRP(Single Responsibility Principle) e DIP (Dependency Inversion Principle).

# ⚙️ Configuração das Variáveis de Ambiente

A API utiliza variáveis de ambiente para proteger dados sensíveis (como credenciais do banco de dados). Antes de rodar, certifique-se de ter um arquivo de configuração com as seguintes chaves:
* `DB_USERNAME`: Usuário do banco de dados (ex: `test`)
* `DB_PASSWORD`: Senha do banco de dados (ex: `test123`)

Por motivos de segurança, as credenciais reais do banco de dados e chaves do sistema estão configuradas em arquivos locais protegidos pelo `.gitignore`. 

O repositório disponibiliza um arquivo modelo chamado `application.example.yml` e um `docker-compose.example.yml` contendo a estrutura necessária. Para rodar o projeto localmente na sua IDE, siga os passos abaixo:

### 💾 Application
1. Na raiz do pacote de configurações (`src/main/resources`), **duplique** o arquivo `application.example.yml`.
2. Renomeie a cópia para **`application.yml`** (este nome é ignorado pelo Git e lido pelo Spring).
3. Abra o novo `application.yml` e preencha as variáveis com as suas credenciais locais:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/csm16
    username: ${DB_USERNAME:seu_usuario_aqui}
    password: ${DB_PASSWORD:sua_senha_aqui}
 ```

### 🐋 Docker-Compose
1. Na raiz do pacote de configurações, **duplique** o arquivo `docker-compose.example.yml`.
2. Renomeie a cópia para **`docker-compose.yml`** (este nome é ignorado pelo Git e lido pelo Spring).

* **Observação:** Caso você já tenha o PostgreSQL rodando localmente na porta padrão (5432), mantenha a porta externa mapeada como está no arquivo de exemplo (geralmente 5433:5432) para evitar conflitos. Caso contrário, mude para 5432:5432
```yaml
  mixs:
    image: postgres:16
    volumes:
      - ./Postgres:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: mixs
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USERNAME}"]
      interval: 10s
      timeout: 5s
      retries: 5
```

# 🔁 Estados do Sistema
## ⚙ Match State
Representa os estados em que um Match (partida) se encontra.
* **COLD:** "Frio". O estado inicial de um Match, quando não se tem jogadores no Lobby.
* **WAITING:** Atinge esse estado quando se tem um ou mais jogadores no Lobby. Permanecera nesse estado até que atinga o valor determinado da aquele tipo de partida.
* **READY_MATCH:** Ocorre quando a quantidade necessária para Match é atinginda.
  
## ⚙ Type Match State 
Representa o tipo de Match (partida) que está ocorrendo.
* **DEFAULT:** O valor padrão para qualquer Match (quantidade de Players no Lobby de Match, que é 0).
* **COMPETITIVE:** O valor máximo de Players para uma partida competitiva.
* **DEATHMATCH:** O valor padrão de Players para uma partida mata-mata.

## ⚙ Interaction Event
Representa ações no sistema
* **ENTER:** Uma ação de entrar.
* **EXIT:** Uma ação de sair.
* **MATCHING:** Uma ação de aceitar um Match.

# 🚀 Como Executar

## 🐋 Docker Compose
1. Certifique-se de que o seu arquivo `cred.env` (ou `.env`) está na raiz do projeto com as variáveis preenchidas.
2. Suba os containers com o comando:
   ```bash
   docker compose up --build

3. Para interromper a execução, no terminal do projeto, use o comando:
   ```bash
   docker compose down
