package com.itheima.web.servlet;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class AddProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = new Product();
        //用map来方便存数据
        Map<String,Object> map = new HashMap<String,Object>();
        try {
        //1.获得磁盘项工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //2.创建文件上传的核心类
        ServletFileUpload upload = new ServletFileUpload(factory);
        //3.对request进行解析
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem fileItem : list){
                boolean formField = fileItem.isFormField();
                if(formField){
                    //如果是普通表单项
                    String fileItemName = fileItem.getFieldName();
                    String fileItemValue = fileItem.getString("UTF-8");
                    map.put(fileItemName,fileItemValue);
                }else{
                    //如果是文件项
                    //文件名(上传)
                    String fileName = fileItem.getName();
                    //获得上传的内容
                    InputStream in = fileItem.getInputStream();
                    String path_store = this.getServletContext().getRealPath("upload");
                    OutputStream out = new FileOutputStream(path_store+"/"+fileName);
                    //复制文件
                    IOUtils.copy(in,out);
                    in.close();
                    out.close();
                    map.put("pimage","upload/"+fileName);
                }
            }
            //对map中存在的数据其进行封装
            BeanUtils.populate(product,map);
            product.setPid(UUID.randomUUID().toString());
            product.setPdate(new Date());
            product.setPflag(0);
            //没有Category自己创造一个Category
            Category category = new Category();
            category.setCname(map.get("cid").toString());
            product.setCategory(category);
            //将封装好的product传给后台
            ProductService service = new ProductService();
            service.AddProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
