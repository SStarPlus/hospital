package com.it.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.*;
import com.it.util.ItdragonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 前台相关操作接口
 *
 * @version:
 * @Description:
 */
@Controller
public class ProesceniumController {
    private static final String GENERAL_USER = "035e6cd6738c42e5a4112d34e85e0832";
    private static final String DOCTOR_USER = "267b5a862b534ffea61b72f90bdcf6cc";
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private LogService logService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private CaseService caseService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MessageService messageService;
    /**
     * 前台首页跳转(前台初始页面跳转接口)
     *
     * @param mv
     * @return
     */
    @GetMapping("/diplomaProject")
    public ModelAndView diplomaProject(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/index");
        //查询医生信息
        List<User> doctorList = userService.selectListByRole(DOCTOR_USER);
        for (User user : doctorList) {
            user.setOfficeName(officeService.getOne(user.getOfficeId()).getName());
        }
        mv.addObject("doctorList", doctorList);
        //查询资讯列表
        Article article = new Article();
        article.setType("最新公告");
        Page<Article> articlePage = articleService.selectPage(article, 1, 4);
        article.setType("省医快讯");
        Page<Article> articlePage1 = articleService.selectPage(article, 1, 4);
        mv.addObject("articleList", articlePage.getRecords());
        mv.addObject("articleList1", articlePage1.getRecords());
        return mv;
    }

