package com.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.admin.entity.AdminUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员角色关联Mapper
 */
@Mapper
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {
}
