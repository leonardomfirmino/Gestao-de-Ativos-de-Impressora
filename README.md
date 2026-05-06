# 🖨️ Sistema de Gestão de Impressoras

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/java-17+-red)
![Spring Boot](https://img.shields.io/badge/spring--boot-✔-brightgreen)
![MySQL](https://img.shields.io/badge/mysql-✔-orange)
![Frontend](https://img.shields.io/badge/frontend-HTML%2FCSS%2FJS-blue)

---

## 📌 Sobre o Projeto

Sistema web desenvolvido para gerenciamento de impressoras em uma organização, permitindo o controle de **ativos de TI**, incluindo **status**, **localização** e **histórico de movimentações**.

O sistema busca melhorar a rastreabilidade e organização dos equipamentos, facilitando a gestão pela equipe de TI.

---

## 🧱 Arquitetura do Sistema

O projeto segue uma arquitetura padrão:

* **Backend:** Spring Boot (API REST)
* **Banco de Dados:** MySQL
* **Frontend:** HTML, CSS e JavaScript
* **Comunicação:** HTTP (API REST)

---

## ✨ Funcionalidades

* 📦 Cadastro de impressoras
* 📍 Controle de localização (locais/unidades)
* 🔄 Registro de movimentações
* 📊 Controle de status
* 📜 Histórico completo
* 🔍 Filtros por modelo, status e local

---

## 🧠 Regras de Negócio

Cada impressora possui:

* Modelo
* Número de série
* Endereço IP
* Fila de impressão
* Status atual

### 📊 Status disponíveis:

| Status           | Descrição     |
| ---------------- | ------------- |
| 🟢 Alocado       | Em uso        |
| 🟡 Estoque       | Disponível    |
| 🔵 Backup        | Reserva       |
| 🔴 Troca Técnica | Em manutenção |

---

## 🗄️ Banco de Dados

### 📌 Tabela: `impressoras`

```sql
id_imp BIGINT PRIMARY KEY,
fila_impressao VARCHAR(100),
modelo VARCHAR(100),
serial VARCHAR(50),
status_atual ENUM('TrocaTecnica','Backup','Alocado','Estoque'),
ip VARCHAR(15)
```

### 📌 Tabela: `locais`

```sql
id_local BIGINT PRIMARY KEY,
nome_local VARCHAR(255),
unidade VARCHAR(255)
```

### 📌 Tabela: `movimentacao_impressora`

```sql
id_mov_imp BIGINT PRIMARY KEY,
id_imp BIGINT,
id_local BIGINT,
data_inicio DATETIME,
data_fim DATETIME,
FOREIGN KEY (id_imp) REFERENCES impressoras(id_imp),
FOREIGN KEY (id_local) REFERENCES locais(id_local)
```

---

## 🔄 Fluxo do Sistema

```mermaid
flowchart LR
A[Cadastrar Impressora] --> B[Definir Status]
B --> C[Associar Local]
C --> D[Registrar Movimentação]
D --> E[Consultar Histórico]
```

---

## ⚙️ Tecnologias Utilizadas

* ☕ Java 17
* 🌱 Spring Boot
* 🐬 MySQL
* 🌐 HTML, CSS, JavaScript

---

## 🚀 Como Executar o Projeto

### 🔧 Backend (Spring Boot)

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/seu-repo.git

# Entrar na pasta
cd seu-repo

# Rodar aplicação
./mvnw spring-boot:run
```

---

### 💾 Banco de Dados

1. Criar banco no MySQL
2. Executar os scripts SQL
3. Configurar `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=root
spring.datasource.password=sua_senha
```

---

### 🌐 Frontend

Abrir o arquivo HTML no navegador ou servir via backend.

---

## 📷 Preview

> Adicione prints do sistema aqui

---

## 📌 Status do Projeto

🚧 Em desenvolvimento

---

## 🚀 Melhorias Futuras

* 📊 Dashboard com gráficos
* 🔐 Autenticação de usuários
* 📱 Interface responsiva
* 📷 Leitura de QR Code
* 🔔 Alertas de manutenção

---

## 👨‍💻 Autor

Desenvolvido por você 😄
