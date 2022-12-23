package com.it.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.it.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
/**
 * 角色数据访问层
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过角色Id查询到角色用户关系表中的permissionId集合
     *
     * @param roleId
     * @return
     */
    List<String> slectPermissionAndUser(@Param(value = "roleId") String roleId);

    /**
     * 删除角色下的用户
     *
     * @param userId
     * @param roleId
     * @return
     */
    int delUser(@Param(value = "userId") String userId, @Param(value = "roleId") String roleId);


    int delByRoleId(@Param(value = "roleId") String roleId);
}
