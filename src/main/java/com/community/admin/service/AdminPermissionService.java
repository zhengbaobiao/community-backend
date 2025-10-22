package com.community.admin.service;

import com.community.admin.dto.response.AdminPermissionVO;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author Community Team
 * @since 1.0.0
 */
public interface AdminPermissionService {

    /**
     * 获取权限树
     *
     * @return 权限树
     */
    List<AdminPermissionVO> getPermissionTree();

    /**
     * 获取所有权限列表
     *
     * @return 权限列表
     */
    List<AdminPermissionVO> listAll();

    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<AdminPermissionVO> getByRoleId(Long roleId);
}
