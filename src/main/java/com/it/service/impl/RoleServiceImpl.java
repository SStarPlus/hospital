package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Role;
import com.it.mapper.PermissionMapper;
import com.it.mapper.RoleMapper;
import com.it.service.RoleService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈角色service实现类〉<br>
 *
 * @author
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Page<Role> selectPage(Role role, int page, int limit) {
        EntityWrapper<Role> searchInfo = new EntityWrapper<>();
        Page<Role> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(role.getRole())) {
            searchInfo.eq("role", role.getRole());
        }
        List<Role> resultList = roleMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public Role selectByPrimaryKey(String id) {
        return roleMapper.selectById(id);
    }

    @Override
    public boolean insert(Role role) {
        role.setCreateName(itdragonUtils.getSessionUserName());
        Integer insert = roleMapper.insert(role);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(Role role) {
        Integer update = roleMapper.updateById(role);
        if (update > 0) {

            return true;
        }
        return false;
    }

    @Override
    public boolean accreditByPrimaryKey(Role role) {
        int result = 0;
        //编辑角色成功后开始修改权限
        //先删除原来的权限
        roleMapper.delByRoleId(role.getId());
        if (!ItdragonUtils.stringIsNotBlack(role.getPermissionsIds())) {
            return true;
        }
        //然后重新分配
        String ids = role.getPermissionsIds();
        String[] idList = ids.split(",");
        for (String s : idList) {
            if (ItdragonUtils.stringIsNotBlack(s)) {
                result = permissionMapper.allotPermission(role.getId(), s);
            }
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteByPrimaryKey(String id) {
        Integer delete = roleMapper.deleteById(id);
        if (delete > 0) {
            //删除角色成功后删除相应的权限
            roleMapper.delByRoleId(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Role> getRoleList() {
        EntityWrapper<Role> searchInfo = new EntityWrapper<>();
        //不查询超级管理员角色
        searchInfo.ne("id", "1");
        List<Role> roleList = roleMapper.selectList(searchInfo);
        return roleList;
    }
}