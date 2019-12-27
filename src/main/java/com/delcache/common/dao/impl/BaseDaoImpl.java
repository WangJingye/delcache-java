package com.delcache.common.dao.impl;

import com.delcache.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;

@Repository
@Transactional
public class BaseDaoImpl implements BaseDao {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public int insert(String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int delete(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    //批量插入
    public void batchInsert(String sql) {
        jdbcTemplate.batchUpdate(sql);
    }

    public Object findAll(String sql, Class clazz) {
        //返回是map
        if ("Map".equals(clazz.getSimpleName()) || "HashMap".equals(clazz.getSimpleName())) {
            return jdbcTemplate.queryForList(sql);
        }
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(clazz));
    }

    public Object find(String sql, Class clazz) {
        try {
            //返回是map
            if ("Map".equals(clazz.getSimpleName()) || "HashMap".equals(clazz.getSimpleName())) {
                return jdbcTemplate.queryForMap(sql);
            }
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(clazz));
        } catch (Exception e) {
            return null;
        }
    }

    public int count(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
