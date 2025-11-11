package com.community.admin.dto.request;

import com.community.admin.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色查询条件
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminRoleQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 状态(1-启用 0-禁用)
     */
    private Integer status;
}
