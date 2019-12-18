package com.delcache.common.dao.impl;

import com.delcache.common.dao.BaseDao;
import com.delcache.common.entity.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class BaseDaoImpl  implements BaseDao {

    @Autowired
    SessionFactory sessionFactory;

    public Object find(String hql) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createQuery(hql).setMaxResults(1);
        return query.uniqueResult();
    }

    public Object find(String sql, Class clazz) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createNativeQuery(sql, clazz);
        return query.uniqueResult();
    }

    public Object findAll(String hql, int firstRow, int listRow) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createQuery(hql);
        if (firstRow > 0) {
            query = query.setFirstResult(firstRow);
        }
        if (listRow > 0) {
            query = query.setMaxResults(listRow);
        }
        return query.list();
    }

    public Object findAll(String sql, Class clazz, int firstRow, int listRow) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createNativeQuery(sql, clazz);
        if (firstRow > 0) {
            query = query.setFirstResult(firstRow);
        }
        if (listRow > 0) {
            query = query.setMaxResults(listRow);
        }
        return query.list();
    }

    public Integer count(String hql) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createQuery(hql);
        return ((Number) query.uniqueResult()).intValue();
    }

    @Transactional
    public void save(BaseEntity t) {
        Session factory = sessionFactory.getCurrentSession();
        factory.saveOrUpdate(t);
    }

    @Transactional
    public int update(String hql) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createQuery(hql);
        return query.executeUpdate();
    }

    @Transactional
    public void multiInsert(List entities) {
        Session factory = sessionFactory.getCurrentSession();
        for (int i = 0; i < entities.size(); i++) {
            factory.save(entities.get(i)); // 保存对象
            // 批插入的对象立即写入数据库并释放内存
            if (i % 20 == 0) {
                factory.flush();
                factory.clear();
            }
        }
    }

    @Transactional
    public void delete(String hql) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createQuery(hql);
        query.executeUpdate();
    }

    public int updateSql(String sql) {
        Session factory = sessionFactory.getCurrentSession();
        Query query = factory.createNativeQuery(sql);
        return query.executeUpdate();
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
}
