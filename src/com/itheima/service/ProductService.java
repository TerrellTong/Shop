package com.itheima.service;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.utils.DataSourceUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductService {
    ProductDao dao = new ProductDao();
    public List<Product> FindHotProduct() {
        List<Product> HotProduct = null;
        try {
            HotProduct = dao.FindHotProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return HotProduct;
    }

    public List<Product> FindNewProduct() {
        List<Product> NewProduct = null;
        try {
            NewProduct = dao.FindNewProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return NewProduct;
    }

    //查找商品条目
    public List<Category> FindCategory() {
        List<Category> Category = null;
        try {
            Category = dao.FindCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Category;
    }

    //根据cid,CurrentPage等其他参数来封装PageBean
    public PageBean<Product> FindProductByCid(String cid,int CurrentPage,int CurrentCount) {
        PageBean<Product> pageBean = new PageBean<Product>();
        //设置当前页
        pageBean.setCurrentPage(CurrentPage);
        //设置当前页商品的数量
        pageBean.setCurrentCount(CurrentCount);
        //设置每一个cid的总商品数
        int TotalCount = 0;
        try {
             TotalCount = dao.getTotalCount(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setTotalCount(TotalCount);
        //设置总页数
        int TotalPage = (int) Math.ceil(1.0*TotalCount/CurrentCount);
        pageBean.settotalPage(TotalPage);
        //设置商品总数
        List<Product> productList = null;
        try {
            productList = dao.FindProductByCid(cid,CurrentPage,CurrentCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageBean.setProductList(productList);
        return pageBean;
    }

    //查找商品信息
    public Product FindProductInfo(String pid) throws SQLException {
        return dao.FindProductInfo(pid);
    }

    //提交订单
    public void SubmitOrder(Order order) {
        //由于不能够出现只有订单而没有订单项，也不会出现存在订单项而没有订单的状况,因此我们用事务来解决
        try {
            //1.开启事务
            DataSourceUtils.startTransaction();
            //2.将order传入dao层进行数据库添加
            dao.submitOrder(order);
            dao.submitOrderItem(order.getOrderItems());
        } catch (SQLException e) {
            try {
                //失败就进行事务回滚
                DataSourceUtils.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                //无论是否成功，最后都要提交事务并释放资源
                DataSourceUtils.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //更新用户的地址等信息
    public void UpdateAdd(Order order,String oid) {
        try {
            dao.UpdateAdd(order,oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //从数据库中根据用户UID查找所有的订单信息，并封装成一个list集合
    public List<Order> FindAllOrderByUid(String uid) {
        List<Order> orderList = null;
        try {
             orderList = dao.FindAllOrderByUid(uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Map<String, Object>> FindOrderItem(String oid) {
        List<Map<String, Object>> map = null;
        try {
            map = dao.FindOrderItem(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    //添加商品
    public void AddProduct(Product product) {
        try {
            dao.AddProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查找所有的订单
    public List<Order> FindAllOrder() {
        List<Order> orderList = null;
        try {
            orderList = dao.FindAllOrder();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    //根据oid来查找订单项
    public List<Map<String, Object>> FindOrderItemByOid(String oid) {
        List<Map<String, Object>> mapList = null;
        try {
            mapList = dao.FindOrderItemByOid(oid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //根据cid来查找相应的cname
    public String FindCategoryByCid(String cid) {
        String cname = null;
        try {
            cname = dao.FindCategoryByCid(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cname;
    }

    //通过pid来获取cid
    public String FindCategoryByPid(String pid) throws SQLException {
        return dao.FindCategoryByPid(pid);
    }
}
