package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.it.entity.Message;
import com.it.mapper.MessageMapper;
import com.it.service.MessageService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息函数实现类
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private MessageMapper MessageMapper;

    @Override
    public boolean insert(Message entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setState("0");
        Integer insert = MessageMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String receiveUser) {
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("receiveUser", receiveUser);
        Integer delete = MessageMapper.delete(wrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        Integer delete = MessageMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updateState() {
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("receiveUser", itdragonUtils.getSessionUserName());
        Message message = new Message();
        message.setState("1");
        MessageMapper.update(message, wrapper);
    }


    @Override
    public List<Message> getListNew(String receiveUser) {
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("receiveUser", receiveUser);
        List<Message> list = MessageMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<Message> getListNew() {
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("receiveUser", itdragonUtils.getSessionUserName());
        wrapper.eq("state", "0");
        List<Message> list = MessageMapper.selectList(wrapper);
        return list;

    }


}