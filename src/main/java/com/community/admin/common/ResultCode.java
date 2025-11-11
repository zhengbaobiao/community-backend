package com.community.admin.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author Community Team
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权,请先登录"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "没有权限访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),

    /**
     * 账号已被禁用
     */
    ACCOUNT_DISABLED(1002, "账号已被禁用"),

    /**
     * Token无效
     */
    TOKEN_INVALID(1003, "Token无效"),

    /**
     * Token已过期
     */
    TOKEN_EXPIRED(1004, "Token已过期"),

    /**
     * 用户名已存在
     */
    USERNAME_EXIST(2001, "用户名已存在"),

    /**
     * 数据已存在
     */
    DATA_EXIST(2002, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST(2003, "数据不存在");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;
}
