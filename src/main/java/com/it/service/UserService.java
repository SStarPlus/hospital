package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 通过UserName查询到User对象
     *
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 通过User对象查找列表
     *
     * @param user
     * @param page
     * @param limit
     * @return
     */
    Page<User> getUserList(User user, int page, int limit);

    /**
     * 修改密码
     *
     * @param newPas
     * @return
     */
    boolean changePass(String newPas, String userName);

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    boolean updateUserStatus(String id, Integer status);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    User selectByPrimaryKey(String id);

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    boolean insert(User user);

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    boolean updateByPrimaryKey(User user);

    /**
     * 删除,单个删除批量删除通用
     *
     * @param ids
     * @return
     */
    boolean deleteByPrimaryKey(String ids);

    /**
     * 修改登陆时间
     *
     * @param userName
     * @param time
     * @return
     */
    void updateLoginTime(String userName, String time);

    /**
     * 获取用户集合
     *
     * @return
     */
    List<User> selectList();
    List<User> selectList(String officeId);
    List<User> selectListByRole(String roleId);

    boolean haveUserInRole(String roleId);

    /**
     * 验证电子邮件
     *
     * @param email
     * @return
     */
    boolean checkEmail(String email);
    /**
     * 验证手机
     *
     * @param iphone
     * @return
     */
    boolean checkIphone(String iphone);

    /**
     * 统计用户总户数
     */
    String zumNumber();
}
