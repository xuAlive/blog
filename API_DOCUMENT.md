# 权限管理系统 API 接口文档

## 1. 概述

### 1.1 基本信息
- **Base URL**: `http://your-domain/blog`
- **请求方式**: RESTful API
- **数据格式**: JSON
- **字符编码**: UTF-8

### 1.2 公共响应结构
```json
{
  "code": 1,
  "msg": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码（200-成功，其他-失败） |
| msg | String | 响应消息 |
| data | Object | 响应数据 |

### 1.3 公共请求头
```
Content-Type: application/json
token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...##A0CA30783FF8BA208BADF3372D815EBF
```

### 1.4 角色权限说明
| 角色编码 | 角色名称 | 权限说明 |
|---------|---------|---------|
| ADMIN | 管理员 | 拥有所有菜单和功能操作权限 |
| USER | 用户 | 已绑定手机号，拥有部分功能权限 |
| GUEST | 访客 | 未绑定手机号，仅查看权限 |

---

## 2. 菜单管理接口

### 2.1 获取当前用户菜单树

**接口地址**: `/menu/getUserMenus`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 获取当前登录用户有权限访问的菜单树形结构

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "menuName": "系统管理",
      "menuCode": "system",
      "menuType": 1,
      "path": "/system",
      "component": "Layout",
      "icon": "system",
      "sortOrder": 1,
      "visible": 1,
      "status": 1,
      "permission": "",
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "menuName": "用户管理",
          "menuCode": "user",
          "menuType": 2,
          "path": "/system/user",
          "component": "system/user/index",
          "icon": "user",
          "sortOrder": 1,
          "visible": 1,
          "status": 1,
          "permission": "system:user:list",
          "children": []
        }
      ]
    }
  ]
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 菜单ID |
| parentId | Integer | 父菜单ID（0表示顶级菜单） |
| menuName | String | 菜单名称 |
| menuCode | String | 菜单编码 |
| menuType | Integer | 菜单类型（1-目录 2-菜单 3-按钮） |
| path | String | 路由地址 |
| component | String | 组件路径 |
| icon | String | 菜单图标 |
| sortOrder | Integer | 排序号 |
| visible | Integer | 是否可见（0-隐藏 1-显示） |
| status | Integer | 状态（0-禁用 1-启用） |
| permission | String | 权限标识 |
| children | Array | 子菜单列表 |

---

### 2.2 获取所有菜单树

**接口地址**: `/menu/getAllMenus`
**请求方式**: `GET`
**权限要求**: `ADMIN`
**接口描述**: 获取系统中所有启用的菜单树形结构（管理员用）

#### 请求参数
无

#### 响应参数
同 2.1

---

### 2.3 获取菜单列表（平铺）

**接口地址**: `/menu/getMenuList`
**请求方式**: `GET`
**权限要求**: `ADMIN`
**接口描述**: 获取所有菜单的平铺列表，用于分配权限时选择

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "menuName": "系统管理",
      "menuCode": "system",
      "parentId": 0,
      "menuType": 1
    }
  ]
}
```

---

### 2.4 新增菜单

**接口地址**: `/menu/add`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 创建新的菜单项

#### 请求参数
```json
{
  "parentId": 0,
  "menuName": "系统管理",
  "menuCode": "system",
  "menuType": 1,
  "path": "/system",
  "component": "Layout",
  "icon": "system",
  "sortOrder": 1,
  "visible": 1,
  "status": 1,
  "permission": ""
}
```

#### 请求字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| parentId | Integer | 是 | 父菜单ID（0表示顶级） |
| menuName | String | 是 | 菜单名称 |
| menuCode | String | 是 | 菜单编码（唯一） |
| menuType | Integer | 是 | 菜单类型（1-目录 2-菜单 3-按钮） |
| path | String | 否 | 路由地址 |
| component | String | 否 | 组件路径 |
| icon | String | 否 | 菜单图标 |
| sortOrder | Integer | 否 | 排序号（默认0） |
| visible | Integer | 否 | 是否可见（默认1） |
| status | Integer | 否 | 状态（默认1） |
| permission | String | 否 | 权限标识 |

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 2.5 修改菜单

**接口地址**: `/menu/update`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 更新已有菜单信息

