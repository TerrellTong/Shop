package com.itheima.web.servlet;

import com.itheima.domain.Order;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FindAllOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService service = new ProductService();
        List<Order> orderList = service.FindAllOrder();
        //将数据存入request域中
        request.setAttribute("orderList",orderList);
        request.getRequestDispatcher("/admin/order/list.jsp").forward(request,response);
    }
}
