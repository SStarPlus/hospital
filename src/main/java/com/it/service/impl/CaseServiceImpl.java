package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Case;
import com.it.mapper.CaseMapper;
import com.it.service.CaseService;
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
public class CaseServiceImpl implements CaseService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private CaseMapper CaseMapper;

    @Override
    public Page<Case> selectPage(Case entity, int page, int limit) {
        EntityWrapper<Case> searchInfo = new EntityWrapper<>();
        Page<Case> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.like("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.like("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorName())) {
            searchInfo.eq("doctorName", entity.getDoctorName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUuId())) {
            searchInfo.like("uuId", entity.getUuId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        searchInfo.orderBy("time desc");
        List<Case> resultList = CaseMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Case entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setDoctorId(itdragonUtils.getSessionUserId());
        entity.setDoctorName(itdragonUtils.getSessionRealName());
        entity.setUuId(itdragonUtils.getOrderIdByUUId());
        Integer insert = CaseMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Case entity) {
        Integer insert = CaseMapper.updateById(entity);
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
            result = CaseMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Case> getList(Case entity) {
        EntityWrapper<Case> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            wrapper.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            wrapper.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorName())) {
            wrapper.eq("doctorName", entity.getDoctorName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            wrapper.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        wrapper.orderBy("time desc");
        List<Case> resultList = CaseMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Case getOne(String id) {
        return CaseMapper.selectById(id);
    }

    @Override
    public Case getOneByUuId(String uuId) {
        EntityWrapper<Case> wrapper = new EntityWrapper<>();
        wrapper.eq("userName", uuId);
        wrapper.orderBy("time desc");
        List<Case> resultList = CaseMapper.selectList(wrapper);
        if(resultList.isEmpty()){
            return null;
        }
        return resultList.get(0);
    }


}