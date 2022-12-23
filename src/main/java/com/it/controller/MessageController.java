package com.it.controller;

import com.it.entity.Message;
import com.it.service.MessageService;
import com.it.service.UserService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 描述：〈消息管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    private static final String GENERAL_USER = "035e6cd6738c42e5a4112d34e85e0832";//普通用户id
    @Autowired
    private MessageService messageService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;

    /**
     * 删除消息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/message.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = messageService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败!");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除全部消息
     *
     * @return
     */
    @PostMapping("/delAll.do")
    @ResponseBody
    public ResultResponse delAll() {
        boolean result = messageService.delete(itdragonUtils.getSessionUserName());
        if (!result) {
            return Result.resuleError("删除失败!");
        }
        return Result.resuleSuccess();
    }

    /**
     * 查询未读新消息
     *
     * @param id
     * @return
     */
    @PostMapping("/getNewMessage.do")
    @ResponseBody
    public ResultResponse getNewMessage(String id) {
        //查询当前用户是否有新消息
        List<Message> newList = messageService.getListNew();
        if (!newList.isEmpty()) {
            return Result.resuleSuccess(newList);
        }
        return Result.resuleError("暂无消息!");
    }
}