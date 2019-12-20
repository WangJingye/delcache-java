package com.delcache.extend;

import com.delcache.common.dao.BaseDao;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Db {

    public int page = 1;

    public int pageSize = 10;

    private BaseDao dao;

    private Map<String, List<Object[]>> where;

    private String order;

    private List<String> fields;

    private String limit;

    private String select;

    private String table;

    private String primaryKey;

    private Class clazz;

    public static Db table(Class clazz) {
        Db db = new Db();
        db.dao = ContextLoader.getCurrentWebApplicationContext().getBeansOfType(BaseDao.class).get("baseDaoImpl");
        db.clazz = clazz;
        db.table = "tbl_" + Util.toUnderlineString(clazz.getSimpleName());
        db.where = new LinkedHashMap<>();
        db.order = "";
        db.limit = "";
        db.select = "*";
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

    public String sql() {
        String sql = "select " + this.select + " from " + this.table;
        return sql + this.whereSql() + this.limit;
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
                        condition = String.join("','", String.join("','", conditionLi.toArray(new String[0])));
                        conditionList.add(String.format("%s in ('%s')", entry.getKey(), condition));
                        break;
                    case "not in":
                        conditionLi = (List<String>) val[0];
                        condition = String.join("','", String.join("','", conditionLi.toArray(new String[0])));
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

    public Db field(String select) {
        this.select = select;
        return this;
    }

    public Db order(String order) {
        this.order = order;
        return this;
    }

    public Db limit(Object limit) {
        if (!StringUtils.isEmpty(limit)) {
            this.limit = " limit " + limit.toString();
        }
        return this;
    }

    public Object find() {
        return dao.find(this.sql(), this.clazz);
    }

    public Object find(String sql) {
        return dao.find(sql, this.clazz);
    }

    public Object findAll() {
        return dao.findAll(this.sql(), this.clazz);
    }

    public Object findAll(String sql) {
        return dao.findAll(sql, this.clazz);
    }

    /**
     * 查询总数
     *
     * @return
     */
    public int count() {
        String sql = "select count(*) from " + this.table + this.whereSql();
        return dao.count(sql);
    }

    /**
     * 插入
     */
    public int insert(String sql) {
        return dao.insert(sql);
    }

    /**
     * 保存数据（插入或更新）
     */
    public void save(Object t) throws Exception {
        this.getField();
        String method = "get" + Util.toCamelName(primaryKey);
        Method getMethod = clazz.getMethod(method);
        Object value = getMethod.invoke(t);
        if (Util.parseInt(value) == 0) {
            int id = dao.insert(buildInsertSql(t));
            method = "set" + Util.toCamelName(primaryKey);
            Method setMethod = clazz.getMethod(method, int.class);
            setMethod.invoke(t, id);
        } else {
            dao.update(buildUpdateSql(t));
        }
    }

    /**
     * 更新
     */
    public void update(Map<String, Object> map) {
        String sql = "update " + this.table + " set ";
        StringBuilder setSql = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String value = StringUtils.isEmpty(entry.getValue()) ? "" : entry.getValue().toString();
            if (setSql.length() != 0) {
                setSql.append(",");
            }
            //todo 存在sql注入问题,需要修改
            setSql.append(entry.getKey()).append("='").append(value).append("'");
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
        String sql = "update " + this.table + " set ";
        sql += key + "='" + value.toString() + "'";
        sql += this.whereSql();
        dao.update(sql);
    }

    /**
     * 更新
     */
    public void update(String sql) {
        dao.update(sql);
    }

    public void multiInsert(Object entities) {
        this.multiInsert(entities, "");
    }

    public void multiInsert(Object entities, String ignore) {
        try {
            this.getField();
            StringBuilder sql = new StringBuilder("insert ").append(ignore).append(" into ").append(this.table);
            List<String> keys = new ArrayList<>();
            Map<String, Method> methodMap = new HashMap<>();
            Method[] methods = clazz.getDeclaredMethods();
            List<String> methodList = new ArrayList<>();
            List<String> entityParams = new ArrayList<>();
            for (Method method : methods) {
                methodList.add(method.getName());
            }
            //获取key
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String column = Util.toUnderlineString(field.getName());
                if (column.equals(this.primaryKey)) {
                    continue;
                }
                if (!this.fields.contains(column)) {
                    continue;
                }
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (!methodList.contains(method)) {
                    continue;
                }
                keys.add(column);
            }
            //获取 value
            for (Object entity : (List<Object>) entities) {
                List<String> params = new ArrayList<>();
                for (Field field : fields) {
                    String column = Util.toUnderlineString(field.getName());
                    if (column.equals(this.primaryKey)) {
                        continue;
                    }
                    if (!this.fields.contains(column)) {
                        continue;
                    }
                    String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    if (!methodList.contains(method)) {
                        continue;
                    }
                    if (!methodMap.containsKey(method)) {
                        methodMap.put(method, clazz.getMethod(method));
                    }
                    Method getMethod = methodMap.get(method);
                    Object value = getMethod.invoke(entity);
                    if (StringUtils.isEmpty(value)) {
                        value = "";
                    }
                    params.add(value.toString());
                }
                entityParams.add("('" + String.join("','", params.toArray(new String[0])) + "')");
            }
            sql.append("(`").append(String.join("`,`", keys.toArray(new String[0])))
                    .append("`) values ")
                    .append(String.join(",", entityParams.toArray(new String[0])));
            dao.batchInsert(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        String sql = "delete from " + this.table + this.whereSql();
        dao.delete(sql);
    }

    public void getField() {
        if (this.fields == null || this.fields.size() == 0) {
            String sql = "show columns from " + this.table;
            List<Map<String, Object>> fields = (List<Map<String, Object>>) dao.findAll(sql, Map.class);
            for (Map<String, Object> field : fields) {
                if ("PRI".equals(field.get("Key"))) {
                    this.primaryKey = field.get("Field").toString();
                    break;
                }
            }
            this.fields = Util.arrayColumn(fields, "Field");
        }
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

    public String buildInsertSql(Object entity) {
        StringBuilder sql = new StringBuilder("insert into ").append(this.table).append("(`");
        List<String> params = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        //获取属性信息
        try {
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();
            List<String> methodList = new ArrayList<>();
            for (Method method : methods) {
                methodList.add(method.getName());
            }
            for (Field field : fields) {
                String column = Util.toUnderlineString(field.getName());
                if (column.equals(this.primaryKey)) {
                    continue;
                }
                if (!this.fields.contains(column)) {
                    continue;
                }
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (methodList.contains(method)) {
                    Method getMethod = clazz.getMethod(method);
                    Object value = getMethod.invoke(entity);
                    if (StringUtils.isEmpty(value)) {
                        value = "";
                    }
                    params.add(value.toString());
                    keys.add(column);

                }
            }
        } catch (Exception e) {
        }
        sql.append(String.join("`,`", keys.toArray(new String[0])))
                .append("`) values ('")
                .append(String.join("','", params.toArray(new String[0])))
                .append("')");
        return sql.toString();
    }

    public String buildUpdateSql(Object entity) {
        StringBuilder sql = new StringBuilder("update ").append(this.table).append(" set ");
        List<String> params = new ArrayList<>();
        List<String> where = new ArrayList<>();
        //获取属性信息
        try {
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();
            List<String> methodList = new ArrayList<>();
            for (Method method : methods) {
                methodList.add(method.getName());
            }
            for (Field field : fields) {
                String column = Util.toUnderlineString(field.getName());
                if (!this.fields.contains(column)) {
                    continue;
                }
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (methodList.contains(method)) {
                    Method getMethod = clazz.getMethod(method);
                    Object value = getMethod.invoke(entity);
                    if (column.equals(this.primaryKey)) {
                        where.add("`" + column + "` = '" + value + "'");
                    } else {
                        if (StringUtils.isEmpty(value)) {
                            value = "";
                        }
                        params.add("`" + column + "` = '" + value + "'");
                    }


                }
            }
        } catch (Exception e) {
        }
        sql.append(String.join(", ", params.toArray(new String[0])))
                .append(" where ")
                .append(String.join(" and ", where.toArray(new String[0])));
        return sql.toString();
    }

}
