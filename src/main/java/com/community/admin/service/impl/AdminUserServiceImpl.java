package com.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.admin.common.PageResult;
import com.community.admin.common.ResultCode;
import com.community.admin.dto.request.AdminUserDTO;
import com.community.admin.dto.request.AdminUserQuery;
import com.community.admin.dto.response.AdminUserVO;
import com.community.admin.entity.AdminRole;
import com.community.admin.entity.AdminUser;
import com.community.admin.entity.AdminUserRole;
import com.community.admin.exception.BusinessException;
import com.community.admin.mapper.AdminRoleMapper;
import com.community.admin.mapper.AdminUserMapper;
import com.community.admin.mapper.AdminUserRoleMapper;
import com.community.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员服务实现类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final AdminUserRoleMapper adminUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<AdminUserVO> list(AdminUserQuery query) {
        // 构建查询条件
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索(用户名、真实姓名、手机号)
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(AdminUser::getUsername, query.getKeyword())
                    .or().like(AdminUser::getRealName, query.getKeyword())
                    .or().like(AdminUser::getPhone, query.getKeyword()));
        }
        
        // 状态筛选
        if (query.getStatus() != null) {
            wrapper.eq(AdminUser::getStatus, query.getStatus());
        }
        
        // 时间范围
        if (StringUtils.hasText(query.getStartTime())) {
            wrapper.ge(AdminUser::getCreatedAt, query.getStartTime());
        }
        if (StringUtils.hasText(query.getEndTime())) {
            wrapper.le(AdminUser::getCreatedAt, query.getEndTime());
        }
        
        // 排序
        wrapper.orderByDesc(AdminUser::getCreatedAt);
        
        // 分页查询
        Page<AdminUser> page = new Page<>(query.getPage(), query.getSize());
        Page<AdminUser> result = adminUserMapper.selectPage(page, wrapper);
        
        // 转换VO
        List<AdminUserVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), voList);
    }

    @Override
    public AdminUserVO getById(Long id) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdminUserDTO dto) {
        // 检查用户名是否存在
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getUsername, dto.getUsername());
        if (adminUserMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXIST);
        }
        
        // 创建用户
        AdminUser user = new AdminUser();
        BeanUtils.copyProperties(dto, user);
        
        // 加密密码
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            // 默认密码: 123456
            user.setPassword(passwordEncoder.encode("123456"));
        }
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        adminUserMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, AdminUserDTO dto) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        // 检查用户名是否被其他用户使用
        if (StringUtils.hasText(dto.getUsername()) && !dto.getUsername().equals(user.getUsername())) {
            LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AdminUser::getUsername, dto.getUsername());
            wrapper.ne(AdminUser::getId, id);
            if (adminUserMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(ResultCode.USERNAME_EXIST);
            }
        }
        
        // 更新用户信息
        BeanUtils.copyProperties(dto, user, "id", "password", "createdAt");
        user.setUpdatedAt(LocalDateTime.now());
        
        adminUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        // 不能删除自己
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                String currentUsername = authentication.getName();
                if (user.getUsername().equals(currentUsername)) {
                    throw new BusinessException(ResultCode.OPERATION_ERROR.getCode(), "不能删除当前登录用户");
                }
            }
        } catch (Exception e) {
            // 如果获取当前用户失败,忽略此检查
        }
        
        // 删除用户角色关联
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getUserId, id);
        adminUserRoleMapper.delete(wrapper);
        
        adminUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        AdminUser user = adminUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, Long[] roleIds) {
        AdminUser user = adminUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        // 1. 删除用户原有角色
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getUserId, userId);
        adminUserRoleMapper.delete(wrapper);
        
        // 2. 添加新角色
        if (roleIds != null && roleIds.length > 0) {
            List<AdminUserRole> userRoles = Arrays.stream(roleIds)
                    .map(roleId -> {
                        AdminUserRole ur = new AdminUserRole();
                        ur.setUserId(userId);
                        ur.setRoleId(roleId);
                        return ur;
                    })
                    .collect(Collectors.toList());
            
            // 批量插入
            userRoles.forEach(adminUserRoleMapper::insert);
        }
    }

    /**
     * 转换为VO
     */
    private AdminUserVO convertToVO(AdminUser user) {
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        
        // 查询用户角色
        List<AdminRole> roles = adminRoleMapper.selectRolesByUserId(user.getId());
        vo.setRoleNames(roles.stream().map(AdminRole::getRoleName).collect(Collectors.toList()));
        vo.setRoleIds(roles.stream().map(AdminRole::getId).collect(Collectors.toList()));
        
        return vo;
    }
}
