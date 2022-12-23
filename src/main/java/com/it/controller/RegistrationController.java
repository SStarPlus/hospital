package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Registration;
import com.it.entity.Visit;
import com.it.service.RegistrationService;
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
 * 〈预约挂号实列接口〉<br>
 *
 * @author
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService RegistrationService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private VisitService visitService;

    /**
     * 管理界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("registration/index");
        return mv;
    }

    /**
     * 管理界面列表数据异步加载接口
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("registration.do")
    public TableResultResponse reloadTable(Registration entity, int page, int limit) {
        entity.setDoctorId(itdragonUtils.getSessionUserId());
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Registration> pageInfo = RegistrationService.selectPage(entity, page, limit);
        for (Registration record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("userName", record.getUserName());
            resultMap.put("doctorName", record.getDoctorName());
            resultMap.put("description", record.getDescription());
            resultMap.put("doctorId", record.getDoctorId());
            resultMap.put("day", record.getDay());
            resultMap.put("week", record.getWeek());
            resultMap.put("type", record.getType());
            resultMap.put("state", record.getState());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv, String visitId) {
        mv.addObject("visitId", visitId);
        mv.setViewName("registration/addPage");
        return mv;
    }

    /**
     * 新增数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/registration.do")
    public ResultResponse insert(Registration entity, String visitId) {
        Visit visit = visitService.getOne(visitId);
        //挂号数验证
        if (visit.getNumber() >= visit.getZum()) {
            return Result.resuleError("该医生已无可预约挂号位置!");
        }
        //验重(同样的个人信息，当天不能重复预约同一位专家)
        boolean cheack = RegistrationService.cheack(visit.getDay(), visit.getDoctorId(), itdragonUtils.getSessionUserName());
        if (cheack) {
            return Result.resuleError("同样的个人信息，当天不能重复预约同一位专家!");
        }
        entity.setDay(visit.getDay());
        entity.setDoctorId(visit.getDoctorId());
        entity.setDoctorName(visit.getDoctorName());
        entity.setWeek(visit.getWeek());
        entity.setType(visit.getType());
        boolean result = RegistrationService.insert(entity);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        //成功后修改该出诊安排的挂号数
        visit.setNumber(visit.getNumber() + 1);
        visitService.edit(visit);
        return Result.resuleSuccess();
    }

    /**
     * 编辑界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(ModelAndView mv, String id) {
        Registration registration = RegistrationService.getOne(id);
        mv.addObject("registration", registration);
        mv.setViewName("registration/editPage");
        return mv;
    }

    /**
     * 编辑数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/registration.do")
    public ResultResponse edit(Registration entity) {
        boolean result = RegistrationService.editById(entity);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除数据接口
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/registration.do")
    public ResultResponse delete(String id) {
        boolean result = RegistrationService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

}