    /**
     * 专科介绍
     */
    @RequestMapping("/officePage")
    public ModelAndView officePage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/officePage");
        return mv;
    }

    /**
     * 专科介绍详情
     */
    @RequestMapping("/officeDetailsPage")
    public ModelAndView officeDetailsPage(ModelAndView mv, String id) {
        CommonMethods(mv);
        Office office = officeService.getOne(id);
        mv.addObject("office", office);
        //查询该科室下的医生
        List<User> doctorList = userService.selectList(id);
        mv.addObject("doctorList", doctorList);
        mv.setViewName("proscenium/officeDetailsPage");
        return mv;
    }

    /**
     * 所有医生页面
     */
    @RequestMapping("/allDoctorPage")
    public ModelAndView allDoctorPage(ModelAndView mv) {
        CommonMethods(mv);
        //查询医生信息
        List<User> doctorList = userService.selectListByRole(DOCTOR_USER);
        for (User user : doctorList) {
            user.setOfficeName(officeService.getOne(user.getOfficeId()).getName());
        }
        mv.addObject("doctorList", doctorList);
        mv.setViewName("proscenium/allDoctorPage");
        return mv;
    }

    /**
     * 科室医生页面
     */
    @RequestMapping("/doctorPage")
    public ModelAndView allDoctorPage(ModelAndView mv, String officeId) {
        CommonMethods(mv);
        //查询医生信息
        List<User> doctorList = userService.selectList(officeId);
        for (User user : doctorList) {
            user.setOfficeName(officeService.getOne(user.getOfficeId()).getName());
        }
        mv.addObject("doctorList", doctorList);
        Office office = officeService.getOne(officeId);
        mv.addObject("office", office);
        mv.setViewName("proscenium/doctorPage");
        return mv;
    }

    /**
     * 医生详情页面
     */
    @RequestMapping("/doctorDetailsPage")
    public ModelAndView doctorDetailsPage(ModelAndView mv, String id) {
        CommonMethods(mv);
        User doctor = userService.selectByPrimaryKey(id);
        doctor.setOfficeName(officeService.getOne(doctor.getOfficeId()).getName());
        mv.addObject("doctor", doctor);
        List<String> dayBeginNow = itdragonUtils.get7DayBeginNow();
        List<Visit> visitList = visitService.getList("", "", id, "");
        String str = "";
        for (Visit visit : visitList) {
            str = str + visit.getDay() + visit.getType();
        }
        mv.addObject("dayBeginNow", dayBeginNow);
        mv.addObject("str", str);
        mv.setViewName("proscenium/doctorDetailsPage");
        return mv;
    }

    /**
     * 出诊页面
     */
    @RequestMapping("/visitPage")
    public ModelAndView visitPage(ModelAndView mv) {
        CommonMethods(mv);
        //查询出所有科室,并且去重
        Set<Office> listdeWeight = officeService.getListdeWeight();
        HashMap<String, Map<String, Map<String, List<Visit>>>> hashMap = new HashMap<>();
        for (Office office : listdeWeight) {
            List<String> dayBeginNow = itdragonUtils.get7DayBeginNow();
            Map<String, Map<String, List<Visit>>> resultMap = new LinkedHashMap<>();
            for (String day : dayBeginNow) {
                Map<String, List<Visit>> map = new HashMap<>();
                List<Visit> visitListAm = visitService.getList(day, "上午", "", office.getId());
                List<Visit> visitListPm = visitService.getList(day, "下午", "", office.getId());
                map.put("上午", visitListAm);
                map.put("下午", visitListPm);
                resultMap.put(day, map);
            }
            hashMap.put(office.getName(), resultMap);
        }

        mv.addObject("hashMap", hashMap);
        mv.setViewName("proscenium/visitPage");
        return mv;
    }

    /**
     * 科室出诊出诊页面
     */
    @RequestMapping("/officeVisitPage")
    public ModelAndView officeVisitPage(ModelAndView mv, String officeId) {
        CommonMethods(mv);
        List<String> dayBeginNow = itdragonUtils.get7DayBeginNow();
        Map<String, Map<String, List<Visit>>> resultMap = new LinkedHashMap<>();
        for (String day : dayBeginNow) {
            Map<String, List<Visit>> map = new HashMap<>();
            List<Visit> visitListAm = visitService.getList(day, "上午", "", officeId);
            List<Visit> visitListPm = visitService.getList(day, "下午", "", officeId);
            map.put("上午", visitListAm);
            map.put("下午", visitListPm);
            resultMap.put(day, map);
        }
        mv.addObject("resultMap", resultMap);
        Office office = officeService.getOne(officeId);
        mv.addObject("office", office);
        mv.setViewName("proscenium/officeVisitPage");
        return mv;
    }

    /**
     * 预约挂号界面
     */
    @RequestMapping("/appointmentReg")
    public ModelAndView appointmentReg(ModelAndView mv, String officeId, String doctorId, String type) {
        CommonMethods(mv);
        //查询当天至七天内的出诊安排
        List<Visit> visitList = visitService.getList(officeId, doctorId, type);
        mv.addObject("visitList", visitList);
        mv.addObject("officeId", officeId);
        mv.addObject("doctorId", doctorId);
        mv.addObject("type", type);
        mv.setViewName("proscenium/appointmentReg");
        return mv;
    }

    /**
     * 病例查询界面
     */
    @RequestMapping("/infoSearch")
    public ModelAndView infoSearch(ModelAndView mv, String userName) {
        CommonMethods(mv);
        Case aCase = caseService.getOneByUuId(userName);
        mv.addObject("userName", userName);
        mv.addObject("case", aCase);
        mv.setViewName("proscenium/infoSearch");
        return mv;
    }

    /**
     * 文章列表页面
     */
    @RequestMapping("/articlePage")
    public ModelAndView articlePage(ModelAndView mv, String type) {
        CommonMethods(mv);
        if (!ItdragonUtils.stringIsNotBlack(type)) {
            type = "省医快讯";
        }
        Article article = new Article();
        article.setType(type);
        List<Article> articleList = articleService.getList(article);
        mv.addObject("type", type);
        mv.addObject("articleList", articleList);
        mv.setViewName("proscenium/articlePage");
        return mv;
    }

    /**
     * 公告列表
     */
    @RequestMapping("/noticePage")
    public ModelAndView noticePage(ModelAndView mv) {
        CommonMethods(mv);
        Article article = new Article();
        article.setType("最新公告");
        List<Article> articleList = articleService.getList(article);
        mv.addObject("articleList", articleList);
        mv.setViewName("proscenium/noticePage");
        return mv;
    }

    /**
     * 文章详情页面
     */
    @RequestMapping("/articleDetailsPage")
    public ModelAndView articleDetailsPage(ModelAndView mv, String id) {
        CommonMethods(mv);
        Article article = articleService.getOne(id);
        mv.addObject("article", article);
        mv.setViewName("proscenium/articleDetailsPage");
        return mv;
    }

    /**
     * 在线留言界面
     */
    @RequestMapping("/opinionsPage")
    public ModelAndView opinionsPage(ModelAndView mv, String id) {
        CommonMethods(mv);
        mv.setViewName("proscenium/opinionPage");
        return mv;
    }

    /**
     * 前台用户注册页面跳转
     */
    @RequestMapping("/prosceniumReg")
    public ModelAndView prosceniumReg(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("prosceniumReg");
        return mv;
    }

    /**
     * 前台登录页面跳转
     */
    @RequestMapping("/loginShiro")
    public ModelAndView prosceniumLogin(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("prosceniumLogin");
        return mv;
    }


    /**
     * 忘记密码页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/prosceniumForgetPas")
    public ModelAndView prosceniumForgetPas(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("/prosceniumForgetPas");
        return mv;
    }

    /**
     * 用户中心
     *
     * @param mv
     * @return
     */
    @GetMapping("/userCenter")
    public ModelAndView userCenter(ModelAndView mv) {
        CommonMethods(mv);
        Registration registration = new Registration();
        registration.setUserName(itdragonUtils.getSessionUserName());
        List<Registration> registrationList = registrationService.getList(registration);
        mv.addObject("registrationList", registrationList);
        mv.setViewName("proscenium/userCenter");
        return mv;
    }

    /**
     * 用户信息
     *
     * @param mv
     * @return
     */
    @GetMapping("/userSet")
    public ModelAndView userSet(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/userSet");
        return mv;
    }

    /**
     * 安全设置
     *
     * @param mv
     * @return
     */
    @GetMapping("/safety")
    public ModelAndView safety(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/safety");
        return mv;
    }

    /**
     * 消息中心
     *
     * @param mv
     * @return
     */
    @GetMapping("/messagePage")
    public ModelAndView messagePage(ModelAndView mv) {
        CommonMethods(mv);
        //修改当前用户的消息列表状态
        messageService.updateState();
        //查询当前用户消息列表
        List<Message> messageList = messageService.getListNew(itdragonUtils.getSessionUserName());
        mv.addObject("messageList", messageList);
        mv.setViewName("proscenium/messagePage");
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
        //查询用户是否登录
        boolean gogin = itdragonUtils.isGogin();
        boolean is_general = false;
        boolean is_admin = false;
        boolean is_company = false;
        if (gogin) {
            //查询用户是否后台管理用户
            is_admin = itdragonUtils.getSessionUser().getRoleId().equals("1");
            is_company = itdragonUtils.getSessionUser().getRoleId().equals(DOCTOR_USER);
            is_general = itdragonUtils.getSessionUser().getRoleId().equals(GENERAL_USER);
            //当前登录用户
            User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
            mv.addObject("user", user);
        }
        mv.addObject("is_loin", gogin);
        mv.addObject("is_admin", is_admin);
        mv.addObject("is_general", is_general);
        mv.addObject("is_company", is_company);
        //查询出科室列表
        List<Office> officeList = officeService.getList();
        mv.addObject("officeList", officeList);
    }
}