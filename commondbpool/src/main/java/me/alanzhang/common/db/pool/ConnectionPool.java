package me.alanzhang.common.db.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.sql.Connection;

/**
 * 数据库连接池
 * Created by alanzhang on 2017/12/28.
 */
public class ConnectionPool extends GenericKeyedObjectPool<ConnectionConfig,Connection> {

    public ConnectionPool(KeyedPooledObjectFactory<ConnectionConfig, Connection> factory) {
        super(factory);
    }

    public ConnectionPool(KeyedPooledObjectFactory<ConnectionConfig, Connection> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }
}
