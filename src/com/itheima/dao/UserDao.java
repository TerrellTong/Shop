package com.itheima.dao;

import com.itheima.domain.User;
import com.itheima.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

public class UserDao {
    QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
    //注册用户
    public int register(User user) throws SQLException {
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        int update = runner.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName()
                , user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode());
        return update;
    }

    //激活用户
    public void Active(String activeCode) throws SQLException {
        String sql = "update user set state =1 where code=?";
        runner.update(sql,activeCode);
    }

    //根据用户名查找该用户是否已经被注册
    public int FindByUserName(String username) throws SQLException {
        String sql = "select count(*) from user where username = ?";
        Long query = (Long) runner.query(sql, new ScalarHandler(), username);
        return query.intValue();
    }

    //判断用户名密码是否正确
    public User FindByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "select * from user where username = ? and password = ?";
        User user = runner.query(sql, new BeanHandler<User>(User.class), username, password);
        return user;
    }
}
