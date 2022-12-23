package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Article> selectPage(Article entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Article entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Article entity);

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
    List<Article> getList(Article entity);



    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Article getOne(String id);

}
