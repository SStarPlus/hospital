package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Visit;

import java.util.List;

public interface VisitService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Visit> selectPage(Visit entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Visit entity);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean edit(Visit entity);

    /**
     * 单个对象
     *
     * @param id
     * @return
     */
    Visit getOne(String id);


    /**
     * 集合
     *
     * @param
     * @return
     */
    List<Visit> getList(String day, String type, String doctorId, String officeId);
    List<Visit> getList(String officeId,String doctorId,String type);
}