#### 请求参数
```json
{
  "id": 1,
  "parentId": 0,
  "menuName": "系统管理",
  "menuCode": "system",
  "menuType": 1,
  "path": "/system",
  "component": "Layout",
  "icon": "system",
  "sortOrder": 1,
  "visible": 1,
  "status": 1,
  "permission": ""
}
```

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 2.6 删除菜单

**接口地址**: `/menu/delete`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 逻辑删除菜单（不会真正删除数据）

#### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| menuId | Integer | 是 | 菜单ID |

**示例**: `/menu/delete?menuId=1`

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 2.7 为角色分配菜单

**接口地址**: `/menu/assignToRole`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 为指定角色分配菜单权限

#### 请求参数
**Query参数**: `roleId=1`

**Body参数**:
```json
[1, 2, 3, 4, 5]
```

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

## 3. 角色管理接口

### 3.1 获取当前用户角色列表

**接口地址**: `/role/getUserRoles`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 获取当前登录用户的角色列表

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "roleCode": "USER",
      "roleName": "用户",
      "description": "普通用户，已绑定手机号，拥有部分功能权限",
      "status": 1
    }
  ]
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 角色ID |
| roleCode | String | 角色编码（ADMIN/USER/GUEST） |
| roleName | String | 角色名称 |
| description | String | 角色描述 |
| status | Integer | 状态（0-禁用 1-启用） |

---

### 3.2 获取当前用户角色编码

**接口地址**: `/role/getUserRoleCode`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 获取当前用户的角色编码（如果有多个角色，返回权限最高的）

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": "USER"
}
```

---

### 3.3 获取所有角色列表

**接口地址**: `/role/getAllRoles`
**请求方式**: `GET`
**权限要求**: `ADMIN`
**接口描述**: 获取系统中所有角色

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "roleCode": "ADMIN",
      "roleName": "管理员",
      "description": "系统管理员，拥有所有权限",
      "status": 1
    },
    {
      "id": 2,
      "roleCode": "USER",
      "roleName": "用户",
      "description": "普通用户，已绑定手机号，拥有部分功能权限",
      "status": 1
    },
    {
      "id": 3,
      "roleCode": "GUEST",
      "roleName": "访客",
      "description": "访客用户，未绑定手机号，仅查看权限",
      "status": 1
    }
  ]
}
```

---

### 3.4 为用户分配角色

**接口地址**: `/role/assignToUser`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 为指定用户分配角色

#### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| account | String | 是 | 用户账号 |
| roleId | Integer | 是 | 角色ID |

**示例**: `/role/assignToUser?account=testuser&roleId=2`

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 3.5 新增角色

**接口地址**: `/role/add`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 创建新的角色

#### 请求参数
```json
{
  "roleCode": "VIP",
  "roleName": "VIP用户",
  "description": "VIP用户，拥有特殊权限",
  "status": 1
}
```

#### 请求字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleCode | String | 是 | 角色编码（唯一） |
| roleName | String | 是 | 角色名称 |
| description | String | 否 | 角色描述 |
| status | Integer | 否 | 状态（默认1） |

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 3.6 修改角色

**接口地址**: `/role/update`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 更新已有角色信息

#### 请求参数
```json
{
  "id": 4,
  "roleCode": "VIP",
  "roleName": "高级VIP用户",
  "description": "高级VIP用户，拥有特殊权限",
  "status": 1
}
```

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

## 4. 权限管理接口

### 4.1 获取当前用户权限列表

**接口地址**: `/permission/getUserPermissions`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 获取当前登录用户的所有权限

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "permissionCode": "system:user:list",
      "permissionName": "查看用户列表",
      "resourceType": "API",
      "resourcePath": "/blog/sys/getUserList",
      "description": "查看用户列表权限",
      "status": 1
    }
  ]
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 权限ID |
| permissionCode | String | 权限编码 |
| permissionName | String | 权限名称 |
| resourceType | String | 资源类型（API/BUTTON/DATA） |
| resourcePath | String | 资源路径 |
| description | String | 权限描述 |
| status | Integer | 状态（0-禁用 1-启用） |

---

### 4.2 获取所有权限列表

**接口地址**: `/permission/getAllPermissions`
**请求方式**: `GET`
**权限要求**: `ADMIN`
**接口描述**: 获取系统中所有权限

#### 请求参数
无

#### 响应参数
同 4.1

---

### 4.3 新增权限

**接口地址**: `/permission/add`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 创建新的权限

