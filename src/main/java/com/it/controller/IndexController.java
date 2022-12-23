package com.it.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Log;
import com.it.entity.Menu;
import com.it.entity.User;
import com.it.entity.WbeParameter;
import com.it.service.LogService;
import com.it.service.PermissionService;
import com.it.service.UserService;
import com.it.service.WbeParameterService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * 后台相关操作接口(页面跳转,个人信息操作等)
 *
 * @version:
 * @Description:
 */
@Controller
public class IndexController {
    private static final transient Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private LogService logService;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 后台首页跳转(后台初始页面跳转接口)
     *
     * @param mv
     * @param model
     * @return
     */
    @GetMapping("/diplomaProjectAdmin")
    public ModelAndView diplomaProjectAdmin(ModelAndView mv, Model model) {
        CommonMethods(mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        mv.addObject("user", user);
        List<Menu> menuList = permissionService.getMenuInfoByUserId(user.getId());
        JSONArray jsonArray = new JSONArray();
        for (Menu menu : menuList) {
            JSONObject menuMap = new JSONObject();
            menuMap.put("id", menu.getMenuId());
            menuMap.put("pId", menu.getPerentMenuId());
            menuMap.put("name", menu.getMenuName());
            menuMap.put("icon", menu.getIcon());
            menuMap.put("file", menu.getMenuURL());
            jsonArray.add(menuMap);
        }
        mv.addObject("menuInfo", jsonArray);
        mv.setViewName("content/index");
        return mv;
    }
    /**
     * 后台注册页面跳转
     */
    @RequestMapping("/backstageReg")
    public ModelAndView reg(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("backstageReg");
        return mv;
    }

    /**
     * 登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/backstageLogin")
    public ModelAndView login(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("/login");
        return mv;
    }

    /**
     * 404页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/404")
    public ModelAndView silingsi(ModelAndView mv) {
        mv.setViewName("content/404");
        return mv;
    }

    /**
     * 修改密码页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/changePwd")
    public ModelAndView changePwd(ModelAndView mv) {
        User user = itdragonUtils.getSessionUser();
        mv.addObject("user", user);
        mv.setViewName("content/changePwd");
        return mv;
    }

    /**
     * 用户修改密码
     *
     * @param newPas
     * @param oldPas
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePas")
    public ResultResponse updatePassword(String newPas, String oldPas) {
        User user = itdragonUtils.getSessionUser();
        if (oldPas.equals(user.getPlainPassword())) {
            boolean result = userService.changePass(newPas, user.getUserName());
            if (result) {
                user.setPlainPassword(newPas);
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("修改失败");
            }

        }
        return Result.resuleError("原密码错误,请重新输入");


    }

    /**
     * 用户个人信息
     *
     * @param mv
     * @return
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("content/userInfo");
        return mv;
    }

    /**
     * 修改头像信息
     *
     * @param mv
     * @return
     */
    @GetMapping("/changeImg")
    public ModelAndView changeImg(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("content/changeImg");
        return mv;
    }

    /**
     * 忘记密码页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/forgetPas")
    public ModelAndView forgetPas(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("/forgetPas");
        return mv;
    }

    /**
     * 找回密码
     *
     * @param toEmail
     * @return
     */
    @ResponseBody
    @PostMapping("/retrievePass")
    public ResultResponse retrievePass(String toEmail, String userName) throws Exception {
        User userByUserName = userService.getUserByUserName(userName);
        if (!toEmail.equals(userByUserName.getEmail())) {
            return Result.resuleError("账号和邮箱不符!");
        }
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("1156326165@qq.com");
            message.setTo(toEmail);
            message.setSubject("找回密码");
            message.setText("您的密码是:" + userByUserName.getPassword());
            this.mailSender.send(mimeMessage);
            return Result.resuleSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.resuleError("发送失败");
        }
    }

    /**
     * 首页
     *
     * @param mv
     * @return
     */
    @GetMapping("/main")
    public ModelAndView main(ModelAndView mv) {
        CommonMethods(mv);
        Log log = new Log();
        log.setOperation("登录");
        Page<Log> logPage = logService.selectPage(log, 1, 10);
        mv.addObject("logList", logPage.getRecords());
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("content/main");
        return mv;
    }

    /**
     * 页面有一些公用需求在这里抽取出来
     *
     * @param mv
     */
    public void CommonMethods(ModelAndView mv) {
        //网站参数
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
    }
}
