package com.community.admin.controller;

import com.community.admin.annotation.OperationLog;
import com.community.admin.common.PageResult;
import com.community.admin.common.Result;
import com.community.admin.dto.request.AdminRoleDTO;
import com.community.admin.dto.request.AdminRoleQuery;
import com.community.admin.dto.response.AdminRoleVO;
import com.community.admin.service.AdminRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author Community Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin/role")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的增删改查、权限分配")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    /**
     * 分页查询角色列表
     */
    @Operation(summary = "角色列表", description = "分页查询角色列表,支持角色名称筛选")
    @OperationLog(value = "查询角色列表")
    @GetMapping("/list")
    public Result<PageResult<AdminRoleVO>> list(@Validated AdminRoleQuery query) {
        PageResult<AdminRoleVO> result = adminRoleService.list(query);
        return Result.success(result);
    }

    /**
     * 查询所有角色(不分页)
     */
    @Operation(summary = "所有角色", description = "查询所有启用状态的角色,用于下拉选择")
    @GetMapping("/all")
    public Result<List<AdminRoleVO>> listAll() {
        List<AdminRoleVO> list = adminRoleService.listAll();
        return Result.success(list);
    }

    /**
     * 查询角色详情
     */
    @Operation(summary = "角色详情", description = "根据ID查询角色详细信息")
    @OperationLog(value = "查询角色详情")
    @GetMapping("/{id}")
    public Result<AdminRoleVO> getById(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        AdminRoleVO vo = adminRoleService.getById(id);
        return Result.success(vo);
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色", description = "创建新的角色")
    @OperationLog(value = "新增角色")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody AdminRoleDTO dto) {
        adminRoleService.add(dto);
        return Result.success("新增成功");
    }

    /**
     * 更新角色
     */
    @Operation(summary = "更新角色", description = "更新角色基本信息")
    @OperationLog(value = "更新角色")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Validated @RequestBody AdminRoleDTO dto) {
        adminRoleService.update(id, dto);
        return Result.success("更新成功");
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除角色(如有用户关联则无法删除)")
    @OperationLog(value = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        adminRoleService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 分配权限
     */
    @Operation(summary = "分配权限", description = "为角色分配一个或多个权限")
    @OperationLog(value = "分配权限")
    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "权限ID数组") @RequestBody Long[] permissionIds) {
        adminRoleService.assignPermissions(id, permissionIds);
        return Result.success("权限分配成功");
    }
}
