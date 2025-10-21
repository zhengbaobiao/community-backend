我来创建一个详细的设计方案文档。
markdown
# 社区服务平台后台管理系统设计方案

## 目录
- [一、系统架构设计](#一系统架构设计)
- [二、功能模块设计](#二功能模块设计)
- [三、技术栈选型](#三技术栈选型)
- [四、数据库设计](#四数据库设计)
- [五、后端核心代码实现](#五后端核心代码实现)
- [六、前端核心代码实现](#六前端核心代码实现)
- [七、权限控制方案](#七权限控制方案)
- [八、部署方案](#八部署方案)
- [九、开发规范](#九开发规范)
- [十、项目实施计划](#十项目实施计划)

---

## 一、系统架构设计

### 1.1 总体架构
┌─────────────────┐
│ 管理员浏览器 │
└────────┬────────┘
│
▼
┌─────────────────┐
│ Admin前端 │
│ Vue3 + Element │
└────────┬────────┘
│
▼
┌─────────────────┐
│ API网关 │
└────────┬────────┘
│
▼
┌─────────────────┐
│ 后端服务 │
│ Spring Boot │
└────┬────────┬───┘
│ │
▼ ▼
┌────────┐ ┌────────┐
│ MySQL │ │ Redis │
└────────┘ └────────┘

### 1.2 系统分层

#### 表现层（Presentation Layer）
- **技术**: Vue 3 + TypeScript + Element Plus
- **职责**: 用户交互界面、数据展示、表单验证

#### 网关层（Gateway Layer）
- **技术**: Nginx反向代理
- **职责**: 请求路由、负载均衡、SSL终止

#### 业务层（Business Layer）
- **技术**: Spring Boot + Spring Security
- **职责**: 业务逻辑处理、权限控制、数据验证

#### 数据层（Data Layer）
- **技术**: MySQL 8.0 + Redis 6.x
- **职责**: 数据持久化、缓存加速

---

## 二、功能模块设计

### 2.1 用户管理模块

#### 功能列表
- ✅ 用户列表查询（支持多条件筛选、搜索、分页）
- ✅ 用户详情查看（基本信息、实名信息、积分记录）
- ✅ 用户状态管理（启用/禁用/删除）
- ✅ 用户等级管理（会员等级调整）
- ✅ 积分记录查询（积分明细、兑换记录）
- ✅ 用户标签管理（标签添加、移除）

#### 业务流程
管理员登录 → 进入用户管理 → 搜索/筛选用户 → 查看详情 → 执行操作 → 记录日志

### 2.2 订单管理模块

#### 功能列表
- ✅ 社区服务订单列表（全部订单、待接单、进行中、已完成）
- ✅ 订单详情查看（订单信息、服务信息、支付信息）
- ✅ 订单状态追踪（状态流转历史）
- ✅ 纠纷处理（纠纷工单、仲裁决策）
- ✅ 退款管理（退款申请、审核、执行）
- ✅ 订单导出（Excel导出、数据统计）

#### 订单状态流转
待支付 → 待接单 → 已接单 → 服务中 → 已完成 → 已评价
↓
已取消/已退款

### 2.3 商品管理模块

#### 功能列表
- ✅ 闲置商品列表（全部商品、在售、已售、已下架）
- ✅ 商品详情查看（商品信息、卖家信息、浏览记录）
- ✅ 商品上下架（批量上架、批量下架）
- ✅ 违规商品处理（标记违规、强制下架、处罚卖家）
- ✅ 商品分类管理（分类增删改、排序）
- ✅ 商品审核（新发布商品审核）

### 2.4 商家管理模块

#### 功能列表
- ✅ 商家申请审核（待审核列表、审核操作）
- ✅ 商家资质管理（资质查看、资质变更）
- ✅ 商家店铺管理（店铺信息、店铺状态）
- ✅ 商家产品管理（商家商品列表、商品审核）
- ✅ 商家评分管理（服务评分、信用评分）
- ✅ 商家保证金管理（保证金缴纳、退还）

#### 商家审核流程
提交申请 → 资料初审 → 资质验证 → 实地考察（可选） → 审核通过/拒绝 → 通知商家

### 2.5 内容审核模块

#### 功能列表
- ✅ 待审核内容列表（订单内容、商品内容、评论内容）
- ✅ 图片内容审核（鉴黄、暴恐、政治敏感）
- ✅ 文本内容审核（敏感词、违禁词检测）
- ✅ 违规内容处理（删除、屏蔽、警告、封禁）
- ✅ 审核规则配置（敏感词库、审核策略）
- ✅ 审核记录查询（审核历史、处理结果）

### 2.6 数据统计模块

#### 功能列表
- ✅ 用户增长趋势（日/周/月新增用户）
- ✅ 订单统计分析（订单量、订单金额、完成率）
- ✅ 交易额统计（日交易额、月交易额、年度对比）
- ✅ 活跃度分析（DAU、MAU、留存率）
- ✅ 商品统计（商品发布量、成交量、成交率）
- ✅ 商家统计（商家数量、商家GMV排行）

#### 数据报表
运营日报（每日核心指标）
运营周报（周度数据对比）
运营月报（月度数据总结）
自定义报表（灵活配置指标）

### 2.7 系统管理模块

#### 功能列表
- ✅ 管理员账号管理（账号增删改查、密码重置）
- ✅ 角色权限管理（角色配置、权限分配）
- ✅ 菜单管理（菜单配置、动态路由）
- ✅ 操作日志查询（登录日志、操作日志）
- ✅ 系统配置（系统参数、业务参数）
- ✅ 数据字典（字典管理、字典项管理）

---

## 三、技术栈选型

### 3.1 前端技术栈

| 技术类型 | 技术选型 | 版本 | 说明 |
|---------|---------|------|------|
| 框架 | Vue 3 | 3.3+ | 组合式API、TypeScript支持 |
| 语言 | TypeScript | 5.0+ | 类型安全、代码提示 |
| UI组件库 | Element Plus | 2.4+ | 企业级UI组件库 |
| 状态管理 | Pinia | 2.1+ | Vue 3官方推荐 |
| 路由 | Vue Router | 4.2+ | 路由管理、权限控制 |
| HTTP客户端 | Axios | 1.5+ | Promise API、拦截器 |
| 构建工具 | Vite | 4.5+ | 快速构建、HMR |
| 图表库 | ECharts | 5.4+ | 数据可视化 |
| 富文本编辑 | Quill | 1.3+ | 富文本编辑器 |
| CSS预处理 | SCSS | 1.68+ | CSS增强 |
| 代码规范 | ESLint + Prettier | - | 代码风格统一 |

### 3.2 后端技术栈

| 技术类型 | 技术选型 | 版本 | 说明 |
|---------|---------|------|------|
| 框架 | Spring Boot | 2.7.18 | 快速开发、自动配置 |
| 语言 | Java | 1.8 | 稳定可靠 |
| 权限控制 | Spring Security | 5.7+ | 安全框架 |
| 认证方式 | JWT | - | 无状态认证 |
| ORM框架 | MyBatis-Plus | 3.5+ | 增强MyBatis |
| 数据库 | MySQL | 8.0 | 关系型数据库 |
| 缓存 | Redis | 6.x | 内存数据库 |
| 连接池 | Druid | 1.2+ | 数据库连接池 |
| API文档 | Swagger/OpenAPI | 3.0 | 接口文档自动生成 |
| 日志框架 | SLF4J + Logback | - | 日志记录 |
| 工具类 | Hutool | 5.8+ | Java工具集 |
| JSON处理 | FastJson | 2.0+ | JSON序列化 |
| 参数验证 | Hibernate Validator | - | Bean验证 |

### 3.3 开发工具

| 工具类型 | 工具名称 | 说明 |
|---------|---------|------|
| IDE | IntelliJ IDEA | 后端开发 |
| IDE | VS Code | 前端开发 |
| API测试 | Postman | 接口测试 |
| 数据库工具 | Navicat | 数据库管理 |
| 版本控制 | Git | 代码版本管理 |
| 项目管理 | Maven | 依赖管理 |

---

## 四、数据库设计

### 4.1 管理员表（admin_user）

```sql
CREATE TABLE `admin_user` (
  [id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19) BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  [username](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\User.java#L32-L32) VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `email` VARCHAR(100) COMMENT '邮箱',
  [phone](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\User.java#L82-L82) VARCHAR(20) COMMENT '手机号',
  [avatar](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\User.java#L57-L57) VARCHAR(500) COMMENT '头像URL',
  [status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67) TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  [deleted](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L38-L40) TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除 1-已删除',
  PRIMARY KEY ([id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19)),
  UNIQUE KEY `uk_username` ([username](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\User.java#L32-L32)),
  KEY `idx_status` ([status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67)),
  KEY `idx_deleted` ([deleted](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L38-L40))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
字段说明：
id: 主键，自增
username: 登录用户名，唯一
password: 密码，使用BCrypt加密
status: 账号状态，用于禁用账号
deleted: 软删除标记
4.2 角色表（admin_role）
sql
CREATE TABLE `admin_role` (
  [id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19) BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  [description](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L32-L32) VARCHAR(200) COMMENT '角色描述',
  [status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67) TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `sort_order` INT DEFAULT 0 COMMENT '排序号',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  [deleted](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L38-L40) TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY ([id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19)),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` ([status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 初始化角色数据
INSERT INTO `admin_role` (`role_name`, `role_code`, [description](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L32-L32), `sort_order`) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1),
('系统管理员', 'SYSTEM_ADMIN', '系统配置和用户管理', 2),
('运营管理员', 'OPERATION_ADMIN', '业务运营和内容审核', 3),
('客服人员', 'CUSTOMER_SERVICE', '客户服务和订单处理', 4);
4.3 权限表（admin_permission）
sql
CREATE TABLE `admin_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
  `permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型：menu-菜单 button-按钮 api-接口',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
  `path` VARCHAR(200) COMMENT '路由路径',
  `component` VARCHAR(200) COMMENT '组件路径',
  `icon` VARCHAR(100) COMMENT '图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type` (`permission_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 初始化权限数据
INSERT INTO `admin_permission` (`permission_name`, `permission_code`, `permission_type`, `parent_id`, `path`, `icon`, `sort_order`) VALUES
-- 一级菜单
('仪表盘', 'dashboard', 'menu', 0, '/dashboard', 'dashboard', 1),
('用户管理', 'user', 'menu', 0, '/user', 'user', 2),
('订单管理', 'order', 'menu', 0, '/order', 'shopping-cart', 3),
('商品管理', 'item', 'menu', 0, '/item', 'goods', 4),
('商家管理', 'merchant', 'menu', 0, '/merchant', 'shop', 5),
('内容审核', 'audit', 'menu', 0, '/audit', 'document-checked', 6),
('数据统计', 'statistics', 'menu', 0, '/statistics', 'data-line', 7),
('系统管理', 'system', 'menu', 0, '/system', 'setting', 8);
4.4 用户角色关联表（admin_user_role）
sql
CREATE TABLE `admin_user_role` (
  [id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19) BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '管理员ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY ([id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19)),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
4.5 角色权限关联表（admin_role_permission）
sql
CREATE TABLE `admin_role_permission` (
  [id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19) BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY ([id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19)),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
4.6 操作日志表（admin_operation_log）
sql
CREATE TABLE `admin_operation_log` (
  [id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19) BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` BIGINT COMMENT '操作人ID',
  [username](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\User.java#L32-L32) VARCHAR(50) COMMENT '操作人用户名',
  `operation` VARCHAR(100) COMMENT '操作类型',
  `method` VARCHAR(200) COMMENT '请求方法',
  `params` TEXT COMMENT '请求参数',
  [result](file://d:\workspace\qoder\社区服务\miniprogram\miniprogram_npm\@vant\lib\datetime-picker\index.js#L37-L37) TEXT COMMENT '返回结果',
  `ip` VARCHAR(50) COMMENT 'IP地址',
  [location](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L62-L62) VARCHAR(200) COMMENT 'IP归属地',
  [status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67) TINYINT COMMENT '操作状态：1-成功 0-失败',
  `error_msg` TEXT COMMENT '错误信息',
  `execute_time` INT COMMENT '执行时长（ms）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY ([id](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\base\BaseEntity.java#L18-L19)),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_status` ([status](file://d:\workspace\qoder\社区服务\community-service---qoder\src\main\java\com\community\entity\Item.java#L67-L67))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
4.7 数据库索引优化建议
联合索引：根据常用查询条件创建联合索引
覆盖索引：减少回表查询，提升查询性能
分区表：日志表按时间分区，提升查询和维护效率
定期归档：历史日志定期归档到历史表
五、后端核心代码实现
5.1 项目结构
community-service-admin/
├── src/main/java/com/community/admin/
│   ├── controller/          # 控制器层
│   │   ├── AdminAuthController.java
│   │   ├── AdminUserController.java
│   │   ├── AdminRoleController.java
│   │   ├── UserManageController.java
│   │   ├── OrderManageController.java
│   │   └── ...
│   ├── service/             # 服务层
│   │   ├── AdminAuthService.java
│   │   ├── AdminUserService.java
│   │   ├── AdminPermissionService.java
│   │   └── ...
│   ├── mapper/              # 数据访问层
│   │   ├── AdminUserMapper.java
│   │   ├── AdminRoleMapper.java
│   │   └── ...
│   ├── entity/              # 实体类
│   │   ├── AdminUser.java
│   │   ├── AdminRole.java
│   │   └── ...
│   ├── dto/                 # 数据传输对象
│   │   ├── request/
│   │   │   ├── AdminLoginRequest.java
│   │   │   └── ...
│   │   └── response/
│   │       ├── AdminLoginResponse.java
│   │       └── ...
│   ├── security/            # 安全配置
│   │   ├── AdminSecurityConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── ...
│   ├── aspect/              # 切面
│   │   ├── OperationLogAspect.java
│   │   └── ...
│   ├── config/              # 配置类
│   │   └── ...
│   └── exception/           # 异常处理
│       └── ...
└── src/main/resources/
    ├── mapper/              # MyBatis XML
    └── application.yml      # 配置文件
5.2 实体类设计
AdminUser.java
java
package com.community.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.community.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 管理员实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin_user")
public class AdminUser extends BaseEntity {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（BCrypt加密）
     */
    private String password;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
}
AdminRole.java
java
package com.community.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.community.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin_role")
public class AdminRole extends BaseEntity {
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
}
AdminPermission.java
java
package com.community.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.community.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin_permission")
public class AdminPermission extends BaseEntity {
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限编码
     */
    private String permissionCode;
    
    /**
     * 权限类型：menu-菜单 button-按钮 api-接口
     */
    private String permissionType;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 路由路径
     */
    private String path;
    
    /**
     * 组件路径
     */
    private String component;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 状态：1-启用 0-禁用
     */
    private Integer status;
}
5.3 DTO设计
AdminLoginRequest.java
java
package com.community.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 管理员登录请求
 */
@Data
public class AdminLoginRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 验证码
     */
    private String captcha;
    
    /**
     * 验证码Key
     */
    private String captchaKey;
}
AdminLoginResponse.java
java
package com.community.admin.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 管理员登录响应
 */
@Data
public class AdminLoginResponse {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * Token
     */
    private String token;
    
    /**
     * 角色列表
     */
    private List<String> roles;
    
    /**
     * 权限列表
     */
    private List<String> permissions;
}
5.4 控制器层
AdminAuthController.java
java
package com.community.admin.controller;

import com.community.admin.dto.request.AdminLoginRequest;
import com.community.admin.dto.response.AdminLoginResponse;
import com.community.admin.service.AdminAuthService;
import com.community.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员认证控制器
 */
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<AdminLoginResponse> login(@Validated @RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminAuthService.login(request);
        return Result.success(response);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<AdminLoginResponse> getInfo() {
        AdminLoginResponse response = adminAuthService.getCurrentUserInfo();
        return Result.success(response);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        adminAuthService.logout();
        return Result.success();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestHeader("Authorization") String token) {
        String newToken = adminAuthService.refreshToken(token);
        return Result.success(newToken);
    }
}
5.5 服务层
AdminAuthService.java
java
package com.community.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.admin.dto.request.AdminLoginRequest;
import com.community.admin.dto.response.AdminLoginResponse;
import com.community.admin.entity.AdminUser;
import com.community.admin.mapper.AdminUserMapper;
import com.community.exception.BusinessException;
import com.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 管理员认证服务
 */
@Slf4j