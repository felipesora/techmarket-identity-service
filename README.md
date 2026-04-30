<div align="center">

# 🔐 TechMarket Identity Service

### Serviço responsável pela identidade, autenticação e autorização da plataforma TechMarket.

<br/>

[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

</div>

---

## 📋 Índice

- [Sobre o Identity Service](#-sobre-o-identity-service)
- [Principais Funcionalidades](#️-principais-funcionalidades)
- [Arquitetura e Papel no Sistema](#-arquitetura-e-papel-no-sistema)
- [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [Dependências Relevantes](#-dependências-relevantes)
- [Fluxo de Autenticação](#-fluxo-de-autenticação)
- [Boas Práticas Aplicadas](#-boas-práticas-aplicadas)
- [Integração com Outros Serviços](#-integração-com-outros-serviços)
- [Repositórios](#-repositórios)
- [Autor](#-autor)

--- 

## 💡 Sobre o Identity Service

O **Identity Service** é o microsserviço central de segurança da arquitetura do TechMarket. Ele gerencia todo o ciclo de autenticação de usuários, incluindo cadastro, login e emissão de tokens JWT.

Dentro da arquitetura baseada em microsserviços, ele atua como **provedor de identidade**, sendo integrado ao **API Gateway**, que valida os tokens em cada requisição protegida.

Esse serviço foi projetado com foco em:

* **Segurança**
* **Desacoplamento**
* **Escalabilidade**
* **Responsabilidade única (SRP)**

---

## ⚙️ Principais Funcionalidades

* 🔐 Cadastro de usuários
* 🔑 Autenticação (login)
* 🎫 Geração de tokens JWT
* 🛡️ Controle de acesso baseado em roles (perfis)
* ✔️ Validação de credenciais
* 🔄 Integração com API Gateway para autenticação distribuída
* 🔍 Registro no Eureka (Service Discovery)

---

## 🧱 Arquitetura e Papel no Sistema

O Identity Service se posiciona como:

```
Frontend → Gateway → Identity Service → Banco de Dados
```

### Responsabilidades:

| Responsabilidade | Descrição                  |
| ---------------- | -------------------------- |
| Autenticação     | Validação de login e senha |
| Autorização      | Definição de roles/perfis  |
| Tokenização      | Emissão de JWT             |
| Persistência     | Armazenamento de usuários  |
| Integração       | Registro no Eureka         |

---

## 🛠️ Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot 3.5
* Spring Web
* Spring Security
* Spring Data JPA
* Spring Validation

### Segurança

* JWT (JSON Web Token)
* Biblioteca `java-jwt` (Auth0)

### Banco de Dados

* PostgreSQL

### Cloud & Infra

* Spring Cloud Netflix Eureka Client
* Docker

### Utilitários

* Lombok

---

## 📦 Dependências Relevantes

Principais dependências do projeto:

* `spring-boot-starter-security`
* `spring-boot-starter-data-jpa`
* `spring-boot-starter-web`
* `java-jwt`
* `spring-cloud-starter-netflix-eureka-client`
* `postgresql`

---

## 🔐 Fluxo de Autenticação

1. Usuário envia credenciais (email/senha)
2. Identity Service valida no banco
3. Se válido:

   * Gera token JWT
   * Retorna para o cliente
4. Cliente envia token nas requisições
5. Gateway valida o token

---

## 📊 Boas Práticas Aplicadas

* Arquitetura em camadas (Controller → Service → Repository)
* Separação de responsabilidades
* DTOs para transporte de dados
* Validações com Bean Validation
* Segurança centralizada com Spring Security
* Uso de JWT stateless (sem sessão)

---

## 🔗 Integração com Outros Serviços

| Serviço         | Integração           |
| --------------- | -------------------- |
| Gateway         | Roteamento do serviço   |
| Discovery       | Registro via Eureka  |
| Demais serviços | Usam o token JWT |

---

## 📁 Repositórios

O TechMarket é organizado como um **monorepo com submódulos Git**. Cada serviço possui seu próprio repositório:

| Serviço | Descrição | Repositório |
|---------|-----------|-------------|
| 🗂️ **techmarket** | Repositório principal (monorepo + Docker Compose) | [github.com/felipesora/techmarket](https://github.com/felipesora/techmarket) |
| 🔍 **discovery-service** | Eureka Server para service discovery | [github.com/felipesora/techmarket-discovery-service](https://github.com/felipesora/techmarket-discovery-service) |
| 🌐 **gateway-service** | API Gateway com Spring Cloud Gateway | [github.com/felipesora/techmarket-gateway-service](https://github.com/felipesora/techmarket-gateway-service) |
| 🔐 **identity-service** | Autenticação e gerenciamento de usuários (JWT) | [github.com/felipesora/techmarket-identity-service](https://github.com/felipesora/techmarket-identity-service) |
| 📦 **product-service** | Catálogo e gerenciamento de produtos | [github.com/felipesora/techmarket-product-service](https://github.com/felipesora/techmarket-product-service) |
| 🛒 **order-service** | Criação e acompanhamento de pedidos | [github.com/felipesora/techmarket-order-service](https://github.com/felipesora/techmarket-order-service) |
| 💳 **payment-service** | Processamento de pagamentos via mensageria | [github.com/felipesora/techmarket-payment-service](https://github.com/felipesora/techmarket-payment-service) |
| 🖥️ **techmarket-web** | Frontend da plataforma em Angular | [github.com/felipesora/techmarket-web](https://github.com/felipesora/techmarket-web) |


---

## 👨‍💻 Autor

Desenvolvido por **Felipe Sora**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/felipesora)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/felipesora)
