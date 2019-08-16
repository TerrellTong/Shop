package com.itheima.web.servlet;

import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.MailUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //将提交表单的数据的中文能正常显示
        request.setCharacterEncoding("UTF-8");
        String sex = request.getParameter("sex");
        System.out.println(sex);
        //获得session
        HttpSession session = request.getSession();
        //通过BeanUtils进行数据封装
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            //定义一个类型转换器，将String装换成Data，
            ConvertUtils.register(
                    new Converter() {
                        @Override
                        public Object convert(Class aClass, Object o) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date s = null;
                            try {
                                //Object就是我们自己要转成Date的String
                                s = format.parse(o.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return s;
                        }
                    }, Date.class);
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }
        //由于有一些东西未能封装，则我们要自己设置
        user.setUid(UUID.randomUUID().toString());
        user.setTelephone(null);
        user.setState(0);//0表示未激活
        //激活码由于也是字符串，则可以用UUID来生成
        String activeCode = UUID.randomUUID().toString();
        user.setCode(activeCode);

        //将user对象传入service层
        UserService service = new UserService();
        Boolean isRegisterSuccess = false;
        try {
             isRegisterSuccess = service.register(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //获取session域中的isCheckSuccess
        Boolean isCheckSuccess = (Boolean) session.getAttribute("isCheckSuccess");
        if(isRegisterSuccess)
        {
            //发邮件告诉用户，要求进行校验
            try {
                MailUtils.sendMail(user.getEmail(),"恭喜您，您已注册成功，点击下面地址来进行激活"+
                        "<a href=http://localhost:8080/Shop/active?activeCode="+activeCode+">"+
                        "http://localhost:8080/Shop/active?activeCode="+activeCode
                        +"</a>");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
        }
        else{
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
