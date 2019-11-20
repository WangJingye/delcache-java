package com.delcache.common.service;

import com.delcache.common.dao.BaseDao;
import com.delcache.common.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.*;

@Service
@Transactional
public class BaseService {
    @Autowired
    BaseDao dao;

    private Map<String, List<Object[]>> where;

    public String order;

    public String table;
    public Class clazz;

    public Integer listRow = 0;

    public Integer firstRow = 0;

    public BaseService table(Class clazz) {
        this.clazz = clazz;
        Table annotation = (Table) this.clazz.getAnnotation(Table.class);
        this.table = annotation.name();
        this.where = new HashMap<>();
        this.order = "";
        this.listRow = this.firstRow = 0;
        return this;
    }

    public BaseService where(String key, Object value, String restriction) {
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

    public BaseService where(String key, Object value) {
        if (null == value) {
            this.where.remove(key);
        } else {
            List<Object[]> values = new ArrayList<>();
            if (this.where.containsKey(key)) {
                values = this.where.get(key);
            }
            values.add(new Object[]{value, "eq"});
            this.where.put(key, values);
        }
        return this;
    }

    public String hql() {
        String sql = "from " + this.clazz.getName();
        return sql + this.whereSql();
    }

    public String whereSql() {
        List<String> conditionList = new ArrayList<>();
        for (Map.Entry<String, List<Object[]>> entry : this.where.entrySet()) {
            List<Object[]> valueList = entry.getValue();
            for (Object[] val : valueList) {
                String condition;
                switch (val[1].toString()) {
                    case "=":
                    case "eq":
                        conditionList.add(String.format("%s='%s'", entry.getKey(), val[0].toString()));
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
                        condition = String.join("','", ((String[]) val[0]));
                        conditionList.add(String.format("%s in ('%s')", entry.getKey(), condition));
                        break;
                    case "not in":
                        condition = String.join("','", ((String[]) val[0]));
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

    public BaseService order(String order) {
        this.order = order;
        return this;
    }

    public BaseService limit(String limit) {
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

    public Object findAll() {
        return dao.findAll(this.hql(), this.firstRow, this.listRow);
    }

    /**
     * 查询总数
     *
     * @return
     */
    public Integer count() {
        return dao.count(this.hql());
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
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sql += entry.getKey() + "='" + entry.getValue().toString() + "'";
        }
        sql += this.whereSql();
        dao.update(sql);
    }

    /**
     * 更新
     */
    public void update(String sql) {
        dao.updateSql(sql);
    }


}
