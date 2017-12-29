package me.alanzhang.common.db.pool;

import java.sql.Connection;

/**
 * 数据库链接实现
 * Created by alanzhang on 2017/12/28.
 */
public interface ConnectionProvider {
    /**
     * 获得指定配置链接
     * @param connectionConfig 链接配置
     * @return
     * @throws Exception
     */
    Connection getConnection(ConnectionConfig connectionConfig) throws Exception;

    /**
     * 释放数据库链接
     * @param connectionConfig 链接配置
     * @param connection 释放链接
     */
    void releaseConnection(ConnectionConfig connectionConfig, Connection connection);

    /**
     * 关闭链接
     * @param connectionConfig 链接配置
     * @param connection 关闭链接
     */
    void closeConnection(ConnectionConfig connectionConfig, Connection connection);
}
