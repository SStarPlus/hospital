package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Actual;
import com.it.service.ActualService;
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
 * 〈实列接口〉<br>
 */
@Controller
@RequestMapping("/actual")
public class ActualController {
    @Autowired
    private ActualService actualService;
    @Autowired
    private ItdragonUtils itdragonUtils;

    /**
     * 管理界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("actual/index");
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
    @GetMapping("actual.do")
    public TableResultResponse reloadTable(Actual entity, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Actual> pageInfo = actualService.selectPage(entity, page, limit);
        for (Actual record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
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
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("actual/addPage");
        return mv;
    }

    /**
     * 新增数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/actual.do")
    public ResultResponse insert(Actual entity) {
        boolean result = actualService.insert(entity);
        if (!result) {
            return Result.resuleError("新增失败");
        }
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
        Actual actual = actualService.getOne(id);
        mv.addObject("actual", actual);
        mv.setViewName("actual/editPage");
        return mv;
    }

    /**
     * 编辑数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/actual.do")
    public ResultResponse edit(Actual entity) {
        boolean result = actualService.editById(entity);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除数据接口
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/actual.do")
    public ResultResponse delete(String ids) {
        boolean result = actualService.deleteById(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 详情信息界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/details.do")
    public ModelAndView details(ModelAndView mv, String id) {
        Actual actual = actualService.getOne(id);
        mv.addObject("actual", actual);
        mv.setViewName("actual/details");
        return mv;
    }
}