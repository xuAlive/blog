# Blog System

基于Spring Boot的博客系统，集成DeepSeek AI对话功能和RBAC权限管理。

## 主要功能

### 文章管理
- 创建/更新文章
- 文章列表查询（分页）
- 文章详情查看

### 用户系统
- 用户注册/登录
- JWT Token认证
- 用户信息管理（昵称、真实姓名、邮箱、身份证、简介等）
- 登录记录（IP地址解析）

### RBAC权限管理
- **角色管理**: ADMIN（管理员）、USER（已验证用户）、GUEST（访客）
- **菜单管理**: 树形菜单结构，支持目录/菜单/按钮类型
- **权限管理**: API/按钮/数据级权限控制
- 支持为角色分配菜单和权限
- 手机号验证后自动从GUEST升级为USER

### DeepSeek AI对话
- AI智能问答
- 对话历史记录（按用户账号隔离）

## 技术栈

| 类别 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.2.4, Spring Web MVC |
| 数据库 | MySQL 8.x, MyBatis-Plus 3.4.0, Druid连接池 |
| 数据库版本 | Liquibase 4.23.2 |
| 认证 | Java JWT 3.3.0 |
| 工具库 | Lombok, FastJSON, Apache Commons Lang3, ip2region |

## 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 8.0+

## 快速开始

### 1. 配置数据库

```sql
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog?useSSL=false&serverTimezone=UTC&useUnicode=true&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. 构建运行

```bash
mvn clean install
mvn spring-boot:run
```

应用启动在 `http://localhost:6101`

## API接口概览

Base URL: `/blog`

### 用户认证
| 接口 | 方法 | 说明 |
|------|------|------|
| `/sys/register` | POST | 用户注册 |
| `/sys/login` | POST | 用户登录 |
| `/sys/getUserList` | GET | 获取用户列表（ADMIN） |
| `/sys/getUserInfoByAccount` | GET | 获取用户信息 |
| `/sys/updateUserInfo` | POST | 更新用户信息 |
| `/user/verifyPhone` | POST | 验证手机号 |
| `/user/getCurrentUser` | GET | 获取当前用户 |

### 文章管理
| 接口 | 方法 | 说明 |
|------|------|------|
| `/article/createOrUpdateArticle` | POST | 创建/更新文章 |
| `/article/listArticle` | GET | 获取文章列表 |
| `/article/getArticleById` | GET | 获取文章详情 |

### 权限管理
| 接口 | 方法 | 说明 |
|------|------|------|
| `/menu/getUserMenus` | GET | 获取当前用户菜单树 |
| `/menu/getAllMenus` | GET | 获取所有菜单（ADMIN） |
| `/menu/add` | POST | 新增菜单（ADMIN） |
| `/menu/update` | POST | 修改菜单（ADMIN） |
| `/menu/delete` | POST | 删除菜单（ADMIN） |
| `/menu/assignToRole` | POST | 为角色分配菜单（ADMIN） |
| `/role/getUserRoles` | GET | 获取当前用户角色 |
| `/role/getAllRoles` | GET | 获取所有角色（ADMIN） |
| `/role/assignToUser` | POST | 为用户分配角色（ADMIN） |
| `/permission/getUserPermissions` | GET | 获取当前用户权限 |
| `/permission/getAllPermissions` | GET | 获取所有权限（ADMIN） |
| `/permission/assignToRole` | POST | 为角色分配权限（ADMIN） |

### DeepSeek AI
| 接口 | 方法 | 说明 |
|------|------|------|
| `/ds/sendCompletion` | POST | 发送AI对话 |
| `/ds/getCompletionList` | GET | 获取对话历史 |

## 请求认证

除登录注册外，所有接口需要在请求头携带Token:
```
token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...
```

## 响应格式

```json
{
  "code": 1,
  "msg": "success",
  "data": {}
}
```

## 数据库迁移

使用Liquibase管理数据库版本：
1. 在 `src/main/resources/liquibase/db/` 下创建变更集XML
2. 在 `master.xml` 中引入变更集
3. 应用启动时自动执行迁移

## 相关文档

- [权限管理API文档](API_DOCUMENT.md)
- [用户管理API文档](SYS_USER_API_DOCUMENT.md)

## License

本项目仅供学习和研究使用。
