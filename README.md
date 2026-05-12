# 🔐 Auth API — JWT Authentication with Spring Boot

REST API de autenticación de usuarios construida con Java y Spring Boot, que implementa seguridad basada en tokens JWT con control de roles.

## 🛠️ Stack tecnológico

- **Java 22** + **Spring Boot 4**
- **Spring Security 7** — seguridad y filtros
- **JWT (jjwt 0.12.3)** — generación y validación de tokens
- **PostgreSQL** — base de datos relacional
- **JPA / Hibernate** — persistencia de datos
- **Lombok** — reducción de código repetitivo
- **JUnit5 + Mockito** — pruebas unitarias (80%+ cobertura)
- **Docker** — contenedorización

## 📋 Endpoints

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/auth/register` | Registrar nuevo usuario | ❌ |
| POST | `/auth/login` | Login y obtener JWT | ❌ |
| GET | `/api/profile` | Obtener perfil del usuario | ✅ |
| GET | `/api/admin/dashboard` | Panel de administración | ✅ ADMIN |

## 🔒 Seguridad

- Contraseñas encriptadas con **BCrypt**
- Tokens **JWT** firmados con clave secreta
- Rutas protegidas mediante **Spring Security Filter Chain**
- Control de acceso por roles: **USER** y **ADMIN**
- Sesiones **stateless** — sin cookies ni sesiones del servidor

## 📸 Demostración

### Registro de usuario
![Register](docs.images/register.png)

### Login
![Login](docs.images/login.png)

### Ruta protegida con JWT
![Profile](docs.images/profile.png)

## ⚙️ Cómo correr el proyecto localmente

### Requisitos
- Java 22
- Maven
- PostgreSQL

### Pasos

1. Clona el repositorio
   \```bash
   git clone https://github.com/Jean0124/auth-api.git
   cd auth-api
   \```

2. Crea la base de datos
   \```sql
   CREATE DATABASE auth_db;
   \```

3. Configura las variables de entorno — copia el archivo de ejemplo
   \```bash
   cp src/main/resources/application-example.properties src/main/resources/application.properties
   \```

4. Edita `application.properties` con tus credenciales locales

5. Corre el proyecto
   \```bash
   ./mvnw spring-boot:run
   \```

6. La API estará disponible en `http://localhost:8080`

## 🧪 Correr pruebas unitarias

\```bash
./mvnw test
\```

Resultado esperado:
\```
Tests run: 9, Failures: 0, Errors: 0
\```

## 📁 Estructura del proyecto

\```
src/
├── main/java/com/api/autentificacion/auth_api/
│   ├── config/
│   │   ├── ApplicationConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── UserController.java
│   ├── dto/
│   │   ├── AuthResponse.java
│   │   ├── LoginRequest.java
│   │   └── RegisterRequest.java
│   ├── model/
│   │   ├── Role.java
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── security/
│   │   └── JwtFilter.java
│   └── service/
│       ├── AuthService.java
│       ├── JwtService.java
│       └── PasswordEncoderService.java
└── test/
└── service/
├── AuthServiceTest.java
└── JwtServiceTest.java
\```

## 🧠 Conceptos aplicados

- Arquitectura en capas (Controller → Service → Repository)
- Clean Code y principios SOLID
- Pruebas unitarias con mocks (Mockito)
- Seguridad stateless con JWT
- Manejo de excepciones
- Validación de datos de entrada

## 👨‍💻 Autor

**Jean Pierre Villamil Sanchez**  
[LinkedIn](https://www.linkedin.com/in/jean0124) · [GitHub](https://github.com/Jean0124)