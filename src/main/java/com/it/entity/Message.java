package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息实体类
 */
@Data
@TableName("gm_message")
public class Message implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 接收用户
     */
    private String receiveUser;
    /**
     * 内容
     */
    private String content;
    /**
     * 时间
     */
    private String time;
    /**
     * 已读未读状态
     */
    private String state;

}