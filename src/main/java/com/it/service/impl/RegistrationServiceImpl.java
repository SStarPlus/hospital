package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Registration;
import com.it.mapper.RegistrationMapper;
import com.it.service.RegistrationService;
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
public class RegistrationServiceImpl implements RegistrationService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private RegistrationMapper RegistrationMapper;

    @Override
    public Page<Registration> selectPage(Registration entity, int page, int limit) {
        EntityWrapper<Registration> searchInfo = new EntityWrapper<>();
        Page<Registration> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDay())) {
            searchInfo.eq("day", entity.getDay());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorId())) {
            searchInfo.eq("doctorId", entity.getDoctorId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        searchInfo.orderBy("time desc");
        List<Registration> resultList = RegistrationMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Registration entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setName(itdragonUtils.getSessionRealName());
        entity.setState("0");
        Integer insert = RegistrationMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Registration entity) {
        Integer insert = RegistrationMapper.updateById(entity);
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
            result = RegistrationMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean cheack(String day, String doctorId, String userName) {
        EntityWrapper<Registration> wrapper = new EntityWrapper<>();
        wrapper.eq("userName", userName);
        wrapper.eq("doctorId", doctorId);
        wrapper.eq("day", day);
        List<Registration> resultList = RegistrationMapper.selectList(wrapper);
        if (resultList.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public List<Registration> getList(Registration entity) {
        EntityWrapper<Registration> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            wrapper.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            wrapper.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDay())) {
            wrapper.eq("day", entity.getDay());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getDoctorId())) {
            wrapper.eq("doctorId", entity.getDoctorId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            wrapper.between("time", entity.getTime().split(" - ")[0], entity.getTime().split(" - ")[1]);
        }
        wrapper.orderBy("time desc");
        List<Registration> resultList = RegistrationMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Registration getOne(String id) {
        return RegistrationMapper.selectById(id);
    }


}