package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Permission;
import com.it.entity.SubSystemInfo;
import com.it.service.PermissionService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：〈菜单管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 菜单管理首页页面跳转
     * （将数据库中所有的菜单信息查询出来封装好传给前台，前台通过
     * layui-tree插件现实菜单数据）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/menuManagerHouse.do")
    public ModelAndView menuManagerHouse(ModelAndView mv) {
        //获取子系统列表
        List<SubSystemInfo> sysList = permissionService.getSubSystemInfo();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (SubSystemInfo sys : sysList) {
            Map<String, Object> sysMap = new HashMap<>();
            sysMap.put("name", sys.getSubSystemName());
            sysMap.put("id", sys.getSubSystemId());
            List<Map<String, Object>> mapList = permissionService.getMenuByParentId(sys.getSubSystemId());
            sysMap.put("children", mapList);
            //此判断是将非一级菜单的信息不封装到结果集中，避免前台菜单树展示出二级菜单（注：二级菜单在一级菜单下拉中展示，
            // 取消此判断会让二级菜单和一级菜单并列展示出来）
            if (sys.getTeamId().equals("0")) {
                resultList.add(sysMap);
            }

        }
        mv.addObject("sysList", resultList);
        mv.setViewName("/systemSet/menuManager");
        return mv;
    }

    /**
     * 菜单管理界面列表
     *
     * @param permissionId
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("menu.do")
    public TableResultResponse userTables(int page, int limit, String permissionId) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Permission> pageInfo = permissionService.getPermissionByParentId(permissionId, page, limit);
        for (Permission permissionEntity : pageInfo.getRecords()) {
            Map<String, Object> permissionMap = new HashMap<>(16);
            permissionMap.put("id", permissionEntity.getPermissionId());
            permissionMap.put("parentId", permissionEntity.getParentId());
            permissionMap.put("permissionName", permissionEntity.getPermissionName());
            permissionMap.put("priority", permissionEntity.getPriority());
            permissionMap.put("url", permissionEntity.getUrl());
            permissionMap.put("permissionMark", permissionEntity.getPermissionMark());
            permissionMap.put("permissionType", permissionEntity.getPermissionType());
            permissionMap.put("icon", "<i class='" + permissionEntity.getIcon() + "'></i>");
            permissionMap.put("children", permissionService.getPermissionByParentId(permissionEntity.getPermissionId()));
            infoList.add(permissionMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增菜单页面弹窗
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/addMenuHouser")
    public ModelAndView addMenuHouser(String parentId, ModelAndView mv) {
        Permission permission = permissionService.getPermissionById(parentId);
        //此判断是菜单管理界面右侧菜单信息展示时，如果未点击右侧菜单树则说明查询的是一级菜单，一级
        //菜单的父id都是0，则传入0即可，否则就用前台传入的父id
        if (permission != null) {
            mv.addObject("permission", permission);
        } else {
            Permission permission1 = new Permission();
            permission1.setPermissionId("0");
            mv.addObject("permission", permission1);
        }

        mv.setViewName("/systemSet/addMenu");
        return mv;
    }

    /**
     * 新增菜单
     *
     * @param permission
     * @return
     */
    @PostMapping("/addMenu")
    @ResponseBody
    public ResultResponse addMenu(Permission permission) {
        String oldChar = permission.getPermissionMark().split(":")[0];
        String newMark;
        //如果新增一级菜单则直接拼接权限标识，否则（新增的是二级菜单）取一级菜单权限标识的后置标识在拼接
        //拼接规则：
        //       一级菜单：‘类型+ ：+权限标识符’
        //       二级菜单：‘类型+ ：+父权限标识符+ ：+权限标识符’
        if (ItdragonUtils.stringIsNotBlack(oldChar)) {
            String newChar = permission.getPermissionType();
            newMark = permission.getPermissionMark().replace(oldChar, newChar);
        } else {
            newMark = permission.getPermissionType() + permission.getPermissionMark();
        }

        permission.setPermissionMark(newMark);
        boolean result = permissionService.insert(permission);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @PostMapping("/delMenu")
    @ResponseBody
    public ResultResponse addMenu(String id) {
        boolean result = permissionService.delete(id);
        if (!result) {
            return Result.resuleError("删除成功");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑菜单页面弹窗
     *
     * @param permissionId
     * @return
     */
    @RequestMapping("/editMenuHouser")
    public ModelAndView editMenuHouser(String permissionId, ModelAndView mv) {
        Permission permission = permissionService.getPermissionById(permissionId);
        mv.addObject("permission", permission);
        mv.setViewName("/systemSet/editMenu");
        return mv;
    }

    /**
     * 编辑菜单
     *
     * @param permission
     * @return
     */
    @PostMapping("/editMenu")
    @ResponseBody
    public ResultResponse editMenu(Permission permission) {
        boolean result = permissionService.update(permission);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }
}