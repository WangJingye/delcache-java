package com.delcache.common.service;

import com.delcache.common.dao.BaseDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BaseService {

    @Autowired
    BaseDao dao;

    public SessionFactory getSessionFactory() {
        return dao.getSessionFactory();
    }
}
