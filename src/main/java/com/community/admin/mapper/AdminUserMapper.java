package com.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper
 *
 * @author Community Team
 * @since 1.0.0
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
