# 📚 API文档使用指南 - Swagger集成

## ✅ 功能已完成

本项目已成功集成**SpringDoc OpenAPI 3.0 (Swagger)**,提供完整的在线API文档。

---

## 🚀 快速开始

### 1. 启动项目

```bash
mvn spring-boot:run
```

### 2. 访问API文档

浏览器打开: **http://localhost:8080/admin-api/swagger-ui.html**

---

## 📖 Swagger UI功能

### 主要特性

1. **完整的API列表** - 按模块分组展示所有接口
2. **在线测试** - 直接在浏览器中测试API
3. **参数说明** - 详细的请求参数和响应格式说明
4. **JWT认证支持** - 配置Token后自动添加到请求头

### API分组

- 🔐 **认证授权** - 登录、退出、Token管理
- 👤 **管理员管理** - 管理员CRUD操作
- 👥 **角色管理** - 角色和权限分配
- 🔑 **权限管理** - 权限树和列表查询
- 📝 **操作日志** - 日志查询和详情

---

## 🔐 使用JWT认证

### 步骤1: 登录获取Token

1. 在Swagger UI中找到 **认证授权 > POST /auth/login**
2. 点击"Try it out"
3. 输入登录信息:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
4. 点击"Execute"执行
5. 从响应中复制`token`字段的值

### 步骤2: 配置Authorization

1. 点击页面右上角的 **"Authorize"** 按钮
2. 在弹出框中输入: `Bearer {你的token}`
   - 例如: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWI...`
3. 点击"Authorize"确认
4. 关闭弹窗

### 步骤3: 测试需要认证的接口

现在所有请求都会自动携带Token,可以测试任何需要认证的接口。

---

## 📝 API接口分组

### 🔐 认证授权 (4个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/login | 用户登录 |
| GET | /auth/info | 获取用户信息 |
| POST | /auth/refresh | 刷新Token |
| POST | /auth/logout | 退出登录 |

### 👤 管理员管理 (8个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/user/list | 管理员列表 |
| GET | /admin/user/{id} | 管理员详情 |
| POST | /admin/user | 新增管理员 |
| PUT | /admin/user/{id} | 更新管理员 |
| DELETE | /admin/user/{id} | 删除管理员 |
| PUT | /admin/user/{id}/status | 更新状态 |
| PUT | /admin/user/{id}/password | 重置密码 |
| POST | /admin/user/{id}/roles | 分配角色 |

### 👥 角色管理 (7个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/role/list | 角色列表(分页) |
| GET | /admin/role/all | 所有角色 |
| GET | /admin/role/{id} | 角色详情 |
| POST | /admin/role | 新增角色 |
| PUT | /admin/role/{id} | 更新角色 |
| DELETE | /admin/role/{id} | 删除角色 |
| POST | /admin/role/{id}/permissions | 分配权限 |

### 🔑 权限管理 (3个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/permission/tree | 权限树 |
| GET | /admin/permission/list | 权限列表 |
| GET | /admin/permission/role/{roleId} | 角色权限 |

### 📝 操作日志 (2个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /admin/log/list | 日志列表 |
| GET | /admin/log/{id} | 日志详情 |

---

## 🔧 配置说明

### Swagger配置位置

- **配置类**: `src/main/java/com/community/admin/config/SwaggerConfig.java`
- **YML配置**: `src/main/resources/application.yml`

### 自定义配置

在`application.yml`中可以修改Swagger配置:

```yaml
springdoc:
  api-docs:
    enabled: true  # 是否启用API文档
    path: /v3/api-docs  # API文档JSON路径
  swagger-ui:
    enabled: true  # 是否启用Swagger UI
    path: /swagger-ui.html  # Swagger UI访问路径
    tags-sorter: alpha  # 标签排序方式
    operations-sorter: alpha  # 操作排序方式
```

### Security白名单

Swagger相关路径已加入Security白名单,无需认证即可访问:
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`

---

## 📋 使用示例

### 示例1: 测试登录接口

1. 找到 **POST /auth/login**
2. 点击"Try it out"
3. 输入请求体:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
4. 点击"Execute"
5. 查看响应:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGci...",
    "userId": 1,
    "username": "admin",
    "roles": ["超级管理员"],
    "permissions": ["*:*:*"]
  },
  "timestamp": 1699999999999
}
```

### 示例2: 测试管理员列表查询

1. 先按上述步骤配置Authorization
2. 找到 **GET /admin/user/list**
3. 点击"Try it out"
4. 输入查询参数:
   - page: 1
   - size: 10
   - keyword: (可选)
   - status: (可选)
5. 点击"Execute"
6. 查看分页数据响应

---

## 🎯 最佳实践

### 1. API文档维护

- 在Controller类上添加`@Tag`注解描述模块
- 在接口方法上添加`@Operation`注解描述功能
- 在参数上添加`@Parameter`注解说明参数

示例:
```java
@Tag(name = "认证授权", description = "用户登录、退出、Token管理")
@RestController
public class AuthController {
    
    @Operation(summary = "用户登录", description = "管理员登录系统,返回Token和用户信息")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // ...
    }
}
```

### 2. 分组管理

在`application.yml`中配置接口分组:
```yaml
springdoc:
  group-configs:
    - group: '认证授权'
      paths-to-match:
        - /auth/**
    - group: '管理员管理'
      paths-to-match:
        - /admin/user/**
```

### 3. 响应示例

在DTO类上添加示例值:
```java
@Schema(description = "登录请求")
public class LoginRequest {
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    @Schema(description = "密码", example = "admin123")
    private String password;
}
```

---

## 🛡️ 生产环境配置

### 禁用Swagger

在生产环境中,建议禁用Swagger以提高安全性:

```yaml
# application-prod.yml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

或者通过环境变量:
```bash
java -jar app.jar --springdoc.api-docs.enabled=false
```

---

## 📚 相关资源

- **SpringDoc官网**: https://springdoc.org/
- **OpenAPI规范**: https://swagger.io/specification/
- **在线API文档**: http://localhost:8080/admin-api/swagger-ui.html

---

## ✅ 集成清单

- [x] 添加SpringDoc依赖
- [x] 创建Swagger配置类
- [x] 配置Security白名单
- [x] 添加application.yml配置
- [x] AuthController添加Swagger注解
- [x] API分组配置
- [x] JWT认证支持

---

**集成时间**: 2024  
**版本**: SpringDoc OpenAPI 1.7.0  
**状态**: ✅ 已完成并可用
