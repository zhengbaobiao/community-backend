package com.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.admin.entity.AdminRolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联Mapper
 */
@Mapper
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermission> {
}
