# Unidad 9 — Seguridad en Aplicaciones Web

**Programación Web · Ingeniería de Sistemas · UFPS · 2026**
**Autor:** Andres Felipe Jimenez Ramirez

---

## Descripción

Sistema de autenticación y autorización completo construido con **Spring Security 6**. Implementa registro de usuarios con contraseñas hasheadas mediante BCrypt, login basado en formulario personalizado con Thymeleaf, carga de usuarios desde MySQL a través de `UserDetailsService`, y control de acceso diferenciado por roles `ADMIN` y `USER`.

---

## Tecnologías utilizadas

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.2.5 | Framework base |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia ORM |
| Hibernate | 6.x | Implementación JPA |
| MySQL | 8.x | Base de datos relacional |
| Thymeleaf | 3.x | Motor de plantillas HTML |
| thymeleaf-extras-springsecurity6 | 3.x | Integración Security en vistas |
| BCryptPasswordEncoder | — | Hasheo seguro de contraseñas |
| Maven | 3.x | Gestión de dependencias |

---

## Estructura del proyecto

```
jimenez-post1-u9/
├── src/
│   ├── main/
│   │   ├── java/com/universidad/seguridad/
│   │   │   ├── SeguridadApplication.java       # Punto de entrada
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java         # SecurityFilterChain, BCrypt, AuthProvider
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java         # Login, registro, dashboard, admin
│   │   │   ├── model/
│   │   │   │   └── Usuario.java                # Entidad JPA con validaciones
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java      # findByEmail, existsByEmail
│   │   │   └── service/
│   │   │       ├── UsuarioService.java          # Registro con BCrypt, listarTodos
│   │   │       └── UsuarioDetailsService.java   # Implementa UserDetailsService
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── auth/
│   │       │   │   ├── login.html              # Formulario de inicio de sesión
│   │       │   │   └── registro.html           # Formulario de registro
│   │       │   ├── admin/
│   │       │   │   └── panel.html              # Panel exclusivo ADMIN
│   │       │   └── dashboard.html              # Vista post-login con control por rol
│   │       ├── static/css/
│   │       │   └── style.css                   # Estilos globales
│   │       ├── application.properties          # Configuración DB y JPA
│   │       └── db-init.sql                     # Script de inicialización MySQL
│   └── test/java/com/universidad/seguridad/
│       └── GenerarHashTest.java                # Genera hashes BCrypt para insertar en BD
└── pom.xml
```

---

## Requisitos previos

- **Java 17** instalado y en el PATH
- **Maven 3.6+**
- **MySQL 8.x** corriendo localmente
- IDE recomendado: IntelliJ IDEA o VS Code con extensión Spring

---

## Configuración de MySQL

### 1. Crear base de datos y usuario

```sql
CREATE DATABASE IF NOT EXISTS estudiantes_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'tu_password';
GRANT ALL PRIVILEGES ON estudiantes_db.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configurar credenciales en la aplicación

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/estudiantes_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=appuser
spring.datasource.password=tu_password
```

---

## Ejecución

### 1. Clonar y compilar

```bash
git clone https://github.com/<tu-usuario>/jimenez-post1-u9.git
cd jimenez-post1-u9
mvn clean install -DskipTests
```

### 2. Levantar la aplicación

```bash
mvn spring-boot:run
```

Hibernate creará automáticamente la tabla `usuarios` gracias a `ddl-auto=update`.

### 3. Generar hashes BCrypt para los usuarios de prueba

```bash
mvn test -Dtest=GenerarHashTest
```

Copiar los hashes impresos en consola.

### 4. Insertar usuarios en MySQL

```sql
USE estudiantes_db;

-- Usuario ADMIN (contraseña: admin123)
INSERT INTO usuarios (nombre, email, contrasenia, rol, activo)
VALUES ('Administrador', 'admin@universidad.edu',
        '<hash-de-admin123-generado>', 'ROLE_ADMIN', 1);

-- Usuario USER (contraseña: user123) — alternativa al registro por formulario
INSERT INTO usuarios (nombre, email, contrasenia, rol, activo)
VALUES ('Usuario Prueba', 'user@universidad.edu',
        '<hash-de-user123-generado>', 'ROLE_USER', 1);
```

---

## Usuarios de prueba

| Email | Contraseña (texto claro) | Rol |
|---|---|---|
| `admin@universidad.edu` | `admin123` | ADMIN |
| `user@universidad.edu` | `user123` | USER |

> ⚠️ Estas contraseñas son **solo para pruebas**. En producción nunca se documentan contraseñas en texto claro.

---

## Rutas de la aplicación

| Ruta | Acceso | Descripción |
|---|---|---|
| `/` | Público | Redirige a `/login` |
| `/login` | Público | Formulario de inicio de sesión |
| `/registro` | Público | Formulario de registro de nuevo usuario |
| `/dashboard` | Autenticado | Vista principal post-login |
| `/admin` | Solo ADMIN | Panel con lista de todos los usuarios |
| `/logout` | Autenticado | Cierra la sesión e invalida la cookie |

---

## Conceptos clave implementados

**BCryptPasswordEncoder** — Las contraseñas nunca se almacenan en texto claro. Al registrar, `UsuarioService` llama a `encoder.encode()` antes de persistir. Al autenticar, Spring Security compara automáticamente usando el mismo encoder.

**UserDetailsService** — `UsuarioDetailsService.loadUserByUsername()` consulta MySQL por email y devuelve un objeto `UserDetails` compatible con Spring Security.

**SecurityFilterChain** — Define en un solo lugar las reglas de acceso: rutas públicas, rutas restringidas por rol y configuración del formulario de login/logout.

**sec:authorize en Thymeleaf** — Las vistas renderizan contenido condicionalmente según el rol: `sec:authorize="hasRole('ADMIN')"` oculta elementos que el usuario USER nunca debería ver en el HTML renderizado.

**Protección CSRF** — Thymeleaf + Spring Security inyectan automáticamente el token CSRF en todos los formularios POST, protegiendo contra ataques Cross-Site Request Forgery.

---

## Autor

**Andres Felipe Jimenez Ramirez**
Ingeniería de Sistemas — Universidad Francisco de Paula Santander
Programación Web · 2026
