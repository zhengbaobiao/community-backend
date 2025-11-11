package com.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.admin.common.ResultCode;
import com.community.admin.dto.request.LoginRequest;
import com.community.admin.dto.response.LoginResponse;
import com.community.admin.entity.AdminPermission;
import com.community.admin.entity.AdminRole;
import com.community.admin.entity.AdminUser;
import com.community.admin.exception.BusinessException;
import com.community.admin.mapper.AdminPermissionMapper;
import com.community.admin.mapper.AdminRoleMapper;
import com.community.admin.mapper.AdminUserMapper;
import com.community.admin.service.AuthService;
import com.community.admin.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证服务实现类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminUserMapper adminUserMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final AdminPermissionMapper adminPermissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    private static final String USER_INFO_PREFIX = "user:info:";

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 查询用户
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, request.getUsername());
        AdminUser user = adminUserMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 3. 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }

        // 4. 查询用户角色
        List<AdminRole> roles = adminRoleMapper.selectRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream()
                .map(AdminRole::getRoleCode)
                .collect(Collectors.toList());

        // 5. 查询用户权限
        List<AdminPermission> permissions = adminPermissionMapper.selectPermissionsByUserId(user.getId());
        List<String> permissionCodes = permissions.stream()
                .map(AdminPermission::getPermissionCode)
                .collect(Collectors.toList());

        // 6. 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 7. 缓存用户信息
        String userInfoKey = USER_INFO_PREFIX + user.getId();
        redisTemplate.opsForValue().set(userInfoKey, user, 2, TimeUnit.HOURS);

        // 8. 更新最后登录信息
        user.setLastLoginTime(LocalDateTime.now());
        // 这里可以设置IP地址,通过HttpServletRequest获取
        adminUserMapper.updateById(user);

        // 9. 构建响应
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .token(token)
                .roles(roleCodes)
                .permissions(permissionCodes)
                .build();
    }

    @Override
    public String refreshToken(String token) {
        // 1. 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 2. 检查是否可以刷新
        if (!jwtUtil.canTokenBeRefreshed(token)) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        }

        // 3. 刷新Token
        String newToken = jwtUtil.refreshToken(token);

        // 4. 将旧Token加入黑名单
        String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(blacklistKey, "1", 2, TimeUnit.HOURS);

        return newToken;
    }

    @Override
    public void logout(String token) {
        // 将Token加入黑名单
        String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(blacklistKey, "1", 2, TimeUnit.HOURS);

        // 清除用户信息缓存
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId != null) {
            String userInfoKey = USER_INFO_PREFIX + userId;
            redisTemplate.delete(userInfoKey);
        }
    }
}
