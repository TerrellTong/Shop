package com.itheima.web.servlet;

import com.itheima.domain.Cart;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DelProServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        //获取session域，并把当中的Pid进行删除
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        cart.getMap().remove(pid);
        //重定向到cart.jsp中
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
}
