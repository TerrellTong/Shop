package com.itheima.web.servlet;

import com.itheima.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ActiveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String activeCode = request.getParameter("activeCode");
        //将激活码传入service层
        UserService service = new UserService();
        try {
            service.Active(activeCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //重定向到登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");
    }
}
