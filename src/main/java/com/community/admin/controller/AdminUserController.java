package com.community.admin.controller;

import com.community.admin.annotation.OperationLog;
import com.community.admin.common.PageResult;
import com.community.admin.common.Result;
import com.community.admin.dto.request.AdminUserDTO;
import com.community.admin.dto.request.AdminUserQuery;
import com.community.admin.dto.response.AdminUserVO;
import com.community.admin.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器
 *
 * @author Community Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Tag(name = "管理员管理", description = "管理员账号的增删改查、状态管理、角色分配")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 分页查询管理员列表
     */
    @Operation(summary = "管理员列表", description = "分页查询管理员,支持关键词搜索、状态筛选、时间范围")
    @OperationLog(value = "查询管理员列表", description = "分页查询管理员列表")
    @GetMapping("/list")
    public Result<PageResult<AdminUserVO>> list(@Validated AdminUserQuery query) {
        PageResult<AdminUserVO> result = adminUserService.list(query);
        return Result.success(result);
    }

    /**
     * 查询管理员详情
     */
    @Operation(summary = "管理员详情", description = "根据ID查询管理员详细信息")
    @OperationLog(value = "查询管理员详情")
    @GetMapping("/{id}")
    public Result<AdminUserVO> getById(
            @Parameter(description = "管理员ID") @PathVariable Long id) {
        AdminUserVO vo = adminUserService.getById(id);
        return Result.success(vo);
    }

    /**
     * 新增管理员
     */
    @Operation(summary = "新增管理员", description = "创建新的管理员账号,默认密码123456")
    @OperationLog(value = "新增管理员", description = "创建新的管理员账号")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody AdminUserDTO dto) {
        adminUserService.add(dto);
        return Result.success("新增成功");
    }

    /**
     * 更新管理员
     */
    @Operation(summary = "更新管理员", description = "更新管理员基本信息")
    @OperationLog(value = "更新管理员", description = "更新管理员信息")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Validated @RequestBody AdminUserDTO dto) {
        adminUserService.update(id, dto);
        return Result.success("更新成功");
    }

    /**
     * 删除管理员
     */
    @Operation(summary = "删除管理员", description = "删除管理员账号(不能删除当前登录用户)")
    @OperationLog(value = "删除管理员", description = "删除管理员账号")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @Parameter(description = "管理员ID") @PathVariable Long id) {
        adminUserService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 启用/禁用管理员
     */
    @Operation(summary = "更新状态", description = "启用或禁用管理员账号")
    @OperationLog(value = "更新管理员状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Parameter(description = "状态(1:启用 0:禁用)") @RequestParam Integer status) {
        adminUserService.updateStatus(id, status);
        return Result.success("状态更新成功");
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码", description = "重置管理员登录密码")
    @OperationLog(value = "重置密码", description = "重置管理员密码")
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        adminUserService.resetPassword(id, newPassword);
        return Result.success("密码重置成功");
    }

    /**
     * 分配角色
     */
    @Operation(summary = "分配角色", description = "为管理员分配一个或多个角色")
    @OperationLog(value = "分配角色", description = "为管理员分配角色")
    @PostMapping("/{id}/roles")
    public Result<Void> assignRoles(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Parameter(description = "角色ID数组") @RequestBody Long[] roleIds) {
        adminUserService.assignRoles(id, roleIds);
        return Result.success("角色分配成功");
    }
}
