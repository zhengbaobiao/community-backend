# 社区服务后台管理系统

基于Spring Boot 2.7 + Vue 3 + TypeScript 的企业级后台管理系统。

## 项目简介

社区服务后台管理系统是面向运营人员和管理员的 Web 管理平台,用于管理社区服务平台的用户、订单、商品、商家等核心业务数据,并提供数据统计分析、内容审核、权限管理等功能。

## 技术栈

### 后端技术
- **核心框架**: Spring Boot 2.7.18
- **安全框架**: Spring Security + JWT
- **ORM框架**: MyBatis-Plus 3.5.3
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.x
- **连接池**: Druid 1.2.16
- **工具库**: Hutool、Fastjson2

### 前端技术
- **核心框架**: Vue 3
- **开发语言**: TypeScript
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **图表库**: ECharts

## 项目结构

```
community-backend/
├── sql/                           # 数据库脚本
│   └── init.sql                  # 初始化脚本
├── src/main/
│   ├── java/com/community/admin/
│   │   ├── CommunityAdminApplication.java  # 启动类
│   │   ├── common/               # 公共类
│   │   │   ├── Result.java       # 统一响应结果
│   │   │   └── ResultCode.java   # 响应码枚举
│   │   ├── config/               # 配置类
│   │   │   ├── JwtProperties.java
│   │   │   ├── MybatisPlusConfig.java
│   │   │   └── RedisConfig.java
│   │   ├── controller/           # 控制器
│   │   │   └── AuthController.java
│   │   ├── dto/                  # 数据传输对象
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/               # 实体类
│   │   │   ├── AdminUser.java
│   │   │   ├── AdminRole.java
│   │   │   └── AdminPermission.java
│   │   ├── exception/            # 异常处理
│   │   │   ├── BusinessException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── mapper/               # 数据访问层
│   │   │   ├── AdminUserMapper.java
│   │   │   ├── AdminRoleMapper.java
│   │   │   └── AdminPermissionMapper.java
│   │   ├── security/             # 安全配置
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── SecurityConfig.java
│   │   ├── service/              # 服务层
│   │   │   ├── AuthService.java
│   │   │   └── impl/
│   │   │       └── AuthServiceImpl.java
│   │   └── util/                 # 工具类
│   │       └── JwtUtil.java
│   └── resources/
│       ├── application.yml       # 配置文件
│       └── mapper/               # MyBatis XML
└── pom.xml                       # Maven配置

```

## 核心功能

### 已实现功能

#### 1. 认证授权模块
- ✅ 用户登录(用户名密码验证)
- ✅ JWT Token生成与验证
- ✅ Token刷新机制
- ✅ 退出登录
- ✅ 角色权限查询
- ✅ Spring Security集成
- ✅ Token黑名单机制

#### 2. 基础架构
- ✅ 统一响应格式封装
- ✅ 全局异常处理
- ✅ MyBatis-Plus配置(分页、逻辑删除)
- ✅ Redis配置
- ✅ Druid数据源配置
- ✅ 跨域配置

#### 3. 数据库设计
- ✅ 管理员表(admin_user)
- ✅ 角色表(admin_role)
- ✅ 权限表(admin_permission)
- ✅ 用户角色关联表(admin_user_role)
- ✅ 角色权限关联表(admin_role_permission)
- ✅ 操作日志表(admin_operation_log)

### 待实现功能

#### 1. 用户管理模块
- 用户列表查询(支持关键词搜索、状态筛选、时间范围、分页)
- 用户详情查看
- 用户状态管理(启用/禁用)

#### 2. 订单管理模块
- 订单列表查询
- 订单详情查看
- 订单状态追踪

#### 3. 系统管理模块
- 管理员管理(增删改查、密码重置、状态管理、角色分配)
- 角色管理(增删改查、权限分配)
- 权限管理(菜单权限配置、动态路由生成)
- 操作日志查询

#### 4. 前端开发
- Vue 3项目初始化
- 登录页面
- 主框架页面
- 各功能模块页面

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.x+
- Node.js 16+ (前端开发)

### 后端启动

1. **创建数据库并初始化**
```bash
# 创建数据库
mysql -u root -p

# 执行初始化脚本
source sql/init.sql
```

2. **修改配置文件**
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/community_admin?...
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

3. **编译运行**
```bash
# 安装依赖
mvn clean install

# 运行项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/community-admin-1.0.0.jar
```

4. **访问地址**
- 应用地址: http://localhost:8080/admin-api
- Druid监控: http://localhost:8080/admin-api/druid (admin/admin123)

### 默认账号

- 用户名: `admin`
- 密码: `admin123`
- 角色: 超级管理员

## API文档

### 认证接口

#### 1. 用户登录
```
POST /admin-api/auth/login
Content-Type: application/json

Request:
{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "超级管理员",
    "avatar": null,
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "roles": ["SUPER_ADMIN"],
    "permissions": ["dashboard", "user:list", ...]
  },
  "timestamp": 1234567890
}
```

#### 2. 获取用户信息
```
GET /admin-api/auth/info
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "admin",
    ...
  }
}
```

#### 3. 刷新Token
```
POST /admin-api/auth/refresh
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "message": "Token刷新成功",
  "data": "new_token_string"
}
```

#### 4. 退出登录
```
POST /admin-api/auth/logout
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "message": "退出登录成功"
}
```

## 安全设计

### 认证机制
- 使用JWT进行无状态认证
- Token有效期2小时,可在1小时内刷新
- Token采用HS512算法签名
- 支持Token黑名单机制(退出登录、Token刷新)

### 授权机制
- 基于RBAC(角色-权限)模型
- 支持菜单权限、按钮权限、接口权限
- 通过Spring Security进行权限验证

### 密码安全
- 使用BCrypt算法加密存储
- 密码强度要求:最少8位,包含大小写字母、数字

### 数据安全
- 逻辑删除(deleted字段)
- SQL注入防护(MyBatis参数绑定)
- XSS防护(输入验证、输出编码)

## 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一异常处理
- 统一响应格式

### 数据库规范
- 表名使用小写+下划线
- 字段命名见名知意
- 必须字段:id、created_at、updated_at、deleted
- 使用索引提升查询性能

### API规范
- RESTful风格
- 统一使用JSON格式
- 请求参数验证
- 响应数据脱敏

## 项目进度

- [x] 项目初始化与基础架构搭建
- [x] 认证授权模块开发
- [ ] 用户管理模块开发
- [ ] 订单管理模块开发
- [ ] 系统管理模块开发
- [ ] 前端项目开发
- [ ] 测试与优化
- [ ] 部署上线

## 许可证

MIT License

## 联系方式

- 项目地址: https://github.com/community/community-backend
- 问题反馈: https://github.com/community/community-backend/issues

---

**Community Team** © 2024
