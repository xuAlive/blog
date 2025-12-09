# 系统用户管理 API 接口文档

## 1. 获取用户列表

**接口地址**: `/sys/getUserList`
**请求方式**: `GET`
**权限要求**: `ADMIN`
**接口描述**: 获取系统所有用户列表，包含用户基本信息和详细信息

### 1.1 请求参数
无

### 1.2 请求示例
```bash
GET /blog/sys/getUserList
Header: token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...
```

### 1.3 响应参数
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "account": "admin",
      "userName": "管理员",
      "name": "超级管理员",
      "email": "admin@example.com",
      "phone": "13800138000",
      "isDelete": 0,
      "createTime": "2024-01-15 10:00:00",
      "updateTime": "2024-01-20 15:30:00"
    },
    {
      "account": "testuser",
      "userName": "张三",
      "name": "小张",
      "email": "zhangsan@example.com",
      "phone": "13900139000",
      "isDelete": 0,
      "createTime": "2024-01-16 09:00:00",
      "updateTime": "2024-01-18 14:20:00"
    }
  ]
}
```

### 1.4 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| account | String | 用户账号 |
| userName | String | 真实姓名（来自sys_user_info） |
| name | String | 昵称（来自sys_user_info） |
| email | String | 邮箱（来自sys_user_info） |
| phone | String | 手机号（来自sys_user） |
| isDelete | Integer | 状态（0-正常 1-已删除） |
| createTime | String | 创建时间 |
| updateTime | String | 最后更新时间 |

### 1.5 数据查询说明
- 查询SQL关联了 `sys_user` 和 `sys_user_info` 两张表
- 通过 `account` 字段进行关联
- 只返回未删除的用户（is_delete = 0）
- 按创建时间倒序排列

---

## 2. 根据账号获取用户信息

**接口地址**: `/sys/getUserInfoByAccount`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 根据账号获取指定用户的详细信息

### 2.1 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| account | String | 否 | 用户账号（不传则获取当前登录用户信息） |

### 2.2 请求示例
```bash
GET /blog/sys/getUserInfoByAccount?account=testuser
Header: token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...
```

### 2.3 响应参数
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "account": "testuser",
    "phone": "13900139000",
    "name": "小张",
    "userName": "张三",
    "birthday": "1990-01-15",
    "age": 34,
    "sex": 1,
    "email": "zhangsan@example.com",
    "idCard": "110101199001011234",
    "intro": "这是我的个人简介"
  }
}
```

### 2.4 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| account | String | 用户账号 |
| phone | String | 手机号 |
| name | String | 昵称 |
| userName | String | 真实姓名 |
| birthday | String | 出生日期 |
| age | Integer | 年龄 |
| sex | Integer | 性别（0-女 1-男） |
| email | String | 邮箱 |
| idCard | String | 身份证号 |
| intro | String | 个人简介 |

### 2.5 特殊说明
- 如果用户信息不存在，会创建一个空的用户信息对象，只包含账号字段
- 如果不传account参数，返回当前登录用户的信息

---

## 3. 更新用户信息

**接口地址**: `/sys/updateUserInfo`
**请求方式**: `POST`
**权限要求**: 登录用户
**接口描述**: 更新用户的详细信息

### 3.1 请求参数
```json
{
  "account": "testuser",
  "phone": "13900139000",
  "name": "小张",
  "userName": "张三",
  "birthday": "1990-01-15",
  "age": 34,
  "sex": 1,
  "email": "zhangsan@example.com",
  "idCard": "110101199001011234",
  "intro": "这是我的更新后的个人简介"
}
```

### 3.2 请求字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| account | String | 是 | 用户账号 |
| phone | String | 否 | 手机号 |
| name | String | 否 | 昵称 |
| userName | String | 否 | 真实姓名 |
| birthday | String | 否 | 出生日期（格式：yyyy-MM-dd） |
| age | Integer | 否 | 年龄 |
| sex | Integer | 否 | 性别（0-女 1-男） |
| email | String | 否 | 邮箱 |
| idCard | String | 否 | 身份证号 |
| intro | String | 否 | 个人简介 |

