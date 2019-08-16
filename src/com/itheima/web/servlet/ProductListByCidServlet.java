package com.itheima.web.servlet;

import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductListByCidServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        String current = request.getParameter("CurrentPage");
        //获取当前页
        int CurrentPage = 0;
        if(current == null)
             CurrentPage = 1;
        else
            CurrentPage = Integer.parseInt(current);
        //每一页的商品总数
        int CurrentCount = 12;
        //将cid传入service层
        ProductService service = new ProductService();
        PageBean<Product> pageBean  = service.FindProductByCid(cid,CurrentPage,CurrentCount);

        //接收从ProductInfo中传过来的cookie
        Cookie[] cookies = request.getCookies();
        //建一个链表来存储浏览历史记录的商品
        List<Product> historyProduct = new ArrayList<>();
        if(cookies != null){
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals("pids")) {
                    //再把pid用"-"进行切割
                    String[] split = cookie.getValue().split("-");
                    for(int i=0;i<split.length;i++){
                        //从数据库中根据pid查数据
                        Product product = null;
                        try {
                            product = service.FindProductInfo(split[i]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        historyProduct.add(product);
                    }

                }
            }
        }

        //将PageBean用request域进行传输
        request.setAttribute("pageBean",pageBean);
        request.setAttribute("cid",cid);
        request.setAttribute("historyproduct",historyProduct);
        //重定向
        request.getRequestDispatcher("/product_list.jsp").forward(request,response);
    }
}
