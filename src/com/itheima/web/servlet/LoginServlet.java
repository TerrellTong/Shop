package com.itheima.web.servlet;

import com.itheima.domain.User;
import com.itheima.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session并往里面存数据
        HttpSession session = request.getSession();
        //获取数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //传入service层
        UserService service = new UserService();
        User user = service.FindByUsernameAndPassword(username,password);
        if(user == null){
            request.setAttribute("flag","false");
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }else{
            session.setAttribute("user",user);
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
