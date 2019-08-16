package com.itheima.domain;

import java.util.List;

public class PageBean<T> {
    //当前页
    private int currentPage;
    //当前页商品数目
    private int CurrentCount;
    //总页数
    private int totalPage;
    //总商品数
    private int TotalCount;
    List<T> productList;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(int currentCount) {
        CurrentCount = currentCount;
    }

    public int gettotalPage() {
        return totalPage;
    }

    public void settotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public List<T> getProductList() {
        return productList;
    }

    public void setProductList(List<T> productList) {
        this.productList = productList;
    }
}
