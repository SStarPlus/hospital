package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Actual;
import com.it.mapper.ActualMapper;
import com.it.service.ActualService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Service
public class ActualServiceImpl implements ActualService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private ActualMapper ActualMapper;

    @Override
    public Page<Actual> selectPage(Actual entity, int page, int limit) {
        EntityWrapper<Actual> searchInfo = new EntityWrapper<>();
        Page<Actual> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        searchInfo.orderBy("time desc");
        List<Actual> resultList = ActualMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Actual entity) {
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = ActualMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Actual entity) {
        Integer insert = ActualMapper.updateById(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = ActualMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Actual> getList(Actual entity) {
        EntityWrapper<Actual> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            wrapper.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            wrapper.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        wrapper.orderBy("time desc");
        List<Actual> resultList = ActualMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Actual getOne(String id) {
        return ActualMapper.selectById(id);
    }


}