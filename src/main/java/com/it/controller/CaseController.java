package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Case;
import com.it.entity.Registration;
import com.it.service.CaseService;
import com.it.service.RegistrationService;
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
 * 〈病例实列接口〉<br>
 *
 * @author
 */
@Controller
@RequestMapping("/case")
public class CaseController {
    @Autowired
    private CaseService CaseService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private RegistrationService registrationService;

    /**
     * 管理界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("case/index");
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
    @GetMapping("case.do")
    public TableResultResponse reloadTable(Case entity, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Case> pageInfo = CaseService.selectPage(entity, page, limit);
        for (Case record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("uuId", record.getUuId());
            resultMap.put("name", record.getName());
            resultMap.put("userName", record.getUserName());
            resultMap.put("age", record.getAge());
            resultMap.put("sex", record.getSex());
            resultMap.put("selfSay", record.getSelfSay());
            resultMap.put("scheme", record.getScheme());
            resultMap.put("consequence", record.getConsequence());
            resultMap.put("doctorName", record.getDoctorName());
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
    public ModelAndView addPage(ModelAndView mv, String regId) {
        Registration registration = registrationService.getOne(regId);
        mv.addObject("registration", registration);
        mv.setViewName("case/addPage");
        return mv;
    }

    /**
     * 新增数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/case.do")
    public ResultResponse insert(Case entity, String regId) {
        boolean result = CaseService.insert(entity);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        //成功后修改预约挂号的状态信息
        Registration registration = registrationService.getOne(regId);
        registration.setState("1");
        registrationService.editById(registration);
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
        Case Case = CaseService.getOne(id);
        mv.addObject("case", Case);
        mv.setViewName("case/editPage");
        return mv;
    }

    /**
     * 编辑数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/case.do")
    public ResultResponse edit(Case entity) {
        boolean result = CaseService.editById(entity);
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
    @DeleteMapping("/case.do")
    public ResultResponse delete(String id) {
        boolean result = CaseService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

}