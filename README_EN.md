# LibroNova 📚 (Java Module 5.1)

Library management system using layered architecture (DAO + Service + View) with JDBC, transactions, validations, CSV export, logging and unit tests (JUnit 5).  
User interface with `JOptionPane` menus for fast flow (Books, Members, Loans, Export).

## Table of contents

- [Architecture](#architecture)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Run](#run)
- [Quick use](#quick-use)
- [Exports](#exports)
- [Logs](#logs)
- [Tests](#tests)
- [Design choices](#design-choices)
- [Diagram](#diagram)

---

## Architecture

- **Layers**

  - `model/`: entities (`Book`, `Member`, `User`, `Loan`)
  - `dao/` + `dao/jdbc/`: DAO interfaces and JDBC implementations (prepared statements, mapping `ResultSet`)
  - `service/`: business logic, validations, transactions in loans and returns
  - `view/`: `JOptionPane` menus (Books, Members, Loans, Export)
  - `config/`: `DbManager` (connection), `AppConfig` (properties/logging)
  - `util/`: `AppLogger` (logs + “HTTP simulation”), `CSVExporter`
  - `exception/`: custom exceptions (duplicate ISBN, low stock, etc.)

- **Patterns**
  - DAO, Service, and Decorator pattern in `UserService.create()` (sets `role=ASSISTANT`, `status=ACTIVE` by default without changing DAO).
  - JDBC transactions: `setAutoCommit(false)` → many operations → `commit()` / `rollback()`.

## Requirements

- Java 17+
- Maven 3.8+
- MySQL 8+
- NetBeans (optional) or any IDE

## Installation

1. Clone the repo:
   ```bash
   git clone https://github.com/AnSan29/entregaPruebaDesempenio001
   cd entregaPruebaDesempenio001
   ```
2. Create the database and tables (see `bd.sql`). Example:
   ```sql
   CREATE DATABASE IF NOT EXISTS libronova CHARACTER SET utf8mb4;
   USE libronova;
   -- Tables: user, member, book, loan (with indexes)
   ```
3. Build the project:
   ```bash
   mvn clean package
   ```

## Configuration

File: `src/main/resources/config.properties`

```properties
db.url=jdbc:mysql://localhost:3306/libronova
db.user=root
db.password=

diasPrestamo=7
multaPorDia=1500
export.dir=export
```

Logging: `src/main/resources/log.properties`  
Output: console + file `app.log` in project root.

## Run

- From IDE: run `com.mycompany.libronova.LibroNova`.
- From JAR file:
  ```bash
  java -jar target/libronova-1.0.0.jar
  ```

## Quick use

- **Books**: create, edit, list, delete; unique ISBN validation.
- **Members**: create, edit, activate/deactivate; search by name.
- **Loans**: create (check member ACTIVE and stock); return (calculate fine and update stock).
- **Export**: `libros_export.csv` and `prestamos_vencidos.csv` inside `export/` folder.

## Exports

- `export/libros_export.csv`
- `export/prestamos_vencidos.csv`

## Logs

- File `app.log` (java.util.logging).
- “HTTP simulation” lines:
  - `POST /users | User created: ...`
  - `POST /login | Session started: ...`
  - `GET /export/books | File: ...`

## Tests

- Unit: `LoanServiceTest` (fine), `BookServiceTest` (duplicate ISBN).
- Integration: `LoanFlowIT` (loan and return with stock and fine).

```bash
mvn -q test
```

## Design choices

- **DAO/Service**: separates data access from logic, easy to maintain and test.
- **Decorator in User**: adds default values in `create()` without changing DAO.
- **Transactions**: loan and return are atomic operations (insert/update + stock update).
- **Validations**: unique ISBN, length ≤ 20, ACTIVE member, stock > 0, valid roles and states.
- **Properties**: loan days and fine per day in `config.properties` or VM options.
- **Logging**: errors and actions to file; clear user messages.

## Diagram

![App Screenshot](diagrama-clases.png)

---
