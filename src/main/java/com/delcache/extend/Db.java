package com.delcache.extend;

import com.delcache.common.dao.BaseDao;
import com.delcache.common.entity.BaseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import javax.persistence.Table;
import java.util.*;

public class Db {

    BaseDao dao;

    public int page;
    public int pageSize;
    private Map<String, List<Object[]>> where;

    public String order;

    public String table;
    public Class clazz;

    public Integer listRow = 0;

    public Integer firstRow = 0;

    public static Db table(Class clazz) {
        Db db = new Db();
        db.dao = ContextLoader.getCurrentWebApplicationContext().getBeansOfType(BaseDao.class).get("baseDao");
        db.clazz = clazz;
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        db.table = annotation.name();
        db.where = new HashMap<>();
        db.order = "";
        db.listRow = db.firstRow = 0;
        db.page = 1;
        db.pageSize = 10;
        return db;
    }

    public Db where(String key, Object value, String restriction) {
        if (null == value) {
            this.where.remove(key);
        } else {
            List<Object[]> values = new ArrayList<>();
            if (this.where.containsKey(key)) {
                values = this.where.get(key);
            }
            values.add(new Object[]{value, restriction});
            this.where.put(key, values);
        }
        return this;
    }

    public Db where(String key, Object value) {
        if (null == value) {
            value = "";
        }
        List<Object[]> values = new ArrayList<>();
        if (this.where.containsKey(key)) {
            values = this.where.get(key);
        }
        values.add(new Object[]{value, "eq"});
        this.where.put(key, values);
        return this;
    }

    public String hql() {
        String sql = "from " + this.clazz.getName();
        return sql + this.whereSql();
    }

    public String whereSql() {
        List<String> conditionList = new ArrayList<>();
        List<String> conditionLi;
        for (Map.Entry<String, List<Object[]>> entry : this.where.entrySet()) {
            List<Object[]> valueList = entry.getValue();
            for (Object[] val : valueList) {
                String condition;
                switch (val[1].toString()) {
                    case "=":
                    case "eq":
                        conditionList.add(String.format("%s='%s'", entry.getKey(), val[0].toString()));
                        break;
                    case "!=":
                    case "neq":
                        conditionList.add(String.format("%s!='%s'", entry.getKey(), val[0].toString()));
                        break;
                    case "gt":
                        conditionList.add(String.format("%s>%s", entry.getKey(), Integer.parseInt(val[0].toString())));
                        break;
                    case "lt":
                        conditionList.add(String.format("%s<%s", entry.getKey(), Integer.parseInt(val[0].toString())));
                        break;
                    case "egt":
                        conditionList.add(String.format("%s>=%s", entry.getKey(), Integer.parseInt(val[0].toString())));
                        break;
                    case "elt":
                        conditionList.add(String.format("%s<=%s", entry.getKey(), Integer.parseInt(val[0].toString())));
                        break;
                    case "in":
                        conditionLi = (List<String>) val[0];
                        condition = String.join("','", String.join("','", conditionLi.toArray(new String[conditionLi.size()])));
                        conditionList.add(String.format("%s in ('%s')", entry.getKey(), condition));
                        break;
                    case "not in":
                        conditionLi = (List<String>) val[0];
                        condition = String.join("','", String.join("','", conditionLi.toArray(new String[conditionLi.size()])));
                        conditionList.add(String.format("%s not in ('%s')", entry.getKey(), condition));
                        break;
                    case "like":
                        conditionList.add(entry.getKey() + " like '%" + val[0].toString() + "%'");
                        break;
                    case "find_in_set":
                        conditionList.add(" find_in_set('" + val[0].toString() + "'," + entry.getKey() + ")>0");
                        break;
                }
            }
        }

        String sql = "";
        if (conditionList.size() > 0) {
            sql += " where " + String.join(" and ", conditionList);
        }
        if (!this.order.isEmpty()) {
            sql += " order by " + this.order;
        }
        return sql;

    }

    public Db order(String order) {
        this.order = order;
        return this;
    }

    public Db limit(String limit) {
        String[] arr = limit.split(",");
        if (arr.length == 1) {
            this.firstRow = 0;
            this.listRow = Integer.parseInt(arr[0]);
        } else {
            this.firstRow = Integer.parseInt(arr[0]);
            this.listRow = Integer.parseInt(arr[1]);
        }
        return this;
    }

    public Object find() {
        return dao.find(this.hql());
    }
    public Object find(String sql) {
        return dao.find(sql,this.clazz);
    }

    public Object findAll() {
        return dao.findAll(this.hql(), this.firstRow, this.listRow);
    }

    public Object findAll(String sql) {
        return dao.findAll(sql, this.clazz, this.firstRow, this.listRow);
    }

    /**
     * 查询总数
     *
     * @return
     */
    public Integer count() {
        String sql = "select count(*) from " + this.clazz.getName() + this.whereSql();
        return dao.count(sql);
    }


    /**
     * 保存数据（插入或更新）
     *
     * @param t
     */
    public void save(BaseEntity t) {
        dao.save(t);
    }

    /**
     * 更新
     */
    public void update(Map<String, Object> map) {
        String sql = "UPDATE " + this.clazz.getName() + " SET ";
        String setSql = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value = (String) entry.getValue();
            if (StringUtils.isEmpty(value)) {
                value = "";
            }
            if (setSql.isEmpty()) {
                setSql = entry.getKey() + "='" + value + "'";
            } else {
                setSql = setSql + "," + entry.getKey() + "='" + value + "'";
            }
        }
        sql += setSql + this.whereSql();
        dao.update(sql);
    }

    /**
     * 更新
     */
    public void update(String key, Object value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        String sql = "UPDATE " + this.clazz.getName() + " SET ";
        sql += key + "='" + value.toString() + "' ";
        sql += this.whereSql();
        dao.update(sql);
    }

    /**
     * 更新
     */
    public void update(String sql) {
        dao.updateSql(sql);
    }

    public void multiInsert(List entities) {
        dao.multiInsert(entities);
    }

    public void delete() {
        String hql = "DELETE " + this.clazz.getName() + this.whereSql();
        dao.delete(hql);
    }
}
