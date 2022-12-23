package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Visit;
import com.it.mapper.VisitMapper;
import com.it.service.VisitService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈出诊安排业务逻辑层〉<br>
 */
@Service
public class VisitServiceImpl implements VisitService {
    @Resource
    private VisitMapper VisitMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public Page<Visit> selectPage(Visit entity, int page, int limit) {
        EntityWrapper<Visit> searchInfo = new EntityWrapper<>();
        Page<Visit> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getOfficeName())) {
            searchInfo.eq("officeName", entity.getOfficeName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getOfficeId())) {
            searchInfo.eq("officeId", entity.getOfficeId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorName())) {
            searchInfo.like("doctorName", entity.getDoctorName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorId())) {
            searchInfo.eq("doctorId", entity.getDoctorId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getType())) {
            searchInfo.eq("type", entity.getType());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getWeek())) {
            searchInfo.eq("week", entity.getWeek());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDay())) {
            searchInfo.eq("day", entity.getDay());
        }
        searchInfo.orderBy("day desc");
        List<Visit> resultList = VisitMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Visit entity) {
        //根据获取的日期得到星期
        String week = DateUtil.dateToWeek(entity.getDay());
        entity.setWeek(week);
        entity.setNumber(0);
        Integer insert = VisitMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = VisitMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(Visit entity) {
        //根据获取的日期得到星期
        String week = DateUtil.dateToWeek(entity.getDay());
        entity.setWeek(week);
        Integer update = VisitMapper.updateById(entity);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Visit getOne(String id) {
        return VisitMapper.selectById(id);
    }

    @Override
    public List<Visit> getList(String day, String type, String doctorId, String officeId) {
        EntityWrapper<Visit> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(day)) {
            wrapper.eq("day", day);
        }
        if (ItdragonUtils.stringIsNotBlack(doctorId)) {
            wrapper.eq("doctorId", doctorId);
        }
        if (ItdragonUtils.stringIsNotBlack(type)) {
            wrapper.eq("type", type);
        }
        if (ItdragonUtils.stringIsNotBlack(officeId)) {
            wrapper.eq("officeId", officeId);
        }
        wrapper.orderBy("day asc");
        List<Visit> selectList = VisitMapper.selectList(wrapper);
        return selectList;
    }

    @Override
    public List<Visit> getList(String officeId, String doctorId, String type) {
        EntityWrapper<Visit> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(officeId)) {
            wrapper.eq("officeId", officeId);
        }
        if (ItdragonUtils.stringIsNotBlack(doctorId)) {
            wrapper.eq("doctorId", doctorId);
        }
        if (ItdragonUtils.stringIsNotBlack(type)) {
            wrapper.eq("type", type);
        }
        wrapper.between("day",DateUtil.getNowDate(),DateUtil.addDay(6, "yyyy-MM-dd"));
        wrapper.orderBy("day asc");
        List<Visit> selectList = VisitMapper.selectList(wrapper);
        return selectList;
    }
}