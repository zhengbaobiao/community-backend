package com.community.admin.service;

import com.community.admin.common.PageResult;
import com.community.admin.dto.request.AdminRoleDTO;
import com.community.admin.dto.request.AdminRoleQuery;
import com.community.admin.dto.response.AdminRoleVO;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author Community Team
 * @since 1.0.0
 */
public interface AdminRoleService {

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<AdminRoleVO> list(AdminRoleQuery query);

    /**
     * 查询所有角色(不分页)
     *
     * @return 角色列表
     */
    List<AdminRoleVO> listAll();

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    AdminRoleVO getById(Long id);

    /**
     * 新增角色
     *
     * @param dto 角色信息
     */
    void add(AdminRoleDTO dto);

    /**
     * 更新角色
     *
     * @param id  角色ID
     * @param dto 角色信息
     */
    void update(Long id, AdminRoleDTO dto);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void delete(Long id);

    /**
     * 分配权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, Long[] permissionIds);
}
