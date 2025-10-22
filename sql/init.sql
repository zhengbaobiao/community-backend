-- ====================================================
-- 社区服务后台管理系统 - 数据库初始化脚本
-- ====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS community_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_admin;

-- ====================================================
-- 1. 管理员表 (admin_user)
-- ====================================================
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-启用 0-禁用',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记:0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ====================================================
-- 2. 角色表 (admin_role)
-- ====================================================
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-启用 0-禁用',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记:0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ====================================================
-- 3. 权限表 (admin_permission)
-- ====================================================
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
  `permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型:menu/button/api',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父权限ID,0表示根节点',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-启用 0-禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记:0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_permission_type` (`permission_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ====================================================
-- 4. 用户角色关联表 (admin_user_role)
-- ====================================================
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '管理员ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ====================================================
-- 5. 角色权限关联表 (admin_role_permission)
-- ====================================================
DROP TABLE IF EXISTS `admin_role_permission`;
CREATE TABLE `admin_role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ====================================================
-- 6. 操作日志表 (admin_operation_log)
-- ====================================================
DROP TABLE IF EXISTS `admin_operation_log`;
CREATE TABLE `admin_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作人用户名',
  `operation` VARCHAR(100) DEFAULT NULL COMMENT '操作类型',
  `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
  `params` TEXT DEFAULT NULL COMMENT '请求参数',
  `result` TEXT DEFAULT NULL COMMENT '返回结果',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `location` VARCHAR(200) DEFAULT NULL COMMENT 'IP归属地',
  `status` TINYINT DEFAULT NULL COMMENT '操作状态:1-成功 0-失败',
  `error_msg` TEXT DEFAULT NULL COMMENT '错误信息',
  `execute_time` INT DEFAULT NULL COMMENT '执行时长(ms)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ====================================================
-- 初始化数据
-- ====================================================

-- 插入预置角色
INSERT INTO `admin_role` (`id`, `role_name`, `role_code`, `description`, `status`, `sort_order`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限,负责系统配置和角色分配', 1, 1),
(2, '系统管理员', 'SYSTEM_ADMIN', '管理管理员账号、角色权限、系统参数', 1, 2),
(3, '运营管理员', 'OPERATION_ADMIN', '管理业务数据、内容审核、数据分析', 1, 3),
(4, '客服人员', 'CUSTOMER_SERVICE', '处理用户咨询、订单问题、售后服务', 1, 4);

-- 插入默认超级管理员账号 (密码: admin123, BCrypt加密)
-- BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
INSERT INTO `admin_user` (`id`, `username`, `password`, `real_name`, `email`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'admin@community.com', 1);

-- 分配超级管理员角色
INSERT INTO `admin_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 插入菜单权限数据
INSERT INTO `admin_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `path`, `component`, `icon`, `sort_order`, `status`) VALUES
-- 一级菜单
(1, '首页', 'dashboard', 'menu', 0, '/dashboard', 'Dashboard', 'dashboard', 1, 1),
(2, '用户管理', 'user', 'menu', 0, '/user', 'Layout', 'user', 2, 1),
(3, '订单管理', 'order', 'menu', 0, '/order', 'Layout', 'order', 3, 1),
(4, '系统管理', 'system', 'menu', 0, '/system', 'Layout', 'setting', 4, 1),

-- 用户管理子菜单
(5, '用户列表', 'user:list', 'menu', 2, '/user/list', 'User/List', 'user', 1, 1),

-- 订单管理子菜单
(6, '订单列表', 'order:list', 'menu', 3, '/order/list', 'Order/List', 'order', 1, 1),

-- 系统管理子菜单
(7, '管理员管理', 'system:admin', 'menu', 4, '/system/admin', 'System/Admin', 'user', 1, 1),
(8, '角色管理', 'system:role', 'menu', 4, '/system/role', 'System/Role', 'role', 2, 1),
(9, '权限管理', 'system:permission', 'menu', 4, '/system/permission', 'System/Permission', 'permission', 3, 1),
(10, '操作日志', 'system:log', 'menu', 4, '/system/log', 'System/Log', 'log', 4, 1),

-- 按钮权限
(11, '用户查询', 'user:query', 'button', 5, NULL, NULL, NULL, 1, 1),
(12, '用户详情', 'user:detail', 'button', 5, NULL, NULL, NULL, 2, 1),
(13, '用户启用', 'user:enable', 'button', 5, NULL, NULL, NULL, 3, 1),
(14, '用户禁用', 'user:disable', 'button', 5, NULL, NULL, NULL, 4, 1),

(15, '订单查询', 'order:query', 'button', 6, NULL, NULL, NULL, 1, 1),
(16, '订单详情', 'order:detail', 'button', 6, NULL, NULL, NULL, 2, 1),

(17, '管理员查询', 'system:admin:query', 'button', 7, NULL, NULL, NULL, 1, 1),
(18, '管理员新增', 'system:admin:add', 'button', 7, NULL, NULL, NULL, 2, 1),
(19, '管理员编辑', 'system:admin:edit', 'button', 7, NULL, NULL, NULL, 3, 1),
(20, '管理员删除', 'system:admin:delete', 'button', 7, NULL, NULL, NULL, 4, 1),

(21, '角色查询', 'system:role:query', 'button', 8, NULL, NULL, NULL, 1, 1),
(22, '角色新增', 'system:role:add', 'button', 8, NULL, NULL, NULL, 2, 1),
(23, '角色编辑', 'system:role:edit', 'button', 8, NULL, NULL, NULL, 3, 1),
(24, '角色删除', 'system:role:delete', 'button', 8, NULL, NULL, NULL, 4, 1),
(25, '角色授权', 'system:role:permission', 'button', 8, NULL, NULL, NULL, 5, 1);

-- 为超级管理员角色分配所有权限
INSERT INTO `admin_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `admin_permission` WHERE `deleted` = 0;

COMMIT;
