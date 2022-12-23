package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Office;
import com.it.entity.User;
import com.it.entity.Visit;
import com.it.service.OfficeService;
import com.it.service.UserService;
import com.it.service.VisitService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈出诊安排接口〉<br>
 */
@Controller
@RequestMapping("/visit")
public class VisitController {
    @Autowired
    private VisitService VisitService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    private static final String DOCTOR_USER = "267b5a862b534ffea61b72f90bdcf6cc";

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        //查询科室列表
        List<Office> officeList = officeService.getList();
        mv.addObject("officeList", officeList);
        mv.setViewName("visit/index");
        return mv;
    }

    /**
     * 管理界面列表
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("visit.do")
    public TableResultResponse getTables(Visit entity, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Visit> pageInfo = VisitService.selectPage(entity, page, limit);
        for (Visit record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("officeName", record.getOfficeName());
            resultMap.put("doctorName", record.getDoctorName());
            resultMap.put("doctorId", record.getDoctorId());
            resultMap.put("day", record.getDay());
            resultMap.put("type", record.getType());
            resultMap.put("week", record.getWeek());
            resultMap.put("number", record.getZum() - record.getNumber());
            resultMap.put("zum", record.getZum());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/visit.do")
    public ResultResponse delete(String id) {
        boolean result = VisitService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        List<Office> officeList = officeService.getList();
        mv.addObject("officeList", officeList);
        //查询出所有医生列表
        List<User> doctorList = userService.selectListByRole(DOCTOR_USER);
        mv.addObject("doctorList", doctorList);
        mv.setViewName("visit/addPage");
        return mv;
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/visit.do")
    public ResultResponse insert(Visit entity) {
        //首先验重==同一个科室,同一天,同一时间,同一个医生不能出现两次数据
        List<Visit> list = VisitService.getList(entity.getDay(), entity.getType(), entity.getDoctorId(), entity.getOfficeName());
        if (!list.isEmpty()) {
            return Result.resuleError("该医生已在该时段安排出诊!");
        }
        User doctor = userService.selectByPrimaryKey(entity.getDoctorId());
        Office office = officeService.getOne(entity.getOfficeId());
        entity.setDoctorName(doctor.getRealName() + "(" + doctor.getUserName() + ")");
        entity.setOfficeName(office.getName());
        boolean result = VisitService.insert(entity);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(ModelAndView mv, String id) {
        Visit visit = VisitService.getOne(id);
        mv.addObject("visit", visit);
        //只查询该医生同科室的医生
        List<User> doctorList = userService.selectList(visit.getOfficeId());
        mv.addObject("doctorList", doctorList);
        mv.setViewName("visit/editPage");
        return mv;
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/visit.do")
    public ResultResponse update(Visit entity) {
        //首先验重==同一个科室,同一天,同一时间,同一个医生不能出现两次数据
        List<Visit> list = VisitService.getList(entity.getDay(), entity.getType(), entity.getDoctorId(), entity.getOfficeName());
        if (!list.isEmpty()) {
            return Result.resuleError("该医生已在该时段安排出诊!");
        }
        User doctor = userService.selectByPrimaryKey(entity.getDoctorId());
        Office office = officeService.getOne(entity.getOfficeId());
        entity.setDoctorName(doctor.getRealName() + "(" + doctor.getUserName() + ")");
        entity.setOfficeName(office.getName());
        boolean result = VisitService.edit(entity);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }
}