package com.itheima.dao;

import com.itheima.domain.*;
import com.itheima.utils.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductDao {
    QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
    //查找最热商品
    public List<Product> FindHotProduct() throws SQLException {
        String sql = "select * from product where is_hot = 1 limit 0,9";
        List<Product> HotProducts = runner.query(sql, new BeanListHandler<Product>(Product.class));
        return HotProducts;
    }

    //查找新上架的商品
    public List<Product> FindNewProduct() throws SQLException {
        //默认order by是升序排列，因此要用降序排列
        String sql = "select * from product order by pdate desc limit 0,9";
        List<Product> NewProducts = runner.query(sql, new BeanListHandler<Product>(Product.class));
        return NewProducts;
    }

    //查找商品列表
    public List<Category> FindCategory() throws SQLException {
        String sql = "select * from category";
        List<Category> categoryList = runner.query(sql, new BeanListHandler<Category>(Category.class));
        return  categoryList;
    }

    //根据cid查找
    public List<Product> FindProductByCid(String cid,int CurrentPage,int CurrentCount) throws SQLException {
        String sql = "select * from product where cid = ? limit ?,?";
        List<Product> products =  runner.query(sql, new BeanListHandler<Product>(Product.class),cid,CurrentPage,CurrentCount);
        return products;
    }

    //获取单个cid的商品总数
    public int getTotalCount(String cid) throws SQLException {
        String sql = "select count(*) from product where cid = ?";
        Long query = (Long) runner.query(sql, new ScalarHandler(), cid);
        return query.intValue();
    }

    //根据pid查找商品的所有信息，用map存储，并用BeanUtils封装
    public Product FindProductInfo(String pid) throws SQLException {
        String sql = "select * from product where pid = ?";
        //Map<String, Object> map = runner.query(sql, new MapHandler(), pid);
        Product product = runner.query(sql, new BeanHandler<Product>(Product.class), pid);
        return product;
    }

    //提交订单到数据库中
    public void submitOrder(Order order) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
        //通过ThreadLocal来获取连接,这样我们能保证事务开启的连接与数据库执行的连接是同一个连接
        Connection connection = DataSourceUtils.getConnection();
        queryRunner.update(connection,sql,order.getOid(),order.getOrdertime()
                ,order.getTotal(),order.getState(),order.getAddress(),
                order.getName(), order.getTelephone(),order.getUser().getUid());
    }

    //提交订单信息到数据库中
    public void submitOrderItem(List<OrderItem> orderItems) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into orderitem values(?,?,?,?,?)";
        //通过ThreadLocal来获取连接
        Connection connection = DataSourceUtils.getConnection();
        for(OrderItem orderItem : orderItems){
            queryRunner.update(connection,sql,orderItem.getItemid(),orderItem.getCount()
                    ,orderItem.getSubtotal(),orderItem.getProduct().getPid(),
                    orderItem.getOrder().getOid());
        }

    }

    //更新用户的信息
    public void UpdateAdd(Order order,String oid) throws SQLException {
        String sql = "update orders set address=?,name=?,telephone=? where oid=?";
        runner.update(sql,order.getAddress(),order.getName(),order.getTelephone(),oid);
    }

    //根据用户名来查找它下面的订单项
    public List<Order> FindAllOrderByUid(String uid) throws SQLException {
        String sql = "select * from orders where uid = ?";
        //这时候把从数据库中所取得的数据全部封装到了一个Bean中
        List<Order> orderList = runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
        return orderList;
    }

    //根据oid查找商品列表
    public List<Map<String, Object>> FindOrderItem(String oid) throws SQLException {
        //多表的联合查询，这样就不用再web层再进行封装了
        String sql = "select p.pimage,p.pname,p.market_price,o.count,o.subtotal" +
                " from product p , orderitem o where o.oid=? and o.pid = p.pid";

        //通过MapHandler来将多表查询的结果进行存储
        List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
        return mapList;
    }

    //添加商品
    public void AddProduct(Product product) throws SQLException {
        String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
        runner.update(sql,product.getPid(),product.getPname(),product.getMarket_price(),
                product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot()
                ,product.getPdesc(),product.getPflag(),product.getCategory().getCid());
    }

    //查找所有的订单
    public List<Order> FindAllOrder() throws SQLException {
        String sql = "select * from orders";
        List<Order> orderList = runner.query(sql, new BeanListHandler<Order>(Order.class));
        return orderList;
    }

    //根据oid来查找自己想要的数据
    public List<Map<String, Object>> FindOrderItemByOid(String oid) throws SQLException {
        String sql = " select p.pimage,p.pname,p.market_price,i.count,i.subtotal "+
                    " from orderItem i,product p,orders o"+
                    " where o.oid=? and i.pid=p.pid and o.oid=i.oid ";
        List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
        return mapList;
    }

    //根据cid查找cname
    public String FindCategoryByCid(String cid) throws SQLException {
        String sql = "select cname from category where cid = ?";
        Map<String, Object> map = runner.query(sql, new MapHandler(), cid);
        String cname = (String) map.get("cname");
        return cname;
    }

    //通过pid在Product表查找cid
    public String FindCategoryByPid(String pid) throws SQLException {
        String sql = "select cid from product where pid = ?";
        Map<String, Object> query = runner.query(sql, new MapHandler(), pid);
        String cid = (String) query.get("cid");
        return cid;
    }
}
