package com.itheima.web.servlet;

import com.google.gson.Gson;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FindOrderInfoByOidServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取oid
        String oid = request.getParameter("oid");
        //传入service层
        ProductService service = new ProductService();
        List<Map<String, Object>> mapList = service.FindOrderItemByOid(oid);
        //通过Gson将数据进行封装
        Gson gson = new Gson();
        String json = gson.toJson(mapList);
        System.out.println(json);
        //数据回写，并设置编码
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
