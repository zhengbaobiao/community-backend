package com.community.admin.service;

import com.community.admin.dto.request.LoginRequest;
import com.community.admin.dto.response.LoginResponse;

/**
 * 认证服务接口
 *
 * @author Community Team
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    String refreshToken(String token);

    /**
     * 退出登录
     *
     * @param token Token
     */
    void logout(String token);
}
