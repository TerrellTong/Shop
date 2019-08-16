package com.itheima.web.servlet;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.*;

public class ProductInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取传来的pid，cid，currentPage
        String pid = request.getParameter("pid");
        String cid = request.getParameter("cid");
        String currentPage = request.getParameter("currentPage");
        //将cid传入service层
        ProductService service = new ProductService();
        //获取cookie
        String content = pid;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            //获取相应的pid并把之前的pid进行拼接
            if(cookie.getName().equals("pids")){
                //content += cookie.getValue()+"-";
                //将content字符串根据"-"进行切割
                String[] split = cookie.getValue().split("-");
                //将字符数组变为LIST
                List<String> list = Arrays.asList(split);
                //将list数组变为链表
                LinkedList<String> linkedList = new LinkedList<String>(list);
                //判断content是否是在链表中
                if (linkedList.contains(content))
                    //如果在就移除这个结点
                    linkedList.remove(content);
                //将刚才浏览的pid放入链表头
                linkedList.addFirst(content);
                //把链表变成字符串
                //创建一个StringBuffer长度可变
                StringBuffer buffer = new StringBuffer();
                for(int i=0;i<linkedList.size();i++) {
                    buffer.append(linkedList.get(i));
                    buffer.append("-");
                }
                //将最后一个- 去掉
                content = buffer.substring(0,buffer.length()-1);
            }
        }
       //创建Cookie，并把相应pid传给response
        Cookie cookie = new Cookie("pids",content);
        //发送cookie
        response.addCookie(cookie);

        Product product = new Product();
        try {
            product = service.FindProductInfo(pid);
            Category category = new Category();
            String cid_Index = service.FindCategoryByPid(pid);
            category.setCid(cid_Index);
            //category封装到product
            product.setCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //将product传入request域中
        request.setAttribute("product",product);
        request.setAttribute("cid",cid);
        request.setAttribute("currentPage",currentPage);
        //将cname传入request
        String cname = null;
        if(cid.equals("")) {
            //如果cid是""则表示前台是通过首页访问的
            cname = service.FindCategoryByCid(product.getCategory().getCid());
        }
        else {
            //否则是从商品列表中访问的
            cname = service.FindCategoryByCid(cid);
        }
        request.setAttribute("cname", cname);
        //转发
        request.getRequestDispatcher("/product_info.jsp").forward(request,response);
    }
}
