package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Message;
import com.it.entity.Opinions;
import com.it.service.MessageService;
import com.it.service.UserService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：〈意见建议管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/opinions")
public class OpinionsController {
    @Autowired
    private com.it.service.OpinionsService OpinionsService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Resource
    private MessageService messageService;

    /**
     * 意见建议管理首页页面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/opinions/index");
        return mv;
    }

    /**
     * 异步加载公告信息列表
     *
     * @param opinions
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("opinions.do")
    public TableResultResponse reloadTable(int page, int limit, Opinions opinions) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Opinions> pageInfo = OpinionsService.selectPage(opinions, page, limit);
        for (Opinions record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("content", record.getContent());
            resultMap.put("userName", record.getUserName());
            resultMap.put("userId", record.getUserId());
            resultMap.put("imgUrl", record.getImgUrl());
            resultMap.put("state", record.getState());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增页面弹窗
     *
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("/opinions/addPage");
        return mv;
    }

    /**
     * 新增
     *
     * @param opinions
     * @return
     */
    @PostMapping("/opinions.do")
    @ResponseBody
    public ResultResponse insert(Opinions opinions) {
        boolean result = OpinionsService.insert(opinions);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 回复页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Opinions opinions = OpinionsService.getOne(id);
        mv.addObject("opinions", opinions);
        mv.setViewName("/opinions/editPage");
        return mv;
    }

    /**
     * 回复
     *
     * @param opinions
     * @return
     */
    @PutMapping("/opinions.do")
    @ResponseBody
    public ResultResponse edit(Opinions opinions, String reply) {
        boolean result = OpinionsService.editById(opinions);
        if (!result) {
            return Result.resuleError("回复失败");
        }
        //回复成功发送系统消息给用户
        Opinions opinions1 = OpinionsService.getOne(opinions.getId());
        Message message = new Message();
        message.setReceiveUser(opinions1.getUserName());
        message.setContent("系统消息:" + reply);
        messageService.insert(message);
        return Result.resuleSuccess();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/opinions.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = OpinionsService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

}