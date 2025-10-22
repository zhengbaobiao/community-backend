package com.community.admin.dto.request;

import com.community.admin.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员查询条件
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词(用户名/真实姓名/手机号)
     */
    private String keyword;

    /**
     * 状态(1-启用 0-禁用)
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
