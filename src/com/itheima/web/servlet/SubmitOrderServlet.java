package com.itheima.web.servlet;

import com.itheima.domain.*;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class SubmitOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session域中的数据
        HttpSession session = request.getSession();
        //获取购物车的信息
        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        //判断用户是否登录
        if(user==null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }
        //由于订单表与购物车的信息是相同的但是功能是不同的，因此我们要重新封装数据
        Order order = new Order();
        //1.封装Order
        order.setOid(UUID.randomUUID().toString());
        order.setOrdertime(new Date());
        order.setTotal(cart.getTotalMoney());
        order.setState(0);//0表示未支付，1表示已支付
        order.setAddress(null);
        order.setName(null);
        order.setTelephone(null);
        order.setUser(user);
        //获取购物车的商品信息
        Map<String, CartItem> map = cart.getMap();
        //通过foreach来进行orderitem的赋值
        for(Map.Entry<String, CartItem> entry:map.entrySet() ){
            //获取每一条记录的cartItem
            CartItem cartItem = entry.getValue();
            /*由于记录的订单是一条新的数据，因此我们也要对数据进行封装，这样方便我们
              只需要传输一个Order就可以进行完成数据库添加Order和OrderItem
            */
            OrderItem orderItem = new OrderItem();
            orderItem.setItemid(UUID.randomUUID().toString());
            orderItem.setCount(cartItem.getBuyNumber());
            orderItem.setSubtotal(cartItem.getCurrentMoney());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            //每封装好一个orderItem之后就可以直接在Order订单中添加到其集合中
            order.getOrderItems().add(orderItem);
        }
        //将封装好的order传入service层
        ProductService service = new ProductService();
        service.SubmitOrder(order);

        //将order放入到session域中
        session.setAttribute("order",order);
        //清空购物车
        session.removeAttribute("cart");
        //将页面重定向到order_info.jsp中
        response.sendRedirect(request.getContextPath()+"/order_info.jsp");
    }
}
