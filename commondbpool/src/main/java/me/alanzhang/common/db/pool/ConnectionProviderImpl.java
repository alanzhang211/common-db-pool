package me.alanzhang.common.db.pool;

import java.sql.Connection;

/**
 * 数据库链接
 * Created by alanzhang on 2017/12/28.
 */
public class ConnectionProviderImpl implements ConnectionProvider {
    /**
     * 数据库连接池
     */
    private ConnectionPool connectionPool;

    public ConnectionProviderImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public ConnectionProviderImpl() {
        super();
    }


    @Override
    public Connection getConnection(ConnectionConfig connectionConfig) throws Exception {
        return this.connectionPool.borrowObject(connectionConfig);
    }

    @Override
    public void releaseConnection(ConnectionConfig connectionConfig,Connection connection) {
        this.connectionPool.returnObject(connectionConfig,connection);

    }

    /**
     * 关闭链接
     *
     * @param connectionConfig 链接配置
     * @param connection       关闭链接
     */
    @Override
    public void closeConnection(ConnectionConfig connectionConfig, Connection connection) {
        //TODO
    }
}
