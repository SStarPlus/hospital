package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     * 角色列表查询
     *
     * @param role
     * @return
     */
    Page<Role> selectPage(Role role,int page, int limit);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    Role selectByPrimaryKey(String id);

    /**
     * 新增
     *
     * @param role
     * @return
     */
    boolean insert(Role role);

    /**
     * 修改
     *
     * @param role
     * @return
     */
    boolean updateByPrimaryKey(Role role);
    boolean accreditByPrimaryKey(Role role);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean deleteByPrimaryKey(String id);


    /**
     * 得到所有角色,没有条件
     *
     * @return
     */
    List<Role> getRoleList();

}
