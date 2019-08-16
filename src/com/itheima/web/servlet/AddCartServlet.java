package com.itheima.web.servlet;

import com.itheima.domain.Cart;
import com.itheima.domain.CartItem;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AddCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    /*
    * 由于购物车里面存的是购物信息，则我们把数据存入session域中
    * 对于每个购物车对象中存在商品信息以及别的相关信息，
    * 那么我们可以把每一条购物信息可以看成一个购物对象，
    * 购物对象由商品类和别的关键字组成
    * */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session
        HttpSession session = request.getSession();
        //获取添加到购物车的商品信息
        String pid= request.getParameter("pid");
        String buyNumber_str = request.getParameter("BuyNumber");
        int buyNumber = Integer.parseInt(buyNumber_str);
        //根据pid获取商品信息
        ProductService service = new ProductService();
        Product product = new Product();
        double currentMoney;
        try {
            product = service.FindProductInfo(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //将信息封装到CartItem中
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setBuyNumber(buyNumber);
        currentMoney = product.getMarket_price() * buyNumber;
        cartItem.setCurrentMoney(currentMoney);
        //如果session域中存在购物车就不生成了
        Cart cart = (Cart) session.getAttribute("cart");
        if(session.getAttribute("cart") == null) {
             cart = new Cart();
        }

        //判断被选中的商品是否包含在购物车中
        Map<String, CartItem> itemMap = cart.getMap();
        if(itemMap.containsKey(pid)){
            //如果包含
            //1.取出之前的pid所存取的数量
            CartItem oldItem = itemMap.get(pid);
            int oldNum = oldItem.getBuyNumber();
            //2.将旧的购买数量与新的购买数量合并，并再次存入其中
            oldItem.setBuyNumber(oldNum+buyNumber);
            //3.修改总和
            double oldmoney = oldItem.getCurrentMoney();
            double newMoney = oldmoney+oldItem.getProduct().getMarket_price()*buyNumber;
            //4.将新的值存入oldItem
            oldItem.setCurrentMoney(newMoney);
            //5.修改购物车对象的总和
            double total = cart.getTotalMoney()+oldItem.getProduct().getMarket_price()*buyNumber;
            cart.setTotalMoney(total);
        }else {
            //不包含就放入购物车中
            /*计算总价并封装到cart对象中
        总价=之前购物车的所有价格+新添加商品的单项价格
         */
            cart.getMap().put(pid,cartItem);
            double total = cart.getTotalMoney()+currentMoney;
            cart.setTotalMoney(total);
        }
        

        //将购物车对象传给session域
       session.setAttribute("cart",cart);
        //重定向
        response.sendRedirect(request.getContextPath()+"/cart.jsp");
    }
}
