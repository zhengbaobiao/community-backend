package com.community.admin.dto.request;

import com.community.admin.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 操作日志查询条件
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作人用户名
     */
    private String username;

    /**
     * 操作类型
     */
    private String operation;

    /**
     * 操作状态(1-成功 0-失败)
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
