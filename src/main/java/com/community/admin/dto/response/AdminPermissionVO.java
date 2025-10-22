package com.community.admin.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限VO
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
public class AdminPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限类型(menu/button/api)
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
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 状态(1-启用 0-禁用)
     */
    private Integer status;

    /**
     * 子权限列表
     */
    private List<AdminPermissionVO> children;
}
