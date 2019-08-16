package com.itheima.web.servlet;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.service.ProductService;
import com.itheima.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();
        //对于不会怎么变的数据我们放入缓存中（Redis）
        //获取Jedis
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryList = jedis.get("CategoryList");
        //如果缓存区没有就从数据库读
        if(categoryList==null) {
            System.out.println("缓存没数据，从数据库读数据");
            List<Category> CategoryList = service.FindCategory();
            //将对象用Gson工具封装成字符串（json）
            Gson gson = new Gson();
            categoryList  = gson.toJson(CategoryList);
            //将数据放入Redis中
            jedis.set("CategoryList",categoryList);
        }
        //将json写到桌面上
        //将页面以中文显示
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(categoryList);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
