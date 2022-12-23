package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Role;
import com.it.entity.User;
import com.it.mapper.PermissionMapper;
import com.it.mapper.RoleMapper;
import com.it.mapper.UserMapper;
import com.it.service.UserService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户service实现类
 *
 * @author
 */
@Service
public class UserServiceImpl implements UserService {
    private static final String ROLE_ID = "035e6cd6738c42e5a4112d34e85e0832";//初始用户角色Id
    private static final String USER_IMAGE = "/image/default.jpg";//初始用户头像
    private static final int USER_NO_STATUS = 1;//初始用户状态

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public User getUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        User user = userMapper.selectOne(searchUser);
        if (user != null) {
            //如果用户对象不为空将用户角色名赋值到用户对象中
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                user.setRoleName(role.getRole());
            }
            return user;
        }
        return null;
    }

    @Override
    public Page<User> getUserList(User user, int page, int limit) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        if (user != null) {
            if (ItdragonUtils.stringIsNotBlack(user.getUserName())) {
                searchInfo.like("userName", user.getUserName());
            }
            if (ItdragonUtils.stringIsNotBlack(user.getRealName())) {
                searchInfo.like("realName", user.getRealName());
            }
            if (ItdragonUtils.stringIsNotBlack(user.getUserName())) {
                searchInfo.like("iphone", user.getIphone());
            }
            if (user.getStatus() != null) {
                searchInfo.eq("status", user.getStatus());
            }
            if (ItdragonUtils.stringIsNotBlack(user.getRoleId())) {
                searchInfo.eq("roleId", user.getRoleId());
            }
        }
        searchInfo.orderBy("createdDate desc");
        searchInfo.ne("id", "1");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        if (!userList.isEmpty()) {
            //如果集合不为空循环用户对象将用户角色名赋值到用户对象中
            for (User user1 : userList) {
                Role role = roleMapper.selectById(user1.getRoleId());
                if (role != null) {
                    user1.setRoleName(role.getRole());
                }
            }
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }


    @Override
    public boolean changePass(String newPas, String userName) {
        User user = new User();
        user.setPassword(newPas);
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("userName", userName);
        if (ItdragonUtils.isEncrypted()) {
            user.setPlainPassword(newPas);
            itdragonUtils.entryptPassword(user);
        }
        int result = userMapper.update(user, entityWrapper);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateUserStatus(String id, Integer status) {
        User user = new User();
        user.setStatus(status);
        user.setId(id);
        int result = userMapper.updateById(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean insert(User user) {
        //判断前台用户是否传入用户状态
        if (user.getStatus() == null) {
            user.setStatus(USER_NO_STATUS);
        }
        if (!ItdragonUtils.stringIsNotBlack(user.getImgUrl())) {
            user.setImgUrl(USER_IMAGE);
        }
        //判断前台用户是否传入用户角色
        if (!ItdragonUtils.stringIsNotBlack(user.getRoleId())) {
            user.setRoleId(ROLE_ID);
        }
        user.setCreatedDate(DateUtil.getNowDateSS());
        if (ItdragonUtils.isEncrypted()) {
            itdragonUtils.entryptPassword(user);
        } else {
            user.setPassword(user.getPlainPassword());
        }
        int result = userMapper.insert(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateLoginTime(String userName, String time) {
        User user = new User();
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        user.setUpdatedDate(time);
        entityWrapper.eq("userName", userName);
        userMapper.update(user, entityWrapper);
    }

    @Override
    public List<User> selectList() {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        return userMapper.selectList(entityWrapper);
    }

    @Override
    public List<User> selectList(String officeId) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("officeId", officeId);
        return userMapper.selectList(entityWrapper);

    }

    @Override
    public List<User> selectListByRole(String roleId) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("roleId", roleId);
        return userMapper.selectList(entityWrapper);

    }

    @Override
    public boolean haveUserInRole(String roleId) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        if (ItdragonUtils.stringIsNotBlack(roleId)) {
            entityWrapper.eq("roleId", roleId);
        }
        List<User> userList = userMapper.selectList(entityWrapper);
        return userList.isEmpty();
    }

    @Override
    public boolean checkEmail(String email) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        if (ItdragonUtils.stringIsNotBlack(email)) {
            entityWrapper.eq("email", email);
        }
        List<User> userList = userMapper.selectList(entityWrapper);
        return userList.isEmpty();
    }

    @Override
    public boolean checkIphone(String iphone) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        if (ItdragonUtils.stringIsNotBlack(iphone)) {
            entityWrapper.eq("iphone", iphone);
        }
        List<User> userList = userMapper.selectList(entityWrapper);
        return userList.isEmpty();
    }

    @Override
    public String zumNumber() {
        return userMapper.selectList(null).size() + "";
    }

    @Override
    public boolean deleteByPrimaryKey(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = userMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User selectByPrimaryKey(String id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            //如果用户对象不为空将用户角色名赋值到用户对象中
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                user.setRoleName(role.getRole());
            }
            return user;
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKey(User user) {
        //判断前台用户是否传入用户状态
        if (user.getStatus() == null) {
            user.setStatus(USER_NO_STATUS);
        }
        if (ItdragonUtils.isEncrypted()) {
            itdragonUtils.entryptPassword(user);
        } else {
            user.setPassword(user.getPlainPassword());
        }
        int result = userMapper.updateById(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


}