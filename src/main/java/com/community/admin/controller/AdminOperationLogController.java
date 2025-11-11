package com.community.admin.controller;

import com.community.admin.annotation.OperationLog;
import com.community.admin.common.PageResult;
import com.community.admin.common.Result;
import com.community.admin.dto.request.OperationLogQuery;
import com.community.admin.dto.response.OperationLogVO;
import com.community.admin.service.AdminOperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器
 *
 * @author Community Team
 * @since 1.0.0
 */
@Tag(name = "操作日志", description = "操作日志查询、日志详情")
@RestController
@RequestMapping("/admin/log")
@RequiredArgsConstructor
public class AdminOperationLogController {

    private final AdminOperationLogService adminOperationLogService;

    /**
     * 分页查询操作日志
     */
    @Operation(summary = "操作日志列表", description = "分页查询操作日志,支持操作人、操作类型、时间范围筛选")
    @OperationLog(value = "查询操作日志")
    @GetMapping("/list")
    public Result<PageResult<OperationLogVO>> list(@Validated OperationLogQuery query) {
        PageResult<OperationLogVO> result = adminOperationLogService.list(query);
        return Result.success(result);
    }

    /**
     * 查询日志详情
     */
    @Operation(summary = "日志详情", description = "根据ID查询操作日志的详细信息")
    @GetMapping("/{id}")
    public Result<OperationLogVO> getById(
            @Parameter(description = "日志ID") @PathVariable Long id) {
        OperationLogVO vo = adminOperationLogService.getById(id);
        return Result.success(vo);
    }
}