### 3.3 请求示例
```bash
POST /blog/sys/updateUserInfo
Header: token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...
Content-Type: application/json

{
  "account": "testuser",
  "name": "小张",
  "userName": "张三",
  "email": "zhangsan@example.com",
  "intro": "这是我的更新后的个人简介"
}
```

### 3.4 响应参数
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

### 3.5 特殊说明
- 更新时只更新传入的字段，未传入的字段保持不变
- account字段必传，用于定位要更新的用户

---

## 4. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1 | 请求成功 |
| 0 | 请求失败 |
| 401 | 未登录 |
| 403 | 权限不足 |

### 4.1 常见错误响应示例

#### 未登录
```json
{
  "code": 401,
  "msg": "未登录"
}
```

#### 权限不足
```json
{
  "code": 403,
  "msg": "权限不足，需要ADMIN角色"
}
```

#### 参数错误
```json
{
  "code": 0,
  "msg": "账号不能为空"
}
```

---

## 5. 使用示例

### 5.1 管理员获取用户列表
```bash
# 请求
curl -X GET "http://localhost:8080/blog/sys/getUserList" \
  -H "token: admin_token_here"

# 响应
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "account": "admin",
      "userName": "管理员",
      "name": "超级管理员",
      "email": "admin@example.com",
      "phone": "13800138000",
      "isDelete": 0,
      "createTime": "2024-01-15 10:00:00",
      "updateTime": "2024-01-20 15:30:00"
    }
  ]
}
```

### 5.2 获取指定用户信息
```bash
# 请求
curl -X GET "http://localhost:8080/blog/sys/getUserInfoByAccount?account=testuser" \
  -H "token: user_token_here"

# 响应
{
  "code": 1,
  "msg": "success",
  "data": {
    "account": "testuser",
    "name": "小张",
    "userName": "张三",
    "email": "zhangsan@example.com"
  }
}
```

### 5.3 更新用户信息
```bash
# 请求
curl -X POST "http://localhost:8080/blog/sys/updateUserInfo" \
  -H "Content-Type: application/json" \
  -H "token: user_token_here" \
  -d '{
    "account": "testuser",
    "name": "新昵称",
    "intro": "更新后的简介"
  }'

# 响应
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

## 6. 注意事项

1. **权限控制**
   - `/getUserList` 接口只有ADMIN角色可以访问
   - `/getUserInfoByAccount` 和 `/updateUserInfo` 所有登录用户都可以访问

2. **数据关联**
   - 用户列表接口通过LEFT JOIN关联sys_user和sys_user_info表
   - 如果用户没有填写详细信息，相关字段会返回null

3. **数据安全**
   - 密码字段不会在任何接口中返回
   - 建议在前端对身份证、手机号等敏感信息进行脱敏显示

4. **性能建议**
   - 如果用户量较大，建议在用户列表接口添加分页功能
   - 可以添加查询条件（如按账号、姓名搜索）来优化查询

---

## 7. 数据库表结构

### 7.1 sys_user 表（用户基本信息）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键ID |
| account | VARCHAR(50) | 账号 |
| phone | VARCHAR(20) | 手机号 |
| password | VARCHAR(100) | 密码 |
| phone_verified | TINYINT | 手机号验证状态 |
| is_delete | TINYINT | 删除标识 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 7.2 sys_user_info 表（用户详细信息）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键ID |
| account | VARCHAR(50) | 账号（关联sys_user） |
| phone | VARCHAR(20) | 手机号 |
| name | VARCHAR(50) | 昵称 |
| user_name | VARCHAR(50) | 真实姓名 |
| birthday | DATE | 出生日期 |
| age | INT | 年龄 |
| sex | TINYINT | 性别 |
| email | VARCHAR(100) | 邮箱 |
| id_card | VARCHAR(20) | 身份证 |
| intro | VARCHAR(500) | 简介 |
| is_delete | TINYINT | 删除标识 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

---

## 8. 变更历史

| 版本 | 日期 | 修改内容 |
|------|------|---------|
| v1.0 | 2025-01-15 | 初始版本 |
| v1.1 | 2025-01-20 | 新增getUserList接口 |
