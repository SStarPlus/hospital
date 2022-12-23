package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Office;
import com.it.mapper.OfficeMapper;
import com.it.service.OfficeService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 〈科室service实现类〉<br>
 */
@Service
public class OfiicesServiceImpl implements OfficeService {
    @Resource
    private OfficeMapper OfficeMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public Page<Office> selectPage(Office entity, int page, int limit) {
        EntityWrapper<Office> searchInfo = new EntityWrapper<>();
        Page<Office> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.eq("name", entity.getName());
        }
        List<Office> resultList = OfficeMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Office entity) {
        Integer insert = OfficeMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = OfficeMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(Office entity) {
        Integer update = OfficeMapper.updateById(entity);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Office getOne(String id) {
        Office office = OfficeMapper.selectById(id);
        if (office != null) {
            return office;
        }
        return new Office();

    }

    @Override
    public List<Office> getList() {
        EntityWrapper<Office> wrapper = new EntityWrapper<>();
        List<Office> selectList = OfficeMapper.selectList(wrapper);
        return selectList;
    }

    @Override
    public Set<Office> getListdeWeight() {
        EntityWrapper<Office> wrapper = new EntityWrapper<>();
        List<Office> selectList = OfficeMapper.selectList(wrapper);
        Set<Office> ts = new HashSet<Office>();
        ts.addAll(selectList);
        return ts;
    }
}