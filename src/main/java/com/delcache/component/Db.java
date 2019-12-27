package com.delcache.component;

import com.delcache.common.dao.BaseDao;
import com.delcache.common.dao.impl.BaseDaoImpl;
import com.delcache.common.entity.BaseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Db {

    public int page = 1;

    public int pageSize = 10;

    private BaseDao dao;

    private Map<String, List<Object[]>> where;

    private String order;

    private String limit;

    private String select;

    private String table;

    private Class clazz;
    private String primaryKey;

    private List<String> methodList;

    private Field[] fields;

    public static Db table(Class clazz) {
        return table(clazz, true);
    }

    /**
     * @param clazz
     * @param isSpring
     * @return
     */
    public static Db table(Class clazz, boolean isSpring) {
        Db db = new Db();
        if (!isSpring) {
            //不依赖spring执行，需要手动赋值dao
            BaseDaoImpl dao = new BaseDaoImpl();
            dao.jdbcTemplate = new JdbcTemplate(db.getDataSource());
            db.dao = dao;
        } else {
            db.dao = ContextLoader.getCurrentWebApplicationContext().getBeansOfType(BaseDao.class).get("baseDaoImpl");
        }
        db.clazz = clazz;
        db.table = "tbl_" + Util.toUnderlineString(clazz.getSimpleName());
        db.where = new LinkedHashMap<>();
        db.order = "";
        db.limit = "";
        db.select = "*";
        db.fields = clazz.getDeclaredFields();
        for (Field field : db.fields) {
            PrimaryKey f = field.getAnnotation(PrimaryKey.class);
            if (f != null) {
                db.primaryKey = Util.toUnderlineString(field.getName());
                break;
            }
        }
        Method[] methods = clazz.getDeclaredMethods();
        List<String> methodList = new ArrayList<>();
        for (Method method : methods) {
            methodList.add(method.getName());
        }
        db.methodList = methodList;
        return db;
    }

    DriverManagerDataSource getDataSource() {
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/application.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.get("jdbc.driver").toString());
        dataSource.setUrl(properties.get("jdbc.url").toString());
        dataSource.setUsername(properties.get("jdbc.username").toString());
        dataSource.setPassword(properties.get("jdbc.password").toString());
        return dataSource;
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

        StringBuilder sql = new StringBuilder();
        if (conditionList.size() > 0) {
            sql.append(" where ").append(String.join(" and ", conditionList));
        }
        if (!this.order.isEmpty()) {
            sql.append(" order by ").append(this.order);
        }
        return sql.toString();

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
        if (this.limit.isEmpty()) {
            this.limit = " limit 1";
        }
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
        String method = "get" + Util.toCamelName(this.primaryKey);
        Method getMethod = this.clazz.getMethod(method);
        Object value = getMethod.invoke(t);
        if (Util.parseInt(value) == 0) {
            int id = dao.insert(buildInsertSql(t));
            if (id == 0) {
                throw new Exception("数据添加失败");
            }
            method = "set" + Util.toCamelName(this.primaryKey);
            Method setMethod = this.clazz.getMethod(method, int.class);
            setMethod.invoke(t, id);
        } else {
            int i = dao.update(buildUpdateSql(t));
            if (i == 0) {
                throw new Exception("数据保存失败");
            }
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
        StringBuilder sql = new StringBuilder("update ").append(this.table).append(" set ");
        sql.append("`").append(key).append("` = '").append(value.toString()).append("'");
        try {
            Field field = this.clazz.getDeclaredField("updateTime");
            if (field != null) {
                sql.append(", update_time = '").append(Util.time()).append("'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql.append(this.whereSql());
        dao.update(sql.toString());
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
            StringBuilder sql = new StringBuilder("insert ").append(ignore).append(" into ").append(this.table);
            List<String> keys = new ArrayList<>();
            Map<String, Method> methodMap = new HashMap<>();
            List<String> entityParams = new ArrayList<>();
            //获取key
            for (Field field : this.fields) {
                String column = Util.toUnderlineString(field.getName());
                //主键不进行insert
                if (column.equals(this.primaryKey)) {
                    continue;
                }
                //数据库字段不存在
                DbIgnore dbIgnore = field.getAnnotation(DbIgnore.class);
                if (dbIgnore != null) {
                    continue;
                }
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (!this.methodList.contains(method)) {
                    continue;
                }
                keys.add(column);
            }
            //获取 value
            for (Object entity : (List<Object>) entities) {
                List<String> params = new ArrayList<>();
                for (Field field : this.fields) {
                    String column = Util.toUnderlineString(field.getName());
                    //主键不进行insert
                    if (column.equals(this.primaryKey)) {
                        continue;
                    }
                    //数据库字段不存在
                    DbIgnore dbIgnore = field.getAnnotation(DbIgnore.class);
                    if (dbIgnore != null) {
                        continue;
                    }
                    String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    if (!this.methodList.contains(method)) {
                        continue;
                    }
                    if (!methodMap.containsKey(method)) {
                        methodMap.put(method, this.clazz.getMethod(method));
                    }
                    Method getMethod = methodMap.get(method);
                    Object value = getMethod.invoke(entity);
                    if (StringUtils.isEmpty(value)) {
                        value = "";
                    }
                    if (Util.parseInt(value) == 0 && "create_time".equals(column)) {
                        value = String.valueOf(Util.time());
                    }
                    if (Util.parseInt(value) == 0 && "update_time".equals(column)) {
                        value = String.valueOf(Util.time());
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

    public String buildInsertSql(Object entity) {
        StringBuilder sql = new StringBuilder("insert into ").append(this.table).append("(`");
        List<String> params = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        //获取属性信息
        try {
            for (Field field : this.fields) {
                String column = Util.toUnderlineString(field.getName());
                //主键不进行insert
                if (column.equals(this.primaryKey)) {
                    continue;
                }
                //数据库字段不存在
                DbIgnore dbIgnore = field.getAnnotation(DbIgnore.class);
                if (dbIgnore != null) {
                    continue;
                }
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (this.methodList.contains(method)) {
                    Method getMethod = this.clazz.getMethod(method);
                    Object value = getMethod.invoke(entity);
                    if (value == null) {
                        continue;
                    }
                    if (StringUtils.isEmpty(value)) {
                        value = "";
                    }
                    if (Util.parseInt(value) == 0 && "create_time".equals(column)) {
                        value = String.valueOf(Util.time());
                    }
                    if (Util.parseInt(value) == 0 && "update_time".equals(column)) {
                        value = String.valueOf(Util.time());
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
            for (Field field : this.fields) {
                //数据库字段不存在
                DbIgnore dbIgnore = field.getAnnotation(DbIgnore.class);
                if (dbIgnore != null) {
                    continue;
                }
                String column = Util.toUnderlineString(field.getName());
                String method = "get" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                if (this.methodList.contains(method)) {
                    Method getMethod = this.clazz.getMethod(method);
                    Object value = getMethod.invoke(entity);
                    if (column.equals(this.primaryKey)) {
                        where.add("`" + column + "` = '" + value + "'");
                    } else {
                        if (StringUtils.isEmpty(value)) {
                            value = "";

                        }
                        if ("update_time".equals(column)) {
                            value = String.valueOf(Util.time());
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
