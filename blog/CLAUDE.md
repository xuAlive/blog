# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run Commands

```bash
# Build project
mvn clean install

# Run application (starts on port 6101)
mvn spring-boot:run

# Run packaged jar
java -jar target/blog-1.0-SNAPSHOT.jar

# Run tests
mvn test
```

## Architecture Overview

This is a Spring Boot 2.2.4 blog application with DeepSeek AI integration.

### Tech Stack
- Java 8, Spring Boot 2.2.4, MyBatis-Plus 3.4.0
- MySQL 8.x with Druid connection pool
- Liquibase for database migrations
- JWT for authentication

### Package Structure (`com.xu.blog`)
- `controller/` - REST endpoints (prefix: `/blog/`)
- `service/` + `service/impl/` - Business logic layer
- `dao/` - Data access objects (complex queries)
- `mapper/` - MyBatis-Plus mappers (simple CRUD)
- `domain/` - Entity classes mapped to database tables
- `param/po/` - Request parameter objects
- `param/vo/` - Response view objects
- `config/` - Spring configurations including `TokenHandlerAdapter` for JWT validation
- `interceptor/` - Permission interceptor for `@RequireRole` and `@RequirePermission`
- `context/` - `UserContext` with ThreadLocal for current user info
- `utils/` - Utilities including `JWTUtil`, `SessionUtil`, `IpAddressUtil`

### Key Patterns
- **Response wrapper**: All APIs return `Response<T>` with code, message, data
- **Permission annotations**: Use `@RequireRole("role")` or `@RequirePermission("perm")` on controller methods
- **User context**: Access current user via `SessionUtil.getAccount()` or `UserContext.get()`
- **Database migrations**: Add changesets in `src/main/resources/liquibase/db/` and include in `master.xml`

### Main Modules
1. **Article Management**: `ArticleController` - CRUD for blog articles
2. **User System**: `UserController`, `SysController` - Registration, login, JWT tokens
3. **Permission System**: `RoleController`, `MenuController`, `PermissionController` - RBAC
4. **DeepSeek AI**: `DeepseekController` - AI chat with conversation history per user
