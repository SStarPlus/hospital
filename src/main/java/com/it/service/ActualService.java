package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Actual;

import java.util.List;

public interface ActualService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Actual> selectPage(Actual entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Actual entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Actual entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);

    /**
     * 获取集合
     *
     * @param entity
     * @return
     */
    List<Actual> getList(Actual entity);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Actual getOne(String id);

}
