package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

import java.sql.SQLException;

public class UserService {
    UserDao dao = new UserDao();
    //注册用户
    public Boolean register(User user) throws SQLException {
        //由于是插入数据，则返回的是影响的行数
        int row = dao.register(user);
        return row>0?true:false;
    }

    //通过激活码激活用户
    public void Active(String activeCode) throws SQLException {
        dao.Active(activeCode);
    }

    //通过用户名找用户
    public Boolean FindByUserName(String username) throws SQLException {
        int i = dao.FindByUserName(username);
        return i>0?true:false;
    }

    public User FindByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            user = dao.FindByUsernameAndPassword(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
