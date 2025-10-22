package com.community.admin.service;

import com.community.admin.common.PageResult;
import com.community.admin.dto.request.OperationLogQuery;
import com.community.admin.dto.response.OperationLogVO;

/**
 * 操作日志服务接口
 *
 * @author Community Team
 * @since 1.0.0
 */
public interface AdminOperationLogService {

    /**
     * 分页查询操作日志
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<OperationLogVO> list(OperationLogQuery query);

    /**
     * 根据ID查询日志详情
     *
     * @param id 日志ID
     * @return 日志详情
     */
    OperationLogVO getById(Long id);
}
