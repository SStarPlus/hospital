package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Case;

import java.util.List;

public interface CaseService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Case> selectPage(Case entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Case entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Case entity);

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
    List<Case> getList(Case entity);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Case getOne(String id);
    Case getOneByUuId(String uuId);

}
