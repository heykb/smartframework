package com.zhu.transactional;

import com.zhu.helper.ConfigHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 事务操作
 */
public final class DatabaseHelper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;
    static {
        CONNECTION_HOLDER = new ThreadLocal<>();
        QUERY_RUNNER =  new QueryRunner();
        DATA_SOURCE = new BasicDataSource();

        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn!=null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                log.error("get connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if(conn!=null){
            try {
                //关闭事务自动提交（开启事务）
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("begin transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if(conn!=null){
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                log.error("commit transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if(conn!=null){
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                log.error("rollback transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object...args){
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), args);
        }catch (SQLException e){
            log.error("query entity failure",e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object...args){
        List<T> entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), args);
        }catch (SQLException e){
            log.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询并返回单个列
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T query(String sql, Object...args){
        T obj;
        try {
            Connection conn = getConnection();
            obj = QUERY_RUNNER.query(conn, sql, new ScalarHandler<T>(), args);
        }catch (SQLException e){
            log.error("query failure",e);
            throw new RuntimeException(e);
        }
        return obj;
    }
    /**
     * 查询并返回多个列
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(String sql, Object...args){
        List<T> list;
        try {
            Connection conn = getConnection();
            list = QUERY_RUNNER.query(conn, sql, new ColumnListHandler<T>(), args);
        }catch (SQLException e){
            log.error("query list failure",e);
            throw new RuntimeException(e);
        }
        return list;
    }
    /**
     * 查询并返回多个列 (具有唯一性)
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public static <T> Set<T> querySet(String sql, Object...args){
       List<T> list = queryList(sql,args);
       return new LinkedHashSet<T>(list);
    }

    /**
     * 查询并返回数组
     * @param sql
     * @param args
     * @return
     */
    public static Object[] queryArray(String sql,Object...args){
        Object[] array;
        try{
            Connection conn = getConnection();
            array = QUERY_RUNNER.query(conn,sql,new ArrayHandler(),args);
        } catch (SQLException e) {
            log.error("query array failure",e);
            throw new RuntimeException(e);
        }
        return array;
    }
    /**
     * 查询并返回数组列表
     * @param sql
     * @param args
     * @return
     */
    public static List<Object[]> queryArrayList(String sql,Object...args){
        List<Object[]> resultListArray;
        try{
            Connection conn = getConnection();
            resultListArray = QUERY_RUNNER.query(conn,sql,new ArrayListHandler(),args);
        } catch (SQLException e) {
            log.error("query array failure",e);
            throw new RuntimeException(e);
        }
        return resultListArray;
    }

    public static Map<String,Object> queryMap(String sql,Object...args){
        Map<String,Object> map;
        try{
            Connection conn = getConnection();
            map = QUERY_RUNNER.query(conn,sql,new MapHandler(),args);
        } catch (SQLException e) {
            log.error("query map failure",e);
            throw new RuntimeException(e);
        }
        return map;
    }

    public static List<Map<String,Object>> queryMapList(String sql,Object...args){
        List<Map<String,Object>> mapList;
        try{
            Connection conn = getConnection();
            mapList = QUERY_RUNNER.query(conn,sql,new MapListHandler(),args);
        } catch (SQLException e) {
            log.error("query map list failure",e);
            throw new RuntimeException(e);
        }
        return mapList;
    }

    /**
     * 执行更新语句（update insert delete）
     * @param sql
     * @param args
     * @return
     */
    public static int update(String sql,Object... args){
        int rows;
        try{
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn,sql,args);
        } catch (SQLException e) {
            log.error("execute update  failure",e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * insert into tableName(...) values(?,?...) ;
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(MapUtils.isNotEmpty(fieldMap)){
            log.error("can not insert entity: fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + entityClass.getSimpleName();
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(columns.lastIndexOf(","),columns.length(),")");

        sql+=columns+" VALUES "+values;
        Object[] params = fieldMap.values().toArray();
        return update(sql,params) == 1;
    }

    /**
     * update tablename set filedName=? ,... where filedname=? and ...
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntityById(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(MapUtils.isNotEmpty(fieldMap)){
            log.error("can not update entity: fieldMap is empty");
            return false;
        }
        String sql = "UPDATE "+ entityClass.getSimpleName() +" SET ";
        StringBuilder columns = new StringBuilder();
        for(String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(" = ?, ");
        }
        sql+=columns.substring(columns.lastIndexOf(","),columns.length())+"WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return update(sql,params) == 1;
    }
}
