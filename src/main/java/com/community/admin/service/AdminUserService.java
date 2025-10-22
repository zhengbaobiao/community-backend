package com.community.admin.service;

import com.community.admin.common.PageResult;
import com.community.admin.dto.request.AdminUserDTO;
import com.community.admin.dto.request.AdminUserQuery;
import com.community.admin.dto.response.AdminUserVO;

/**
 * 管理员服务接口
 *
 * @author Community Team
 * @since 1.0.0
 */
public interface AdminUserService {

    /**
     * 分页查询管理员列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<AdminUserVO> list(AdminUserQuery query);

    /**
     * 根据ID查询管理员详情
     *
     * @param id 管理员ID
     * @return 管理员详情
     */
    AdminUserVO getById(Long id);

    /**
     * 新增管理员
     *
     * @param dto 管理员信息
     */
    void add(AdminUserDTO dto);

    /**
     * 更新管理员
     *
     * @param id  管理员ID
     * @param dto 管理员信息
     */
    void update(Long id, AdminUserDTO dto);

    /**
     * 删除管理员
     *
     * @param id 管理员ID
     */
    void delete(Long id);

    /**
     * 启用/禁用管理员
     *
     * @param id     管理员ID
     * @param status 状态(1-启用 0-禁用)
     */
    void updateStatus(Long id, Integer status);

    /**
     * 重置密码
     *
     * @param id          管理员ID
     * @param newPassword 新密码
     */
    void resetPassword(Long id, String newPassword);

    /**
     * 分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void assignRoles(Long userId, Long[] roleIds);
}
