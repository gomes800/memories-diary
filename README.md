# Memories Diary

API em Java/Spring para registrar memórias do dia a dia.

A ideia é um serviço simples, que depois possa ser consumido por um front-end (web ou mobile) para criar e listar lembranças.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Docker / Docker Compose
- Banco de dados Postgresql

## Como rodar o projeto

Na raiz do projeto:

1. Subir os serviços do Docker:

docker-compose up -d

2. Iniciar a aplicação:

mvn spring-boot:run

A API deve ficar disponível em http://localhost:8080

Esta API e um CRUD para lidar com memórias, o usuário pode criar memórias, listar, editar e deletar as suas. E como se fosse um álbum de recordações.
