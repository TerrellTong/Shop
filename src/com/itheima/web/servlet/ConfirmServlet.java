package com.itheima.web.servlet;

import com.itheima.domain.Order;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import com.itheima.utils.PaymentUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfirmServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.修改用户的信息
        //取出的数据是中文,设置编码这样存储的数据就不会乱码了
        request.setCharacterEncoding("UTF-8");
        String oid = request.getParameter("oid");
        Map<String, String[]> parameterMap = request.getParameterMap();
        Order order = new Order();
        try {
            BeanUtils.populate(order,parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProductService service = new ProductService();
        service.UpdateAdd(order,oid);

        //2.进行结账（第三方平台给的）
        //接入的易宝支付
        // 获得 支付必须基本数据
        String orderid = request.getParameter("oid");
        String money = order.getTotal()+"";//支付金额
        // 银行
        String pd_FrpId = request.getParameter("pd_FrpId");

        // 发给支付公司需要哪些数据
        String p0_Cmd = "Buy";
        String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
        String p2_Order = orderid;
        String p3_Amt = money;
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        // 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
        // 第三方支付可以访问网址
        String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
        String p9_SAF = "";
        String pa_MP = "";
        String pr_NeedResponse = "1";
        // 加密hmac 需要密钥
        String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
                "keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);


        String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
                "&p0_Cmd="+p0_Cmd+
                "&p1_MerId="+p1_MerId+
                "&p2_Order="+p2_Order+
                "&p3_Amt="+p3_Amt+
                "&p4_Cur="+p4_Cur+
                "&p5_Pid="+p5_Pid+
                "&p6_Pcat="+p6_Pcat+
                "&p7_Pdesc="+p7_Pdesc+
                "&p8_Url="+p8_Url+
                "&p9_SAF="+p9_SAF+
                "&pa_MP="+pa_MP+
                "&pr_NeedResponse="+pr_NeedResponse+
                "&hmac="+hmac;

        //重定向到第三方支付平台
        response.sendRedirect(url);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
