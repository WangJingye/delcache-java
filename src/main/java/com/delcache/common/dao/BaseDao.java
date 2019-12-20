package com.delcache.common.dao;

public interface BaseDao {

    int insert(String sql);

    int delete(String sql);

    int update(String sql);

    void batchInsert(String sql);

    Object findAll(String sql,Class clazz);

    Object find(String sql,Class clazz);

    int count(String sql);
}
