package com.itheima.web.servlet;

import com.itheima.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CheckUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        //传入service层
        UserService service = new UserService();
        Boolean is_exist =false;
        try {
             is_exist = service.FindByUserName(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //将数据以json格式传给register.jsp
        response.getWriter().write("{\"is_exist\":"+is_exist+"}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
