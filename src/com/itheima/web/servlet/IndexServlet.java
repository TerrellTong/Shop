package com.itheima.web.servlet;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //进入到service层，重构页面
        ProductService service = new ProductService();
        //查找商品条类
        //List<Category> CategoryList = service.FindCategory();
        //查找最热商品
        List<Product> HotProductList = service.FindHotProduct();
        //查找最新上架的商品
        List<Product> NewProductList = service.FindNewProduct();

        //由于session域存放的与用户相关的数据/经常用的，因此我们把数据存放到request域中
        //request.setAttribute("CategoryList",CategoryList);
        request.setAttribute("HotProductList",HotProductList);
        request.setAttribute("NewProductList",NewProductList);

        //定向转发
        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }
}
