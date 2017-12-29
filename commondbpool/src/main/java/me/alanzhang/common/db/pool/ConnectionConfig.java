package me.alanzhang.common.db.pool;

import java.io.Serializable;

/**
 * 数据库链接配置
 * Created by alanzhang on 2017/12/28.
 */
public class ConnectionConfig implements Serializable{
    /**驱动名称*/
    private String driver;
    /**用户名*/
    private String user;
    /**密码*/
    private String password;
    /**url*/
    private String url;
    /**大小*/
    private Integer size;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectionConfig that = (ConnectionConfig) o;

        if (driver != null ? !driver.equals(that.driver) : that.driver != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = driver != null ? driver.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConnectionConfig{" +
                "driver='" + driver + '\'' +
                ", user='" + user + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                '}';
    }
}
