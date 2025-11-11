package com.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.admin.common.PageResult;
import com.community.admin.common.ResultCode;
import com.community.admin.dto.request.AdminRoleDTO;
import com.community.admin.dto.request.AdminRoleQuery;
import com.community.admin.dto.response.AdminRoleVO;
import com.community.admin.entity.AdminRole;
import com.community.admin.entity.AdminRolePermission;
import com.community.admin.entity.AdminUserRole;
import com.community.admin.exception.BusinessException;
import com.community.admin.mapper.AdminRoleMapper;
import com.community.admin.mapper.AdminRolePermissionMapper;
import com.community.admin.mapper.AdminUserRoleMapper;
import com.community.admin.service.AdminRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl implements AdminRoleService {

    private final AdminRoleMapper adminRoleMapper;
    private final AdminUserRoleMapper adminUserRoleMapper;
    private final AdminRolePermissionMapper adminRolePermissionMapper;

    @Override
    public PageResult<AdminRoleVO> list(AdminRoleQuery query) {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(query.getRoleName())) {
            wrapper.like(AdminRole::getRoleName, query.getRoleName());
        }
        
        if (query.getStatus() != null) {
            wrapper.eq(AdminRole::getStatus, query.getStatus());
        }
        
        wrapper.orderByAsc(AdminRole::getSortOrder);
        
        Page<AdminRole> page = new Page<>(query.getPage(), query.getSize());
        Page<AdminRole> result = adminRoleMapper.selectPage(page, wrapper);
        
        List<AdminRoleVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), voList);
    }

    @Override
    public List<AdminRoleVO> listAll() {
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getStatus, 1);
        wrapper.orderByAsc(AdminRole::getSortOrder);
        
        List<AdminRole> list = adminRoleMapper.selectList(wrapper);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public AdminRoleVO getById(Long id) {
        AdminRole role = adminRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return convertToVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdminRoleDTO dto) {
        // 检查角色编码是否存在
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getRoleCode, dto.getRoleCode());
        if (adminRoleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.DATA_EXIST.getCode(), "角色编码已存在");
        }
        
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(dto, role);
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        
        adminRoleMapper.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, AdminRoleDTO dto) {
        AdminRole role = adminRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        BeanUtils.copyProperties(dto, role, "id", "createdAt");
        role.setUpdatedAt(LocalDateTime.now());
        
        adminRoleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AdminRole role = adminRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        // 检查是否有用户关联此角色
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getRoleId, id);
        long count = adminUserRoleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.OPERATION_ERROR.getCode(), "该角色下存在用户,无法删除");
        }
        
        // 删除角色权限关联
        LambdaQueryWrapper<AdminRolePermission> permWrapper = new LambdaQueryWrapper<>();
        permWrapper.eq(AdminRolePermission::getRoleId, id);
        adminRolePermissionMapper.delete(permWrapper);
        
        adminRoleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, Long[] permissionIds) {
        AdminRole role = adminRoleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        
        // 1. 删除原有权限
        LambdaQueryWrapper<AdminRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRolePermission::getRoleId, roleId);
        adminRolePermissionMapper.delete(wrapper);
        
        // 2. 添加新权限
        if (permissionIds != null && permissionIds.length > 0) {
            List<AdminRolePermission> rolePermissions = Arrays.stream(permissionIds)
                    .map(permissionId -> {
                        AdminRolePermission rp = new AdminRolePermission();
                        rp.setRoleId(roleId);
                        rp.setPermissionId(permissionId);
                        return rp;
                    })
                    .collect(Collectors.toList());
            
            // 批量插入
            rolePermissions.forEach(adminRolePermissionMapper::insert);
        }
    }

    private AdminRoleVO convertToVO(AdminRole role) {
        AdminRoleVO vo = new AdminRoleVO();
        BeanUtils.copyProperties(role, vo);
        return vo;
    }
}
