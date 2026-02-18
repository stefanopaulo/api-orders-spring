# 🚀 Orders API

## 📌 Visão Geral

API REST para gerenciamento de usuários, produtos e pedidos, com relacionamento **N:N** entre produtos e categorias e relacionamento **1:N** entre usuários e pedidos.

---

## 🛠 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Docker
- H2 (perfil de teste)
- Postgres
- JUnit 5
- Mockito
- Swagger

---

## 🧩 Arquitetura

O projeto segue arquitetura em camadas:

- **Controller** – Exposição dos endpoints REST  
- **Service** – Regras de negócio  
- **Repository** – Acesso a dados com JPA  
- **DTOs** – Separação entre modelo de domínio e contratos da API  
- **Mappers** – Conversão entre entidades e DTOs  
- **Tratamento centralizado de exceções**

---

## 🧪 Testes

### ✔ Testes Unitários
- Cobertura das regras de negócio da camada de serviço  
- Uso de Mockito para isolamento de dependências  

---

## 🌐 Endpoints Principais

### 👤 Usuários

| Método | Endpoint      | Descrição                |
| ------ | ------------- | ------------------------ |
| POST   | `/users`      | Criar novo usuário       |
| GET    | `/users`      | Listar todos os usuários |
| GET    | `/users/{id}` | Buscar usuário por ID    |
| PUT    | `/users/{id}` | Atualizar usuário        |
| DELETE | `/users/{id}` | Remover usuário          |


### 📦 Produtos

| Método | Endpoint         | Descrição                |
| ------ | ---------------- | ------------------------ |
| POST   | `/products`      | Criar novo produto       |
| GET    | `/products`      | Listar todos os produtos |
| GET    | `/products/{id}` | Buscar produto por ID    |
| PUT    | `/products/{id}` | Atualizar produto        |
| DELETE | `/products/{id}` | Remover produto          |


### 🏷 Categorias
| Método | Endpoint           | Descrição                  |
| ------ | ------------------ | -------------------------- |
| POST   | `/categories`      | Criar nova categoria       |
| GET    | `/categories`      | Listar todas as categorias |
| GET    | `/categories/{id}` | Buscar categoria por ID    |
| PUT    | `/categories/{id}` | Atualizar categoria        |
| DELETE | `/categories/{id}` | Remover categoria          |


### 🧾 Pedidos
| Método | Endpoint       | Descrição               |
| ------ | -------------- | ----------------------- |
| POST   | `/orders`      | Criar novo pedido       |
| GET    | `/orders`      | Listar todos os pedidos |
| GET    | `/orders/{id}` | Buscar pedido por ID    |


---

## 🗄️ Persistência e Perfis (Spring Profiles)
- O projeto está configurado para operar em diferentes ambientes através de perfis do Spring:

- Perfil de Teste (Padrão no Docker): Utiliza o banco de dados H2 In-Memory. Esta é a forma encontrada para subir o projeto rapidamente, pois não exige nenhuma dependência externa ou configuração de banco de dados.

- Perfil de Desenvolvimento (Dev): Configurado para PostgreSQL.

- As configurações detalhadas estão em src/main/resources/application-dev.properties.

- Para utilizar este perfil, altere a propriedade spring.profiles.active=dev no arquivo application.properties e certifique-se de que uma instância do PostgreSQL esteja rodando localmente.

**Observação:** *A imagem Docker descrita na seção "Como Executar" foi pré-configurada com o Perfil de Teste. Isso garante que a aplicação seja iniciada imediatamente após o clone, sem necessidade de setup manual de banco de dados por parte do avaliador.*

---

## ▶ Como Executar

1. Clone o repositório
```bash
git clone git@github.com:stefanopaulo/api-orders-spring.git
cd api-orders-spring
```

2. Build da imagem Docker

```bash
docker build -t order-api .
```

3. Executar o container
```bash
docker run -p 8080:8080 --name order-api order-api
```

4. Acesso e Testes
   - Após o container subir, a aplicação estará disponível em http://localhost:8080

- Documentação interativa
```bash
http://localhost:8080/swagger-ui/index.html
```

- H2 console
```bash
http://localhost:8080/h2-console
```
  - JDBC URL: jdbc:h2:mem:testdb

  - User: sa | Password: (em branco)

---

## 👨‍💻 Autor

**Stefano Souza**
*Desenvolvedor Java focado em construção de APIs REST bem estruturadas e aplicação de boas práticas de arquitetura.*
