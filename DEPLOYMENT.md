# UTYCC Student Registration System - Deployment & Production Guide

This guide describes how to build, configure, host, and maintain the **UTYCC Student Registration System** using Docker, Docker Compose, or direct host deployment.

---

## 1. System Overview

- **Application Backend**: Spring Boot 3.5.4
- **Runtime**: Java 21 (LTS)
- **Database**: MariaDB 10.11+
- **Template Engine**: Thymeleaf + JavaScript (ES6)
- **Authentication**: Stateless JWT + Spring Security
- **Containerization**: Docker & Docker Compose

---

## 2. Default Seed Accounts

The initial database script (`docker/initdb/01-init.sql`) seeds the following predefined accounts for testing and administration:

| Role | Email | Password | Access / Capabilities |
| :--- | :--- | :--- | :--- |
| **Super Admin** | `bob.superadmin@example.com` | `1234` | Complete administrative authority, user management |
| **Admin** | `alice.admin@example.com` | `1234` | General administration, review pre-enrollments |
| **HOD** | `charlie.hod@example.com` | `1234` | Head of Department approvals |
| **Student Affairs** | `diana.sa@example.com` | `1234` | Student registration management |
| **Finance** | `eve.finance@example.com` | `1234` | Financial verifications & payments |
| **Multi-Role** | `frank.multirole@example.com` | `1234` | Admin & Finance capabilities |
| **Student 1** | `grace.student@example.com` | `1234` | Student portal, enrollment forms |
| **Student 2** | `henry.student@example.com` | `1234` | Student portal, enrollment forms |

---

## 3. Quick Start with Docker Compose

### Prerequisites
- Docker Engine 24.0+ and Docker Compose v2.20+ installed.

### Step 1: Clone and Configure Environment
Copy the example environment configuration:
```bash
cp .env.example .env
```
*(Optional)* Customize database credentials, application ports, or JWT secret in `.env`.

### Step 2: Launch the Services
Start the application and MariaDB database:
```bash
docker compose up -d --build
```

The system will:
1. Initialize MariaDB and automatically seed the database with Myanmar NRC data, roles, permissions, majors, and initial admin settings from `docker/initdb/01-init.sql`.
2. Wait for MariaDB to report healthy.
3. Build and launch the Spring Boot container.

### Step 3: Access the Application
- **Main Portal**: [http://localhost:8080](http://localhost:8080)
- **Login Page**: [http://localhost:8080/login](http://localhost:8080/login)
- **Health Check**: [http://localhost:8080/auth/ping](http://localhost:8080/auth/ping)

---

## 4. Development Environment (with phpMyAdmin)

To run with **phpMyAdmin** enabled for convenient visual database administration:

```bash
docker compose -f docker-compose.dev.yml up -d
```

- **Application**: [http://localhost:8080](http://localhost:8080)
- **phpMyAdmin**: [http://localhost:8081](http://localhost:8081)
  - **Server**: `mariadb`
  - **Username**: `student_reg` (or `root`)
  - **Password**: `utycc2025` (or root password from `.env`)

---

## 5. Direct Host Deployment (Without Docker)

### Prerequisites
1. **Java Development Kit**: JDK 21+ (`java -version`)
2. **MariaDB Server**: MariaDB 10.11+ running locally on port 3306

### Step 1: Database Setup
Create database and import the seed script:
```bash
mariadb -u root -p -e "CREATE DATABASE student_reg CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mariadb -u root -p -e "CREATE USER 'student_reg'@'localhost' IDENTIFIED BY 'utycc2025';"
mariadb -u root -p -e "GRANT ALL PRIVILEGES ON student_reg.* TO 'student_reg'@'localhost'; FLUSH PRIVILEGES;"
mariadb -u student_reg -putycc2025 student_reg < docker/initdb/01-init.sql
```

### Step 2: Build and Run
```bash
./mvnw clean package -DskipTests
java -jar target/reg-0.0.1-SNAPSHOT.jar
```

---

## 6. Persistent Volumes & Data Directories

When running with Docker, data is persisted across container restarts using Docker volumes:

| Volume Name | Container Path | Purpose |
| :--- | :--- | :--- |
| `student_reg_mariadb_data` | `/var/lib/mysql` | Database files and tables |
| `student_reg_uploads` | `/app/uploads` | Uploaded student attachments, payment receipts, announcements |
| `student_reg_logs` | `/app/logs` | Application rotating log files |

To inspect logs:
```bash
docker compose logs -f app
```

---

## 7. Production Hardening Checklist

When deploying to a public server:
1. **Change Default Passwords**:
   - Update `DB_PASSWORD`, `DB_ROOT_PASSWORD`, and `JWT_SECRET` in `.env`.
   - Update default passwords for admin accounts after first login.
2. **Reverse Proxy & SSL/TLS**:
   - Place Nginx, Traefik, or Caddy in front of port 8080 with automated HTTPS (e.g. Let's Encrypt).
3. **Database Port**:
   - Remove or restrict port `3306` exposure in `docker-compose.yml` so only the internal Docker network can access MariaDB.
4. **Backups**:
   - Schedule regular MariaDB dumps:
     ```bash
     docker exec student-reg-db mariadb-dump -u student_reg -putycc2025 student_reg > backup_$(date +%F).sql
     ```
   - Backup the `/app/uploads` volume to safe storage.

---

## 8. Summary of Fixes Applied

1. **Java Version Alignment**: Standardized compilation to Java 21 LTS (`target=21`, bytecode class version 65) for maximum stability across production runtimes and containers.
2. **Thymeleaf Template Exception**: Resolved SpEL parser crash (`iterStat.index == 0`) in `pre_enrollment_old.html` that broke the old student registration page.
3. **Spring Security Deprecations**: Migrated from `@EnableGlobalMethodSecurity` to Spring Security 6's `@EnableMethodSecurity`.
4. **Enrollment Setting Resilience**: Fixed `EnrollmentCheckService` throwing an unhandled `IllegalStateException` when settings records are not yet initialized.
5. **Configurability**: Converted all database connection strings, file paths, and JWT tokens in `application.properties` to support dynamic environment variable overrides (`${VAR:default}`).
6. **Docker & Compose Packaging**: Added production-ready multi-stage `Dockerfile`, `Dockerfile.local`, `docker-compose.yml`, `docker-compose.dev.yml`, `.dockerignore`, and `.env.example`.
