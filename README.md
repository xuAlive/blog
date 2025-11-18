# Blog System

一个基于Spring Boot的现代化博客系统，集成了DeepSeek AI对话功能。

## 项目简介

本项目是一个功能完整的博客管理系统，提供文章管理、用户认证和AI对话等功能。采用前后端分离架构，使用主流技术栈开发。

## 主要功能

- **文章管理**
  - 创建/更新文章
  - 文章列表查询
  - 文章详情查看

- **用户系统**
  - 用户注册/登录
  - JWT Token认证
  - 用户信息管理

- **AI对话功能**
  - 集成DeepSeek AI
  - 对话历史记录
  - 智能问答

## 技术栈

### 后端框架
- Spring Boot 2.2.4
- Spring Web MVC

### 数据库相关
- MySQL 8.x
- MyBatis-Plus 3.4.0
- Druid 数据库连接池
- Liquibase 数据库版本管理

### 工具库
- Lombok - 简化Java代码
- FastJSON - JSON处理
- Apache Commons Lang3 - 工具类库
- Java JWT - Token认证

## 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 8.0+

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd blog
```

### 2. 配置数据库

创建MySQL数据库：

```sql
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.properties` 中的数据库配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog?useSSL=false&serverTimezone=UTC&useUnicode=true&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 构建项目

```bash
mvn clean install
```

### 4. 运行应用

```bash
mvn spring-boot:run
```

或者运行打包后的jar文件：

```bash
java -jar target/blog-1.0-SNAPSHOT.jar
```

应用将在 `http://localhost:6101` 启动

## API接口

### 文章管理

- `POST /blog/article/createOrUpdateArticle` - 创建或更新文章
- `GET /blog/article/listArticle` - 获取文章列表
- `GET /blog/article/getArticleById` - 根据ID获取文章详情

### DeepSeek AI

- `POST /blog/ds/sendCompletion` - 发送AI对话请求
- `GET /blog/ds/getCompletionList` - 获取对话历史

### 用户系统

- 用户注册、登录等接口（详见SysController）

## 项目结构

```
blog/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/xu/blog/
│   │   │       ├── ai/              # AI相关功能
│   │   │       ├── config/          # 配置类
│   │   │       ├── controller/      # 控制器
│   │   │       ├── dao/             # 数据访问层
│   │   │       ├── domain/          # 领域模型
│   │   │       ├── enums/           # 枚举类
│   │   │       ├── mapper/          # MyBatis映射器
│   │   │       ├── param/           # 参数对象
│   │   │       ├── service/         # 服务层
│   │   │       └── utils/           # 工具类
│   │   └── resources/
│   │       ├── application.properties  # 应用配置
│   │       └── liquibase/              # 数据库版本管理
│   └── test/                        # 测试代码
├── pom.xml                          # Maven配置
└── README.md                        # 项目说明
```

## 配置说明

### 应用配置

主要配置项在 `application.properties` 中：

- `server.port`: 服务端口，默认6101
- `spring.datasource.*`: 数据库连接配置
- `mybatis.*`: MyBatis配置
- `logging.level.*`: 日志级别配置

### 数据库连接池

使用Druid连接池，默认配置：
- 初始连接数: 5
- 最小空闲连接: 10
- 最大活动连接: 200

## 开发指南

### 添加新的API接口

1. 在 `controller` 包下创建控制器
2. 在 `service` 包下创建服务接口和实现
3. 在 `mapper` 包下创建MyBatis映射器
4. 在 `domain` 包下创建领域模型

### 数据库迁移

使用Liquibase管理数据库版本：

1. 在 `resources/liquibase` 目录下创建变更集
2. 在 `master.xml` 中引入变更集
3. 应用启动时自动执行迁移

## 常见问题

### Q: 数据库连接失败？
A: 检查MySQL是否启动，数据库配置是否正确，确保数据库已创建。

### Q: 启动时报Liquibase错误？
A: 确保数据库用户有足够的权限创建表和执行DDL语句。

### Q: JWT Token验证失败？
A: 检查Token是否过期，请求头是否正确携带Token。

## License

本项目仅供学习和研究使用。

## 联系方式

如有问题或建议，欢迎提Issue。