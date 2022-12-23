package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Role;
import com.it.entity.SubSystemInfo;
import com.it.service.PermissionService;
import com.it.service.RoleService;
import com.it.service.UserService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理接口
 *
 * @author
 */

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 用户管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/roleManagerHouse.do")
    public ModelAndView userManagerHouse(ModelAndView mv) {
        mv.setViewName("systemSet/roleManager");
        return mv;
    }

    /**
     * 角色管理界面列表
     *
     * @param role
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("role.do")
    public TableResultResponse getTables(Role role, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Role> pageInfo = roleService.selectPage(role, page, limit);
        for (Role record : pageInfo.getRecords()) {
            Map<String, Object> userMap = new HashMap<>(16);
            userMap.put("id", record.getId());
            userMap.put("role", record.getRole());
            userMap.put("description", record.getDescription());
            userMap.put("createName", record.getCreateName());
            infoList.add(userMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增角色界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addRole.do")
    public ModelAndView addRole(ModelAndView mv) {
        //获取子系统列表
        mv.setViewName("systemSet/addRole");
        return mv;
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/role.do")
    public ResultResponse insert(Role role) {
        boolean result = roleService.insert(role);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑角色界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editRole.do")
    public ModelAndView editRole(ModelAndView mv, String id) {
        Role role = roleService.selectByPrimaryKey(id);
        mv.addObject("role", role);
        mv.setViewName("systemSet/editRole");
        return mv;
    }

    /**
     * 授权界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/accreditRole.do")
    public ModelAndView accreditRole(ModelAndView mv, String id) {
        Role role = roleService.selectByPrimaryKey(id);
        //获取子系统列表
        List<SubSystemInfo> sysList = permissionService.getSubSystemInfo();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (SubSystemInfo sys : sysList) {
            Map<String, Object> sysMap = new HashMap<>();
            sysMap.put("checked", permissionService.thisRoleHavePer(id, sys.getSubSystemId()));
            sysMap.put("name", sys.getSubSystemName());
            sysMap.put("open", "true");
            sysMap.put("id", sys.getSubSystemId());
            List<Map<String, Object>> mapList = permissionService.getMenuByParentId(sys.getSubSystemId());
            for (Map<String, Object> objectMap : mapList) {
                String pid = (String) objectMap.get("id");
                objectMap.put("checked", permissionService.thisRoleHavePer(id, pid));
            }
            sysMap.put("children", mapList);
            if (sys.getTeamId().equals("0")) {
                resultList.add(sysMap);
            }

        }
        mv.addObject("sysList", resultList);
        mv.addObject("role", role);
        mv.setViewName("systemSet/accreditRole");
        return mv;
    }

    /**
     * 编辑角色
     *
     * @param role
     * @return
     */
    @ResponseBody
    @PutMapping("/role.do")
    public ResultResponse update(Role role) {
        boolean result = roleService.updateByPrimaryKey(role);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 角色授权
     *
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/accredit.do")
    public ResultResponse accredit(Role role) {
        boolean result = roleService.accreditByPrimaryKey(role);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/role.do")
    public ResultResponse delete(String id) {
        //删除角色前需要判断该角色下是否存在用户
        boolean haveUserInRole = userService.haveUserInRole(id);
        if (!haveUserInRole) {
            return Result.resuleError("该角色下存在用户,无法删除!");
        }
        boolean result = roleService.deleteByPrimaryKey(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}