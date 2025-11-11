package com.community.admin.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志VO
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
public class OperationLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 操作人用户名
     */
    private String username;

    /**
     * 操作类型
     */
    private String operation;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回结果
     */
    private String result;

    /**
     * IP地址
     */
    private String ip;

    /**
     * IP归属地
     */
    private String location;

    /**
     * 操作状态(1-成功 0-失败)
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 执行时长(ms)
     */
    private Integer executeTime;

    /**
     * 操作时间
     */
    private LocalDateTime createdAt;
}
