package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Registration;

import java.util.List;

public interface RegistrationService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Registration> selectPage(Registration entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Registration entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Registration entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);

    /**
     * 验重
     * @param day
     * @param doctorId
     * @param userName
     * @return
     */
    boolean cheack(String day,String doctorId,String userName);

    /**
     * 获取集合
     *
     * @param entity
     * @return
     */
    List<Registration> getList(Registration entity);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Registration getOne(String id);

}
