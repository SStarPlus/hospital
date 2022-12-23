package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
@TableName("gm_user")
public class User implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 账号
     */
    private String userName;
    /**
     * 登录时的密码，不持久化到数据库
     */
    @TableField(exist = false)
    private String plainPassword;
    /**
     * 加密后的密码
     */
    private String password;
    /**
     * 用于加密的盐
     */
    private String salt;
    /**
     * 联系方式
     */
    private String iphone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户注册时间
     */
    private String createdDate;
    /**
     * 用户最后一次登录时间
     */
    private String updatedDate;
    /**
     * 用户角色id
     */
    private String roleId;
    /**
     * 用户角色名称
     */
    @TableField(exist = false)
    private String roleName;
    /**
     * 用户状态，0表示用户已删除
     */
    private Integer status;
    /**
     * 昵称/姓名
     */
    private String realName;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 性别
     */
    private String sex;
    /**
     * 用户头像
     */
    private String imgUrl;
    /**
     * 头衔
     */
    private String title;
    /**
     * 医生介绍
     */
    private String content;
    /**
     * 所属科室
     */
    private String officeId;
    @TableField(exist = false)
    private String officeName;


}