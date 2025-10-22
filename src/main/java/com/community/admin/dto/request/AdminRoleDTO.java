package com.community.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
public class AdminRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态(1-启用 0-禁用)
     */
    private Integer status;

    /**
     * 排序号
     */
    private Integer sortOrder;
}
