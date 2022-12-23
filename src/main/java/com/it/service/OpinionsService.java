package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Opinions;

import java.util.List;

public interface OpinionsService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Opinions> selectPage(Opinions entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Opinions entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Opinions entity);

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
    List<Opinions> getList(Opinions entity);


    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Opinions getOne(String id);
}
