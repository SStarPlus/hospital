package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 科室实体类
 *
 * @author itdragon
 */
@Data
@TableName("gm_office")
public class Office implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 科室名称
     */
    private String name;
    /**
     * 简介
     */
    private String content;
    /**
     * 大图
     */
    private String bigImg;
    /**
     * 小图
     */
    private String img;
    /**
     * 科室电话
     */
    private String iphone;
    /**
     * 挂号所需费用
     */
    private Float price;
}