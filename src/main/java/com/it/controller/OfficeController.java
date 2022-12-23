package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Office;
import com.it.service.OfficeService;
import com.it.service.UserService;
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
 * 〈科室管理接口相关〉<br>
 */
@Controller
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private OfficeService OfficeService;
    @Autowired
    private UserService userService;

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("office/index");
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
    @GetMapping("office.do")
    public TableResultResponse getTables(Office entity, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Office> pageInfo = OfficeService.selectPage(entity, page, limit);
        for (Office record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("price", record.getPrice());
            resultMap.put("iphone", record.getIphone());
            resultMap.put("content", record.getContent());
            resultMap.put("img", record.getImg());
            resultMap.put("bigImg", record.getBigImg());
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
    @DeleteMapping("/office.do")
    public ResultResponse delete(String id) {
        boolean result = OfficeService.delById(id);
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
        mv.setViewName("office/addPage");
        return mv;
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/office.do")
    public ResultResponse insert(Office entity) {
        boolean result = OfficeService.insert(entity);
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
        Office office = OfficeService.getOne(id);
        mv.addObject("office", office);
        mv.setViewName("office/editPage");
        return mv;
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/office.do")
    public ResultResponse update(Office entity) {
        boolean result = OfficeService.edit(entity);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }
}