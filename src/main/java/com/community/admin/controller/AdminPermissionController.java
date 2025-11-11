package com.community.admin.controller;

import com.community.admin.annotation.OperationLog;
import com.community.admin.common.Result;
import com.community.admin.dto.response.AdminPermissionVO;
import com.community.admin.service.AdminPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 *
 * @author Community Team
 * @since 1.0.0
 */
@Tag(name = "权限管理", description = "权限查询、权限树、角色权限")
@RestController
@RequestMapping("/admin/permission")
@RequiredArgsConstructor
public class AdminPermissionController {

    private final AdminPermissionService adminPermissionService;

    /**
     * 获取权限树
     */
    @Operation(summary = "获取权限树", description = "查询所有权限并以树形结构返回,用于菜单展示")
    @OperationLog(value = "查询权限树")
    @GetMapping("/tree")
    public Result<List<AdminPermissionVO>> getPermissionTree() {
        List<AdminPermissionVO> tree = adminPermissionService.getPermissionTree();
        return Result.success(tree);
    }

    /**
     * 获取所有权限列表
     */
    @Operation(summary = "获取所有权限", description = "查询所有权限的平铺列表,用于权限分配")
    @GetMapping("/list")
    public Result<List<AdminPermissionVO>> listAll() {
        List<AdminPermissionVO> list = adminPermissionService.listAll();
        return Result.success(list);
    }

    /**
     * 根据角色ID获取权限列表
     */
    @Operation(summary = "查询角色权限", description = "根据角色ID查询该角色拥有的所有权限")
    @GetMapping("/role/{roleId}")
    public Result<List<AdminPermissionVO>> getByRoleId(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<AdminPermissionVO> list = adminPermissionService.getByRoleId(roleId);
        return Result.success(list);
    }
}
