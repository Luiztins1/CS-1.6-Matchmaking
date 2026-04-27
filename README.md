# CS 1.6 Matchmaking 🎮

## Sistema de gerenciamento de partidas online de Counter Strike 1.6 (CS1.6).
Esse projeto moderniza a experiência do CS 1.6, trazendo funcionalidades de plataformas modernas, mas com a essência de Lan House.

# 🛠 Tecnologias e ferramentas
* **Java 21** 
* **Spring Boot Web** 
* **JPA/Hibernate**
* **H2Database**
* **PostgreSQL** 

# 🏗 Arquitetura
* **MVC Pattern:** Padrão organizacional de resposabilidades.
* **DTO Pattern:** Padrão para transferência de dados.
* **SOLID:** SRP(Single Responsibility Principle) e DIP (Dependency Inversion Principle).

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
