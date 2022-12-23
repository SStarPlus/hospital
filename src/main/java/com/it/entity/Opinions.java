package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 意见建议实体类
 */
@Data
@TableName("gm_opinions")
public class Opinions implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 留言用户
     */
    private String userId;
    private String userName;
    private String imgUrl;
    /**
     * 时间
     */
    private String time;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private String state;


}