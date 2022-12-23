package com.it.service;

import com.it.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Message entity);

    /**
     * 删除消息
     *
     * @param receiveUser
     * @return
     */
    boolean delete(String receiveUser);
    boolean deleteById(String id);
    void updateState();


    /**
     * 查询消息记录
     *
     * @return
     */
    List<Message> getListNew(String receiveUser);

    /**
     * 查询当前用户未读消息
     * @param
     * @return
     */
    List<Message> getListNew();


}
