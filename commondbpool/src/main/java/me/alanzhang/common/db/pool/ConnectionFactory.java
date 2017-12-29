package me.alanzhang.common.db.pool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库链接
 * Created by alanzhang on 2017/12/28.
 */
public class ConnectionFactory extends BaseKeyedPooledObjectFactory<ConnectionConfig, Connection> {
    /**
     * 数据库链接配置
     */
    private ConnectionConfig connectionConfig;

    public ConnectionFactory() {
        super();
    }

    public ConnectionFactory(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    private synchronized void initialize(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
        try {
            String driver = this.connectionConfig.getDriver();
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get driver class ", e);
        }
    }

    @Override
    public Connection create(ConnectionConfig connectionConfig) throws Exception {
        this.connectionConfig = connectionConfig;
        initialize(this.connectionConfig);
        String user = this.connectionConfig.getUser();
        String password = this.connectionConfig.getPassword();
        String url = this.connectionConfig.getUrl();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
        return connection;
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<Connection>(connection);
    }



    @Override
    public PooledObject<Connection> makeObject(ConnectionConfig connectionConfig) throws Exception {
       return super.makeObject(connectionConfig);
    }

    /**
     * 销毁链接
     * @param connectionConfig 链接配置
     * @param pooledObject 池对象
     */
    @Override
    public void destroyObject(ConnectionConfig connectionConfig, PooledObject<Connection> pooledObject) {
        final Connection connection = pooledObject.getObject();
        try {
            if (!connection.isClosed()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测一个数据库链接是否有效
     * @param connectionConfig 链接配置
     * @param pooledObject 池对象
     * @return
     */
    @Override
    public boolean validateObject(ConnectionConfig connectionConfig, PooledObject<Connection> pooledObject) {
        final Connection connection = pooledObject.getObject();
        try {
            if (!connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测一个对象是否有效
     * @param connectionConfig 链接配置
     * @param pooledObject 池对象
     * @throws Exception
     */
    @Override
    public void activateObject(ConnectionConfig connectionConfig, PooledObject<Connection> pooledObject) throws Exception {
        super.activateObject(connectionConfig,pooledObject);
    }

    /**
     * 归还对象时处理
     * @param connectionConfig 链接配置
     * @param pooledObject 池对象
     * @throws Exception
     */
    @Override
    public void passivateObject(ConnectionConfig connectionConfig, PooledObject<Connection> pooledObject) throws Exception {
        super.passivateObject(connectionConfig,pooledObject);
    }
}
