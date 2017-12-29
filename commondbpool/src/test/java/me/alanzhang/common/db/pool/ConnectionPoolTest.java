package me.alanzhang.common.db.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by alanzhang on 2017/12/29.
 */
public class ConnectionPoolTest {
    public static final String url = "jdbc:mysql://127.0.0.1/cboard";
    public static final String driver = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "alan";
    public static final String sql = "select 1;";

    public static void testPool() throws Exception{
        ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
        connectionPoolConfig.setMaxTotalPerKey(1);
        connectionPoolConfig.setMaxTotal(1);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDriver(driver);
        connectionConfig.setUser(user);
        connectionConfig.setPassword(password);
        connectionConfig.setUrl(url);
        connectionFactory.create(connectionConfig);
        ConnectionPool connectionPool = new ConnectionPool(connectionFactory,connectionPoolConfig);
        Connection connection = connectionPool.borrowObject(connectionConfig);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        System.out.println(rs.getRow());

        connectionPool.returnObject(connectionConfig,connection);

        Connection connection1 = connectionPool.borrowObject(connectionConfig);
        preparedStatement = connection1.prepareStatement("select 2");
        System.out.println(connection.equals(connection1));
        rs = preparedStatement.executeQuery();
        System.out.println(rs.getRow());
    }

    public static void testConnectionProvider() throws Exception {
        ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
        connectionPoolConfig.setMaxTotalPerKey(1);
        connectionPoolConfig.setMaxTotal(1);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDriver(driver);
        connectionConfig.setUser(user);
        connectionConfig.setPassword(password);
        connectionConfig.setUrl(url);
        connectionFactory.create(connectionConfig);
        ConnectionPool connectionPool = new ConnectionPool(connectionFactory,connectionPoolConfig);

        ConnectionProvider connectionProvider = new ConnectionProviderImpl(connectionPool);

        Connection connection = connectionProvider.getConnection(connectionConfig);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        System.out.println(rs.getRow());
    }

    public static void main(String[] args) throws Exception {
        ConnectionPoolTest connectionPoolTest = new ConnectionPoolTest();
        connectionPoolTest.testConnectionProvider();

    }
}
