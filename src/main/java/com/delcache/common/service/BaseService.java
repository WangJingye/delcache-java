package com.delcache.common.service;

import com.delcache.common.dao.BaseDao;
import com.delcache.extend.Db;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Transactional
public class BaseService {
    @Autowired
    BaseDao dao;

    public Db db;

    public Map<String, Object> pagination(Db selector, Map<String, Object> params) {
        if (!StringUtils.isEmpty(params.get("page"))) {
            selector.page = Integer.parseInt(params.get("page").toString());
        }
        if (!StringUtils.isEmpty(params.get("pageSize"))) {
            selector.pageSize = Integer.parseInt(params.get("pageSize").toString());
        }
        selector.firstRow = (selector.page - 1) * selector.pageSize;
        selector.listRow = selector.pageSize;
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> pagination = new HashMap<>();
        int total = selector.count();
        pagination.put("total", total);
        pagination.put("pageSize", selector.pageSize);
        pagination.put("totalPage", (int) Math.ceil((double) total / selector.pageSize));
        pagination.put("current", selector.page);
        result.put("list", selector.findAll());
        result.put("pagination", pagination);
        return result;
    }

    public SessionFactory getSessionFactory() {
        return dao.getSessionFactory();
    }
}