#### 请求参数
```json
{
  "permissionCode": "system:user:add",
  "permissionName": "新增用户",
  "resourceType": "API",
  "resourcePath": "/blog/sys/addUser",
  "description": "新增用户权限",
  "status": 1
}
```

#### 请求字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| permissionCode | String | 是 | 权限编码（唯一） |
| permissionName | String | 是 | 权限名称 |
| resourceType | String | 是 | 资源类型（API/BUTTON/DATA） |
| resourcePath | String | 否 | 资源路径 |
| description | String | 否 | 权限描述 |
| status | Integer | 否 | 状态（默认1） |

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 4.4 修改权限

**接口地址**: `/permission/update`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 更新已有权限信息

#### 请求参数
```json
{
  "id": 1,
  "permissionCode": "system:user:add",
  "permissionName": "新增用户",
  "resourceType": "API",
  "resourcePath": "/blog/sys/addUser",
  "description": "新增用户权限",
  "status": 1
}
```

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 4.5 为角色分配权限

**接口地址**: `/permission/assignToRole`
**请求方式**: `POST`
**权限要求**: `ADMIN`
**接口描述**: 为指定角色分配权限

#### 请求参数
**Query参数**: `roleId=2`

**Body参数**:
```json
[1, 2, 3, 4, 5]
```

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

## 5. 用户管理接口

### 5.1 验证手机号

**接口地址**: `/user/verifyPhone`
**请求方式**: `POST`
**权限要求**: 登录用户
**接口描述**: 验证手机号，将用户从GUEST升级为USER

#### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | String | 是 | 手机号 |
| verifyCode | String | 是 | 验证码 |

**示例**: `/user/verifyPhone?phone=13800138000&verifyCode=123456`

#### 响应参数
```json
{
  "code": 200,
  "msg": "手机号验证成功，已升级为正式用户",
  "data": null
}
```

---

### 5.2 获取当前用户信息

**接口地址**: `/user/getCurrentUser`
**请求方式**: `GET`
**权限要求**: 登录用户
**接口描述**: 获取当前登录用户的详细信息

#### 请求参数
无

#### 响应参数
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "account": "testuser",
    "phone": "13800138000",
    "phoneVerified": 1,
    "createTime": "2025-01-15 10:00:00",
    "updateTime": "2025-01-15 10:30:00"
  }
}
```

#### 响应字段说明
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 用户ID |
| account | String | 用户账号 |
| phone | String | 手机号 |
| phoneVerified | Integer | 手机号验证状态（0-未验证 1-已验证） |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

---

## 6. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未登录 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 7. 使用示例

### 7.1 用户注册并升级流程

```bash
# 1. 注册账号（自动分配GUEST角色）
POST /blog/sys/register
{
  "account": "newuser",
  "password": "123456"
}

# 2. 登录获取token
POST /blog/sys/login
{
  "account": "newuser",
  "password": "123456"
}

# 3. 验证手机号（升级为USER角色）
POST /blog/user/verifyPhone?phone=13800138000&verifyCode=123456
Header: token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...
```

### 7.2 管理员配置菜单权限

```bash
# 1. 创建菜单
POST /blog/menu/add
Header: token: admin_token
{
  "menuName": "用户管理",
  "menuCode": "user_manage",
  "menuType": 2,
  "parentId": 1
}

# 2. 为USER角色分配菜单
POST /blog/menu/assignToRole?roleId=2
Header: token: admin_token
[1, 2, 3, 4]

# 3. 创建权限
POST /blog/permission/add
Header: token: admin_token
{
  "permissionCode": "user:add",
  "permissionName": "新增用户",
  "resourceType": "API"
}

# 4. 为USER角色分配权限
POST /blog/permission/assignToRole?roleId=2
Header: token: admin_token
[1, 2, 3]
```

---

## 8. 注意事项

1. **所有接口（除登录注册外）都需要在请求头中携带token**
2. **带有权限要求的接口，如果权限不足会返回403错误**
3. **菜单树结构的层级理论上不限制，但建议不超过3层**
4. **角色编码（ADMIN/USER/GUEST）是预置的，不建议修改**
5. **手机号验证接口中的验证码逻辑需要根据实际情况实现**
6. **删除菜单和角色都是逻辑删除，不会真正删除数据**

---

## 9. 变更历史

| 版本 | 日期 | 修改内容 |
|------|------|---------|
| v1.0 | 2025-01-15 | 初始版本，包含基础权限管理功能 |
