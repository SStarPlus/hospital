
package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Menu;
import com.it.entity.Permission;
import com.it.entity.SubSystemInfo;
import com.it.mapper.PermissionMapper;
import com.it.mapper.RoleMapper;
import com.it.service.PermissionService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 对权限进行操作
 */

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;

    private Log log = LogFactory.getLog(PermissionServiceImpl.class);

    @Override
    public List<SubSystemInfo> getSubSystemInfoByUserId(String userId) {
        return permissionMapper.getSubSystemInfoByUserId(userId);
    }

    @Override
    public List<SubSystemInfo> getSubSystemInfo() {
        return permissionMapper.getSubSystemInfo();
    }

    @Override
    public List<Permission> getPermissionList() {
        return permissionMapper.selectList(null);
    }

    @Override
    public List<Permission> getPermissionListByRoleId(String roleId) {
        return permissionMapper.selectPermissionByRoleId(roleId);
    }

    @Override
    public List<Permission> getPermissionListForRole(String permissionType, String permissionName, String roleId) {
        EntityWrapper<Permission> searchInfo = new EntityWrapper<>();
        if (permissionType != null && !"".equals(permissionType)) {
            searchInfo.eq("permissionType", permissionType);
        }
        if (permissionName != null && !"".equals(permissionName)) {
            searchInfo.eq("permissionName", permissionName);
        }
        searchInfo.ne("permissionName", "资源管理");
        searchInfo.ne("permissionId", "334");
        searchInfo.ne("parentId", "334");
        List<Permission> permissionList = permissionMapper.selectList(searchInfo);
        List<String> permissionIds = roleMapper.slectPermissionAndUser(roleId);
        List<Permission> resultList = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (!permissionIds.contains(permission.getPermissionId())) {
                resultList.add(permission);
            }
        }

        return resultList;
    }


    @Override
    public boolean delPermission(String permissionId, String roleId) {
        int result = permissionMapper.delPermission(permissionId, roleId);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getMenuByParentId(String id) {
        EntityWrapper<Permission> searchInfo = new EntityWrapper<>();
        searchInfo.eq("parentId", id);
        List<Permission> menuList = permissionMapper.selectList(searchInfo);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Permission permission : menuList) {
            List<Map<String, Object>> childrenResultList = new ArrayList<>();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", permission.getPermissionId());
            resultMap.put("name", permission.getPermissionName());
            EntityWrapper<Permission> childrenSearchInfo = new EntityWrapper<>();
            childrenSearchInfo.eq("parentId", permission.getPermissionId());
            List<Permission> childrenMenuList = permissionMapper.selectList(childrenSearchInfo);
            for (Permission permission1 : childrenMenuList) {
                Map<String, Object> childrenResultMap = new HashMap<>();
                childrenResultMap.put("id", permission1.getPermissionId());
                childrenResultMap.put("name", permission1.getPermissionName());
                childrenResultList.add(childrenResultMap);
            }
            resultMap.put("children", childrenResultList);
            resultList.add(resultMap);
        }
        return resultList;
    }


    @Override
    public Page<Permission> getPermissionByParentId(String id, int page, int limit) {
        EntityWrapper<Permission> searchInfo = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(id)) {
            searchInfo.eq("parentId", id);
        } else {
            searchInfo.eq("parentId", "0");
        }
        Page<Permission> pageInfo = new Page<>(page, limit);
        List<Permission> menuList = permissionMapper.selectPage(pageInfo, searchInfo);
        if (!menuList.isEmpty()) {
            pageInfo.setRecords(menuList);
        }
        return pageInfo;
    }

    @Override
    public String getPermissionByParentId(String id) {
        EntityWrapper<Permission> searchInfo = new EntityWrapper<>();
        searchInfo.eq("parentId", id);
        String info = "";
        List<Permission> menuList = permissionMapper.selectList(searchInfo);
        for (Permission permission : menuList) {
            info = info + "/ " + permission.getPermissionName();
        }
        return info;
    }

    @Override
    public Permission getPermissionById(String id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public boolean insert(Permission permission) {
        permission.setCreateTime(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        permission.setAvailable(1);
        int result = permissionMapper.insert(permission);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Permission permission) {
        int result = permissionMapper.updateById(permission);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        int result = permissionMapper.deleteById(id);
        if (result > 0) {
            permissionMapper.delPermissionByPermissionId(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean thisRoleHavePer(String roleId, String perId) {
        //默认为"false"
        boolean result = false;
        //查询出该角色下所有的权限集合
        List<Permission> permissionList = permissionMapper.selectPermissionByRoleId(roleId);
        //将传入的菜单id作比较确定该角色是否拥有这个权限
        for (Permission permission : permissionList) {
            if (perId.equals(permission.getPermissionId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public List<Menu> getMenuInfoByUserId(String userId) {
        if ("".equals(userId) && userId == null) {
            return new ArrayList<>();
        }
        List<Menu> allMenu = permissionMapper.getMenuInfoByUserId(userId);
        log.info("allMenu" + allMenu);
        return allMenu;
    }
}