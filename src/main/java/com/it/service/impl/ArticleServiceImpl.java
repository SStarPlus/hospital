package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Article;
import com.it.mapper.ArticleMapper;
import com.it.service.ArticleService;
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
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private ArticleMapper ArticleMapper;

    @Override
    public Page<Article> selectPage(Article entity, int page, int limit) {
        EntityWrapper<Article> searchInfo = new EntityWrapper<>();
        Page<Article> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getTitle())) {
            searchInfo.like("title", entity.getTitle());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getType())) {
            searchInfo.eq("type", entity.getType());
        }
        searchInfo.orderBy("time desc");
        List<Article> resultList = ArticleMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Article entity) {
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = ArticleMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Article entity) {
        Integer insert = ArticleMapper.updateById(entity);
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
            result = ArticleMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Article> getList(Article entity) {
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getType())) {
            wrapper.eq("type", entity.getType());
        }
        List<Article> resultList = ArticleMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Article getOne(String id) {
        Article Article = ArticleMapper.selectById(id);
        if (Article != null) {
            return Article;
        } else {
            return new Article();
        }
    }
}