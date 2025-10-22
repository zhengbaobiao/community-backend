package com.community.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.admin.entity.AdminOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper
 *
 * @author Community Team
 * @since 1.0.0
 */
@Mapper
public interface AdminOperationLogMapper extends BaseMapper<AdminOperationLog> {
}
