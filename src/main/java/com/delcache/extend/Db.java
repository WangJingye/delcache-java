package com.delcache.extend;

import com.delcache.common.dao.BaseDao;
import com.delcache.common.entity.BaseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        db.dao = ContextLoader.getCurrentWebApplicationContext().getBeansOfType(BaseDao.class).get("baseDaoImpl");
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
        return dao.find(sql, this.clazz);
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
        Class clazz = t.getClass();
        String primaryKey = this.getPrimaryKey(clazz);

        try {
            boolean isNew = false;
            Field[] fields = clazz.getDeclaredFields();
            if (!primaryKey.isEmpty()) {
                for (Field field : fields) {
                    Id id = field.getAnnotation(Id.class);
                    if (id == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    Method setMethod = pd.getReadMethod();
                    Object res = setMethod.invoke(t);
                    if (Integer.parseInt(res.toString()) == 0) {
                        isNew = true;
                        break;
                    }
                }
                for (Field field : fields) {
                    Column col = field.getAnnotation(Column.class);
                    if (col == null) {
                        continue;
                    }
                    if (isNew && col.name().equals("create_time")) {
                        this.setObjectFieldValue(t, field, Util.time());
                    }
                    if (col.name().equals("update_time")) {
                        this.setObjectFieldValue(t, field, Util.time());
                    }
                }
            }
        } catch (Exception e) {

        }
        dao.save(t);
    }

    /**
     * 更新
     */
    public void update(Map<String, Object> map) {
        String sql = "UPDATE " + this.clazz.getName() + " SET ";
        StringBuilder setSql = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value = StringUtils.isEmpty(entry.getValue()) ? "" : entry.getValue().toString();
            if (setSql.length() != 0) {
                setSql.append(",");
            }
            //todo 存在sql注入问题,需要修改
            setSql.append(entry.getKey()).append("='").append(value).append("'");
        }
        if (map.get("update_time") == null && this.hasFieldDbName("update_time")) {
            if (setSql.length() != 0) {
                setSql.append(",");
            }
            setSql.append("update_time='").append(String.valueOf(Util.time())).append("'");
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
        sql += key + "='" + value.toString() + "'";
        if (!key.equals("update_time") && this.hasFieldDbName("update_time")) {
            if (sql.length() != 0) {
                sql = sql + ",";
            }
            sql = sql + "update_time='" + String.valueOf(Util.time()) + "'";
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

    public void multiInsert(List entities) {
        Object obj = entities.get(0);
        Class clazz = obj.getClass();

        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Column col = field.getAnnotation(Column.class);
                if (col == null) {
                    continue;
                }
                for (Object o : entities) {
                    if (col.name().equals("create_time")) {
                        this.setObjectFieldValue(o, field, Util.time());
                    }
                    if (col.name().equals("update_time")) {
                        this.setObjectFieldValue(o, field, Util.time());
                    }
                }
            }
        } catch (Exception e) {

        }
        dao.multiInsert(entities);
    }

    public void delete() {
        String hql = "DELETE " + this.clazz.getName() + this.whereSql();
        dao.delete(hql);
    }

    public boolean hasFieldDbName(String fieldName) {
        return this.hasFieldDbName(fieldName, this.clazz);
    }

    public boolean hasFieldDbName(String fieldName, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column col = field.getAnnotation(Column.class);
            if (col != null && col.name().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public String getPrimaryKey() {
        return this.getPrimaryKey(this.clazz);
    }

    public String getPrimaryKey(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id == null) {
                continue;
            }

            Column col = field.getAnnotation(Column.class);
            if (col == null) {
                continue;
            }
            return col.name();
        }
        return "";
    }

    public void setObjectFieldValue(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
            Method setMethod = pd.getWriteMethod();
            setMethod.invoke(obj, value);
        } catch (Exception e) {
            //塞不进去就不塞了
        }
    }
}
