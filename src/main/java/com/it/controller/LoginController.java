package com.it.controller;

import com.it.entity.User;
import com.it.service.LogService;
import com.it.service.UserService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 描述：〈用户登入登出接口/控制器〉
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private LogService logService;
    private static final transient Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String ROLE_ID = "035e6cd6738c42e5a4112d34e85e0832";//初始用户角色Id

    /**
     * 用户登录操作
     *
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("login.do")
    public ResultResponse login(String userName, String password) {
        log.info("^^^^^^^^^^^^^^^^^^^^  登录信息:用户名: " + userName + "密码: " + password);
        try {
            Subject currentUser = SecurityUtils.getSubject();
            log.info("^^^^^^^^^^^^^^^^^^^^  是否已通过认证" + currentUser.isAuthenticated());
            //根据前台页面输入的用户名查询用户信息
            User user = userService.getUserByUserName(userName);
            if (user == null) {
                //用户查询出来的user对面为null(空)
                return Result.resuleError("用户不存在");
            } else {
                if (ROLE_ID.equals(user.getRoleId())) {
                    return Result.resuleError("无后台操作权限!");
                }
                user.setPlainPassword(password);
                //如果查询出来了则判断用户禁用启用的状态(0,1)
                if (user.getStatus() == 1) {
                    //状态为1则是启用状态执行后续操作,判断当前系统环境是否有登录过的用户信息
                    if (!currentUser.isAuthenticated()) {
                        //没有则执行登录操作
                        log.info("^^^^^^^^^^^^^^^^^^^^  执行登录操作");
                        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                        try {
                            // 执行登录
                            currentUser.login(token);
                            log.info("^^^^^^^^^^^^^^^^^^^^  登录成功");
                            //日志记录
                            //将登陆信息存储到session中
                            User loginUserInfo = new User();
                            //获取当前时间
                            String loginTime = DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                            log.info("^^^^^^^^^^^^^^^^^^^^ 登陆时间为" + loginTime);
                            loginUserInfo.setUserName(userName);
                            loginUserInfo.setPlainPassword(password);
                            itdragonUtils.getShiroSession().setAttribute("loginUserInfo", loginUserInfo);
                            itdragonUtils.getShiroSession().setAttribute("userInfo", user);
                            //修改登陆时间
                            userService.updateLoginTime(loginUserInfo.getUserName(), loginTime);
                            logService.insert("登录操作");
                            return Result.resuleSuccess();
                        } catch (AuthenticationException ae) {
                            log.info("^^^^^^^^^^^^^^^^^^^^  登录失败,账号密码不匹配: " + ae.getMessage());
                            return Result.resuleError("账号密码不匹配");
                        }
                    } else {
                        //有登录过的用户信息返回给前台提示
                        return Result.resuleError("已存在登录用户信息,请注销当前登录用户的信息再进行登录操作!");
                    }
                } else if (user.getStatus() == 0) {
                    //状态为0则是仅用状态,返回给前台信息提示
                    return Result.resuleError("当前用户不可登录;请联系管理员!");
                }
            }
            //如果上述过程都未被捕捉运行则出现无效操组提示
            return Result.resuleError("无效操作!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.resuleError(e.getMessage());
        }

    }

    /**
     * 前台用户登录操作
     *
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("loginPro.do")
    public ResultResponse loginPro(String userName, String password) {
        log.info("^^^^^^^^^^^^^^^^^^^^  登录信息:用户名: " + userName + "密码: " + password);
        try {
            Subject currentUser = SecurityUtils.getSubject();
            log.info("^^^^^^^^^^^^^^^^^^^^  是否已通过认证" + currentUser.isAuthenticated());
            //根据前台页面输入的用户名查询用户信息
            User user = userService.getUserByUserName(userName);
            if (user == null) {
                //用户查询出来的user为null(空)
                return Result.resuleError("用户不存在");
            } else {
                user.setPlainPassword(password);
                //如果查询出来了则判断用户禁用启用的状态(0,1)
                if (user.getStatus() == 1) {
                    //状态为1则是启用状态执行后续操作,判断当前系统环境是否有登录过的用户信息
                    if (!currentUser.isAuthenticated()) {
                        //没有则执行登录操作
                        log.info("^^^^^^^^^^^^^^^^^^^^  执行登录操作");
                        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
                        try {
                            // 执行登录
                            currentUser.login(token);
                            log.info("^^^^^^^^^^^^^^^^^^^^  登录成功");
                            //日志记录
                            //将登陆信息存储到session中
                            User loginUserInfo = new User();
                            //获取当前时间
                            String loginTime = DateUtil.getNowDateSS();
                            log.info("^^^^^^^^^^^^^^^^^^^^ 登陆时间为" + loginTime);
                            loginUserInfo.setUserName(userName);
                            loginUserInfo.setPlainPassword(password);
                            itdragonUtils.getShiroSession().setAttribute("loginUserInfo", loginUserInfo);
                            itdragonUtils.getShiroSession().setAttribute("userInfo", user);
                            //修改登陆时间
                            userService.updateLoginTime(loginUserInfo.getUserName(), loginTime);
                            logService.insert("登录操作");
                            return Result.resuleSuccess();
                        } catch (AuthenticationException ae) {
                            log.info("^^^^^^^^^^^^^^^^^^^^  登录失败,账号密码不匹配: " + ae.getMessage());
                            return Result.resuleError("账号密码不匹配");
                        }
                    } else {
                        //有登录过的用户信息返回给前台提示
                        return Result.resuleError("已存在登录用户信息,请注销当前登录用户的信息再进行登录操作!");
                    }
                } else if (user.getStatus() == 0) {
                    //状态为0则是仅用状态,返回给前台信息提示
                    return Result.resuleError("当前用户不可登录;请联系管理员!");
                }
            }
            //如果上述过程都未被捕捉运行则出现无效操组提示
            return Result.resuleError("无效操作!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.resuleError(e.getMessage());
        }

    }

    /**
     * 登出
     *
     * @param session
     */
    @GetMapping("logout.do")
    public String logout(ModelAndView mv, HttpSession session) {
        try {
            //退出
            SecurityUtils.getSubject().logout();
            log.info("^^^^^^^^^^^^^^^^^^^^登出成功");


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/diplomaProject";

    }

    /**
     * 后台登出
     *
     * @param session
     */
    @GetMapping("logoutAdmin.do")
    public String logoutAdmin(ModelAndView mv, HttpSession session) {
        try {
            //退出
            SecurityUtils.getSubject().logout();
            log.info("^^^^^^^^^^^^^^^^^^^^登出成功");


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/backstageLogin";

    }
}