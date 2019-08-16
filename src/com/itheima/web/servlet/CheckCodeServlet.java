package com.itheima.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断验证码输入是否正确

        String checkCode = request.getParameter("checkCode");
        HttpSession session = request.getSession();

        System.out.println(session.getAttribute("checkcode_session"));

        if(session.getAttribute("checkcode_session")==null) {
            response.getWriter().write("{\"flag\":null}");
            return;
        }
        if(checkCode.equals(session.getAttribute("checkcode_session"))) {
            session.setAttribute("isCheckSuccess", true);
            response.getWriter().write("{\"flag\":true}");
        }
        else {
            session.setAttribute("isCheckSuccess", true);
            response.getWriter().write("{\"flag\":false}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
