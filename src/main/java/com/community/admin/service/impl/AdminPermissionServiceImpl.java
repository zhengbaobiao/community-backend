package com.community.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.admin.dto.response.AdminPermissionVO;
import com.community.admin.entity.AdminPermission;
import com.community.admin.mapper.AdminPermissionMapper;
import com.community.admin.service.AdminPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AdminPermissionServiceImpl implements AdminPermissionService {

    private final AdminPermissionMapper adminPermissionMapper;

    @Override
    public List<AdminPermissionVO> getPermissionTree() {
        // 查询所有权限
        LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminPermission::getStatus, 1);
        wrapper.orderByAsc(AdminPermission::getSortOrder);
        
        List<AdminPermission> allPermissions = adminPermissionMapper.selectList(wrapper);
        
        // 转换为VO
        List<AdminPermissionVO> voList = allPermissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 构建树形结构
        return buildTree(voList, 0L);
    }

    @Override
    public List<AdminPermissionVO> listAll() {
        LambdaQueryWrapper<AdminPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminPermission::getStatus, 1);
        wrapper.orderByAsc(AdminPermission::getSortOrder);
        
        List<AdminPermission> list = adminPermissionMapper.selectList(wrapper);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<AdminPermissionVO> getByRoleId(Long roleId) {
        List<AdminPermission> permissions = adminPermissionMapper.selectPermissionsByRoleId(roleId);
        return permissions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 构建树形结构
     */
    private List<AdminPermissionVO> buildTree(List<AdminPermissionVO> allNodes, Long parentId) {
        List<AdminPermissionVO> tree = new ArrayList<>();
        
        for (AdminPermissionVO node : allNodes) {
            if (parentId.equals(node.getParentId())) {
                List<AdminPermissionVO> children = buildTree(allNodes, node.getId());
                if (!children.isEmpty()) {
                    node.setChildren(children);
                }
                tree.add(node);
            }
        }
        
        return tree;
    }

    private AdminPermissionVO convertToVO(AdminPermission permission) {
        AdminPermissionVO vo = new AdminPermissionVO();
        BeanUtils.copyProperties(permission, vo);
        return vo;
    }
}
