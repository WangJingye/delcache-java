package com.delcache.common.dao;

import com.delcache.common.entity.BaseEntity;
import org.hibernate.SessionFactory;

import java.util.List;

public interface BaseDao {

    Object find(String hql);

    Object find(String sql, Class clazz);

    Object findAll(String hql, int firstRow, int listRow);

    Object findAll(String sql, Class clazz, int firstRow, int listRow);

    Integer count(String hql);

    void save(BaseEntity t);

    int update(String hql);

    void multiInsert(List entities);

    void delete(String hql);

    int updateSql(String sql);

    SessionFactory getSessionFactory();
}
