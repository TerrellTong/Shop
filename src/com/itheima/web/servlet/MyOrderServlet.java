package com.itheima.web.servlet;

import com.itheima.domain.Order;
import com.itheima.domain.OrderItem;
import com.itheima.domain.Product;
import com.itheima.domain.User;
import com.itheima.service.ProductService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class MyOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session，通过session进行判断
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //判断用户是否登录，如果没登录则重定向到登录界面
        if(user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        //从表中获取order集合
        ProductService service = new ProductService();
        List<Order> orderList = service.FindAllOrderByUid(user.getUid());
        /*由于在数据库中的order取到的数据没有orderItem的集合，因此我们要自己进行封装
            对于order对象中还存在product对象，与上面的理由一样不能进行封装
         */
        if(orderList != null) {
            //封装order对象中的orderItem
            //1.先根据订单编号获取orderitem
            for (Order order : orderList) {
                //获取的是前台想要展示的数据
                List<Map<String, Object>> MapList = service.FindOrderItem(order.getOid());
                //2.在order中封装orderItem
                //由于不能够直接获得orderItem，因此我们只能自己进行orderItem的封装
                //由于存在Maplist一个list对象里面存在的是我们想在后台进行展示的数据，是一些分散的数据
                //因此我们可以直接用beanUtils来直接封装orderItem(BeanUtils是把相对应的属性的值进行封装)
                for (Map<String, Object> map : MapList) {
                    OrderItem orderItem = new OrderItem();
                    //用BeanUtils进行封装orderItem的count subtotal
                    try {
                        BeanUtils.populate(orderItem, map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //封装Product，并把Product封装到OrderItem中
                    Product product = new Product();
                    try {
                        BeanUtils.populate(product, map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    orderItem.setProduct(product);
                    //把封装了我们想要的数据的orderItem封装到order中
                    order.getOrderItems().add(orderItem);
                }
            }
        }

        //将orderlist存入request域中并转发
        request.setAttribute("orderlist",orderList);
        request.getRequestDispatcher("/order_list.jsp").forward(request,response);
    }
}
