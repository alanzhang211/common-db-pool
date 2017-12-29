# common-db-pool
一个数据库连接池的实现；支持对不同类型数据库链接进行缓存。

# 需求
+ 数据库连接的池化管理
+ 支持不同数据源链接池化处理

# 问题
+ 如何维护链接对象
+ 如何区分不同数据源池化
+ 如何实现资源同步问题

# 分析
## 如何维护对象
使用阻塞队列实现对象存储，数据结构采用LinkedBlockingDeque（同步集合，内部线程序安全）。

## 如何区分不同数据源池化
即席查询中，针对不同的数据库链接，会创建不同的的数据库链接对象（connection 是线程不安全的）。为了保证安全，可以使用ThreadLocal来维护。不同的connection要缓存，并且在空闲时可以复用。内部使用数据结构ConcurrentHashMap同步集合map来维护不同数据源链接。

## 如何实现同步
如上，使用同步集合实现共享资源（数据库链接connection）的线程安全。

# 实现
## 思路
涉及到资源的创建，释放等。最初，依据自己的方式实现。发现底层需要考虑的同步，以及淘汰策略问题。写了个雏形，感觉不满意。于是，想到apache有一个专门的对象池处理组件。common-pool2。然后，查阅了相关资料。果然满足底层需求。其中相关组件介绍，这里不展开。[common-pool2对象池(连接池)的介绍及使用](http://www.coc88.com/h-nd.html?id=152&)，这篇文章将各组件介绍的比较详细，可参考。redis的java实现[jedis](https://github.com/xetorthio/jedis)底层就是借用common-pool2实现的。

然后，使用文中介绍的GenericKeyedObjectPool，其内部就是一组k-v模型。刚好满足本文需求，实现**不同数据源链接池化处理**。

## 设计
### 类图

![类图](http://of7369y0i.bkt.clouddn.com/%EF%BC%8F2017/12/%E8%BD%AF%E4%BB%B6%E8%AE%BE%E8%AE%A1%EF%BC%8Fuml.jpg)

+ ConnectionConfig：数据链接配置
+ ConnectionPoolConfig：数据库连接池配置
+ ConnectionFactory：数据库链接
+ ConnectionPool：数据库连接池
+ ConnectionProvider：数据库链接接口
+ ConnectionProviderImpl：数据库链接实现

### 测试

```
public class ConnectionPoolTest {
    public static final String url = "jdbc:mysql://127.0.0.1/mysql";
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
```
---
个人公众号，欢迎交流！

![个人公众号](http://of7369y0i.bkt.clouddn.com/qrcode_for_gh_381787324660_430.jpg)

