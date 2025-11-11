package com.community.admin.controller;

import com.community.admin.annotation.OperationLog;
import com.community.admin.common.Result;
import com.community.admin.dto.request.LoginRequest;
import com.community.admin.dto.response.LoginResponse;
import com.community.admin.service.AuthService;
import com.community.admin.config.JwtProperties;
import com.community.admin.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 *
 * @author Community Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证授权", description = "用户登录、退出、Token管理")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "管理员登录系统，返回Token和用户信息")
    @OperationLog(value = "用户登录", description = "管理员登录系统")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据Token获取当前登录用户信息")
    @GetMapping("/info")
    public Result<LoginResponse> getUserInfo(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        // 这里简化处理,实际应该从数据库查询完整用户信息
        LoginResponse response = LoginResponse.builder()
                .userId(userId)
                .username(jwtUtil.getUsernameFromToken(token))
                .build();
        return Result.success(response);
    }

    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "在Token即将过期时刷新获取新Token")
    @PostMapping("/refresh")
    public Result<String> refreshToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String newToken = authService.refreshToken(token);
        return Result.success("Token刷新成功", newToken);
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "管理员退出系统，Token将被加入黑名单")
    @OperationLog(value = "退出登录", description = "管理员退出系统")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        authService.logout(token);
        return Result.success("退出登录成功");
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getTokenPrefix() + " ")) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length() + 1);
        }
        return null;
    }
}
