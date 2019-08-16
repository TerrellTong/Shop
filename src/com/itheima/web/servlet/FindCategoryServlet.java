package com.itheima.web.servlet;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FindCategoryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();
        List<Category> categories = service.FindCategory();
        //用Gson将categories集合转换成字符串
        Gson gson = new Gson();
        String json = gson.toJson(categories);
        //将json通过write方法写给Ajax
        //将返回的数据进行UTF-8编码
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
