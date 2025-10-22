package com.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.admin.common.PageResult;
import com.community.admin.common.ResultCode;
import com.community.admin.dto.request.OperationLogQuery;
import com.community.admin.dto.response.OperationLogVO;
import com.community.admin.entity.AdminOperationLog;
import com.community.admin.exception.BusinessException;
import com.community.admin.mapper.AdminOperationLogMapper;
import com.community.admin.service.AdminOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志服务实现类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AdminOperationLogServiceImpl implements AdminOperationLogService {

    private final AdminOperationLogMapper adminOperationLogMapper;

    @Override
    public PageResult<OperationLogVO> list(OperationLogQuery query) {
        LambdaQueryWrapper<AdminOperationLog> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(query.getUsername())) {
            wrapper.like(AdminOperationLog::getUsername, query.getUsername());
        }
        
        if (StringUtils.hasText(query.getOperation())) {
            wrapper.like(AdminOperationLog::getOperation, query.getOperation());
        }
        
        if (query.getStatus() != null) {
            wrapper.eq(AdminOperationLog::getStatus, query.getStatus());
        }
        
        if (StringUtils.hasText(query.getStartTime())) {
            wrapper.ge(AdminOperationLog::getCreatedAt, query.getStartTime());
        }
        
        if (StringUtils.hasText(query.getEndTime())) {
            wrapper.le(AdminOperationLog::getCreatedAt, query.getEndTime());
        }
        
        wrapper.orderByDesc(AdminOperationLog::getCreatedAt);
        
        Page<AdminOperationLog> page = new Page<>(query.getPage(), query.getSize());
        Page<AdminOperationLog> result = adminOperationLogMapper.selectPage(page, wrapper);
        
        List<OperationLogVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), voList);
    }

    @Override
    public OperationLogVO getById(Long id) {
        AdminOperationLog log = adminOperationLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return convertToVO(log);
    }

    private OperationLogVO convertToVO(AdminOperationLog log) {
        OperationLogVO vo = new OperationLogVO();
        BeanUtils.copyProperties(log, vo);
        return vo;
    }
}
