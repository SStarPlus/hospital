package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Opinions;
import com.it.service.OpinionsService;
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
public class OpinionsServiceImpl implements OpinionsService {
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private com.it.mapper.OpinionsMapper OpinionsMapper;


    @Override
    public Page<Opinions> selectPage(Opinions entity, int page, int limit) {
        EntityWrapper<Opinions> searchInfo = new EntityWrapper<>();
        Page<Opinions> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.like("userName", entity.getUserName());
        }
        searchInfo.orderBy("time desc");
        List<Opinions> resultList = OpinionsMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Opinions entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setUserId(itdragonUtils.getSessionUserId());
        entity.setImgUrl(itdragonUtils.getSessionUserImg());
        entity.setState("0");
        Integer insert = OpinionsMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Opinions entity) {
        Integer insert = OpinionsMapper.updateById(entity);
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
            result = OpinionsMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Opinions> getList(Opinions entity) {
        EntityWrapper<Opinions> wrapper = new EntityWrapper<>();
        List<Opinions> resultList = OpinionsMapper.selectList(wrapper);
        return resultList;
    }


    @Override
    public Opinions getOne(String id) {
        Opinions Opinions = OpinionsMapper.selectById(id);
        if (Opinions != null) {
            return Opinions;
        } else {
            return new Opinions();
        }
    }
}