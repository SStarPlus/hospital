package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 */
@Data
@TableName("gm_case")
public class Case implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    private String uuId;
    /**
     * 身份证号
     */
    private String userName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private String age;
    /**
     * 性别
     */
    private String sex;
    /**
     * 主述
     */
    private String selfSay;
    /**
     * 诊断结果
     */
    private String consequence;
    /**
     * 治疗方案
     */
    private String scheme;
    /**
     * 医生
     */
    private String doctorName;
    private String doctorId;
    /**
     * 就诊时间
     */
    private String time;

}