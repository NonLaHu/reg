# UTYCC Student Registration System

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.11%20LTS-blue.svg)](https://mariadb.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg?logo=docker&logoColor=white)](https://www.docker.com/)

A comprehensive university student admission, pre-enrollment, and semester enrollment management web portal designed for the **University of Technology (Yatanarpon Cyber City) - UTYCC**.

---

## Table of Contents

- [Features](#features)
- [Architecture & Tech Stack](#architecture--tech-stack)
- [Project Structure](#project-structure)
- [Pre-configured Default Accounts](#pre-configured-default-accounts)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running with Docker Compose (Recommended)](#running-with-docker-compose-recommended)
  - [Running in Development Mode (with phpMyAdmin)](#running-in-development-mode-with-phpmyadmin)
  - [Running Locally on Host (Without Docker)](#running-locally-on-host-without-docker)
- [Configuration & Environment Variables](#configuration--environment-variables)
- [Key Application Endpoints](#key-application-endpoints)
- [Persistent Storage & Backups](#persistent-storage--backups)
- [Production Hardening](#production-hardening)
- [License](#license)

---

## Features

- **New Student Pre-Enrollment**:
  - Interactive multi-step form with Myanmar NRC (National Registration Card) cascading state/township selector.
  - Grade 12 roll number and entrance credentials validation.
  - Certificate upload and tracking PIN generation.
- **Old Student Pre-Enrollment & Re-registration**:
  - Semester-by-semester major and attending year progression.
  - Student ID card photo upload and verification.
- **Admission & Pre-Enrollment Tracking**:
  - Real-time status lookup using application reference code / tracking PIN.
- **Role-Based Access Control (RBAC)**:
  - Six distinct access levels: `SUPERADMIN`, `ADMIN`, `HOD`, `STUDENT_AFFAIR`, `FINANCE`, and `STUDENT`.
  - Secure stateless authentication using JSON Web Tokens (JWT) and BCrypt password encryption.
- **Administrative Backoffice**:
  - Review submitted pre-enrollments with inline editing capabilities.
  - One-click application approval with automated student account generation.
  - Application rejection workflow with customizable feedback/reasons for students.
  - Payment QR code upload and fee setting management.
  - University news and official announcements with PDF attachment downloads.
- **Student Dashboard**:
  - Academic history and enrollment record review.
  - Profile inspection and secure password change.

---

## Architecture & Tech Stack

- **Backend**: Spring Boot 3.5.4 (Spring MVC, Spring Security 6, Spring Data JPA)
- **Language**: Java 21 (LTS)
- **Database**: MariaDB 10.11+ (InnoDB, UTF-8 `utf8mb4_unicode_ci`)
- **Connection Pool**: HikariCP
- **Frontend**: Thymeleaf Template Engine, HTML5, Vanilla JavaScript (ES6+), CSS3
- **Containerization**: Docker multi-stage builds & Docker Compose

---

## Project Structure

```text
├── Dockerfile                   # Multi-stage production build (builds from source)
├── Dockerfile.local             # Fast build from pre-packaged JAR (<2s)
├── docker-compose.yml           # Production Compose (Spring Boot + MariaDB)
├── docker-compose.dev.yml       # Dev Compose (Spring Boot + MariaDB + phpMyAdmin)
├── .dockerignore                # Build context exclusion rules
├── .env.example                 # Environment variables template
├── DEPLOYMENT.md                # Comprehensive deployment documentation
├── docker/
│   └── initdb/
│       └── 01-init.sql          # Auto-seeding database schema and initial data
├── pom.xml                      # Maven project configuration (Java 21 LTS)
├── src/
│   ├── main/
│   │   ├── java/edu/utycc/reg/
│   │   │   ├── config/          # Spring Security & App configuration
│   │   │   ├── controller/      # REST API & MVC View controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities (User, Student, Major, NRC, etc.)
│   │   │   ├── exception/       # Global exception handling
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   ├── security/        # JWT filter & authentication services
│   │   │   └── service/         # Business logic layer
│   │   └── resources/
│   │       ├── application.properties # App configuration with env var overrides
│   │       ├── static/public/   # Stylesheets, JavaScript, icons, and assets
│   │       └── templates/       # Thymeleaf HTML views
└── uploads/                     # Storage volume for student uploads & slips
```

---

## Pre-configured Default Accounts

The database seed (`docker/initdb/01-init.sql`) includes pre-configured accounts for testing and evaluation:

| Role | Email | Password | Scope / Permissions |
| :--- | :--- | :--- | :--- |
| **Super Admin** | `bob.superadmin@example.com` | `1234` | Full privileges: user management, role grants/revocations, settings |
| **Admin** | `alice.admin@example.com` | `1234` | Review and approve/reject pre-enrollments, manage announcements |
| **Head of Department (HOD)** | `charlie.hod@example.com` | `1234` | Academic and major approvals |
| **Student Affairs** | `diana.sa@example.com` | `1234` | Student registration and enrollment verification |
| **Finance** | `eve.finance@example.com` | `1234` | Payment slip checks and tuition fee settings |
| **Multi-Role** | `frank.multirole@example.com` | `1234` | Combined Admin and Finance roles |
| **Student 1** | `grace.student@example.com` | `1234` | Student dashboard, registration forms |
| **Student 2** | `henry.student@example.com` | `1234` | Student dashboard, registration forms |

---

## Getting Started

### Prerequisites

- **For Docker**: [Docker Engine](https://docs.docker.com/engine/install/) 24.0+ and [Docker Compose](https://docs.docker.com/compose/) v2.20+
- **For Native Host**: JDK 21+ and MariaDB Server 10.11+

---

### Running with Docker Compose (Recommended)

1. **Clone the repository and prepare configuration**:
   ```bash
   cp .env.example .env
   ```

2. **Start all services**:
   ```bash
   docker compose up -d --build
   ```

3. **Verify running containers**:
   ```bash
   docker compose ps
   ```

4. **Access the application**:
   - **Web Portal**: [http://localhost:8080](http://localhost:8080)
   - **Login Page**: [http://localhost:8080/login](http://localhost:8080/login)
   - **Health Check**: [http://localhost:8080/auth/ping](http://localhost:8080/auth/ping)

To follow logs:
```bash
docker compose logs -f app
```

To stop:
```bash
docker compose down
```

---

### Running in Development Mode (with phpMyAdmin)

To launch the application alongside **phpMyAdmin** for database inspection:

```bash
docker compose -f docker-compose.dev.yml up -d
```

- **Application**: [http://localhost:8080](http://localhost:8080)
- **phpMyAdmin**: [http://localhost:8081](http://localhost:8081)
  - **Server**: `mariadb`
  - **Username**: `student_reg` (or `root`)
  - **Password**: `utycc2025` (or root password from `.env`)

---

### Running Locally on Host (Without Docker)

1. **Create and initialize the database**:
   ```bash
   mariadb -u root -p -e "CREATE DATABASE student_reg CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
   mariadb -u root -p -e "CREATE USER 'student_reg'@'localhost' IDENTIFIED BY 'utycc2025';"
   mariadb -u root -p -e "GRANT ALL PRIVILEGES ON student_reg.* TO 'student_reg'@'localhost'; FLUSH PRIVILEGES;"
   mariadb -u student_reg -putycc2025 student_reg < docker/initdb/01-init.sql
   ```

2. **Build and package the Spring Boot JAR**:
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Run the application**:
   ```bash
   java -jar target/reg-0.0.1-SNAPSHOT.jar
   ```

---

## Configuration & Environment Variables

All settings in `src/main/resources/application.properties` can be overridden using environment variables or a `.env` file:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `APP_PORT` / `SERVER_PORT` | `8080` | Port on which the Spring Boot application listens |
| `SPRING_DATASOURCE_URL` | `jdbc:mariadb://mariadb:3306/student_reg?...` | JDBC connection string |
| `DB_USER` / `SPRING_DATASOURCE_USERNAME` | `student_reg` | Database username |
| `DB_PASSWORD` / `SPRING_DATASOURCE_PASSWORD` | `utycc2025` | Database user password |
| `DB_ROOT_PASSWORD` | `utycc_root_2025` | MariaDB root password (Docker container only) |
| `DB_NAME` | `student_reg` | MariaDB database name |
| `DB_PORT` | `3306` | External exposed database port (host mapping) |
| `JWT_SECRET` | `11c2cddd...` | Hex-encoded 256-bit secret key for JWT signing |
| `JWT_EXPIRATION` | `86400000` | JWT token validity in milliseconds (default: 24h) |
| `FILE_UPLOAD_DIR` | `/app/uploads` | Root directory for uploaded documents and images |
| `ADMIN_UPLOAD_DIR` | `/app/uploads/payment` | Subdirectory for payment QR slips |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Hibernate schema update mode (`update`, `validate`, `none`) |
| `SPRING_JPA_SHOW_SQL` | `false` | Enable SQL query printing to console |
| `PMA_PORT` | `8081` | Port for phpMyAdmin in development compose |
| `TZ` | `Asia/Yangon` | Container and database timezone |

---

## Key Application Endpoints

### Public Pages
- `GET /` — University homepage with latest announcements
- `GET /login` — Login page
- `GET /pre_enrollment` — Fresh student pre-enrollment form
- `GET /pre_enrollment_old` — Returning student pre-enrollment form
- `GET /auth/ping` — Lightweight public healthcheck (`{"status":"ok"}`)

### Student Portal (Requires `STUDENT` Role)
- `GET /user_dashboard` — Student personal dashboard
- `GET /new_enrollment_form` — Semester enrollment for new students
- `GET /old_enrollment_form` — Semester enrollment for continuing students

### Administrative Backoffice (Requires `ADMIN` or `SUPERADMIN` Role)
- `GET /admin` — Administrative control panel
- `GET /private/admin/dashboard` — System statistics, enrollment and user counters
- `GET /admin/users` — User and role assignment panel
- `GET /admin/settings` — Payment QR and enrollment window settings

---

## Persistent Storage & Backups

When running in Docker, data is preserved using named volumes:

- `student_reg_mariadb_data`: Holds database tables and records.
- `student_reg_uploads`: Holds applicant uploads, payment slips, and announcement PDFs.
- `student_reg_logs`: Holds application log files (`logs/app.log`).

### Creating a Database Backup
```bash
docker exec student-reg-db mariadb-dump -u student_reg -putycc2025 student_reg > backup_$(date +%F).sql
```

### Restoring from a Backup
```bash
docker exec -i student-reg-db mariadb -u student_reg -putycc2025 student_reg < backup.sql
```

---

## Production Hardening

Before deploying to a public server:
1. **Secrets**: Change `JWT_SECRET`, `DB_PASSWORD`, and `DB_ROOT_PASSWORD` in your production `.env`.
2. **Reverse Proxy & SSL**: Terminate TLS/HTTPS using Nginx, Caddy, or Traefik with automated Let's Encrypt certificates.
3. **Database Security**: Remove the port `3306` port mapping in `docker-compose.yml` so MariaDB is only reachable inside the private Docker network.
4. **Admin Accounts**: Change default passwords (`1234`) immediately upon initial login.

For detailed architecture diagrams and step-by-step guides, refer to [DEPLOYMENT.md](file:///home/morte/Projects/gits/school/school_regist/reg/DEPLOYMENT.md).

---

## License

This project is developed for educational and administrative use at the **University of Technology (Yatanarpon Cyber City)**.
