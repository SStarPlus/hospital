package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Office;

import java.util.List;
import java.util.Set;

public interface OfficeService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Office> selectPage(Office entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Office entity);

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
    boolean edit(Office entity);

    /**
     * 单个对象
     *
     * @param id
     * @return
     */
    Office getOne(String id);

    /**
     * 集合
     *
     * @param
     * @return
     */
    List<Office> getList();
    Set<Office> getListdeWeight();
}
