package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 出诊安排实体类
 */
@Data
@TableName("gm_visit")
public class Visit implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 日期
     */
    private String day;
    /**
     * 星期
     */
    private String week;
    /**
     * 全天类型
     */
    private String type;
    /**
     * 所属科室
     */
    private String officeName;
    private String officeId;
    /**
     * 出诊医生
     */
    private String doctorName;
    private String doctorId;
    /**
     * 剩余挂号数
     */
    private Integer number;
    /**
     * 总挂号数
     */
    private Integer zum;
}