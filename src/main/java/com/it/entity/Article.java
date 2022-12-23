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
@TableName("gm_article")
public class Article implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类
     */
    private String type;
    /**
     * 预览图
     */
    private String img;
    /**
     * 时间
     */
    private String time;
    /**
     * 内容
     */
    private String content;
}