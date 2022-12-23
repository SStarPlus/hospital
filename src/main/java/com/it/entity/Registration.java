package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 挂号信息实体类
 */
@Data
@TableName("gm_registration")
public class Registration implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 身份证号
     */
    private String userName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 预约医生
     */
    private String doctorId;
    private String doctorName;
    /**
     * 预约时间
     */
    private String day;
    private String week;
    private String type;
    /**
     * 挂号时间
     */
    private String time;
    /**
     * 就诊状态
     */
    private String state;
    private String description;
}