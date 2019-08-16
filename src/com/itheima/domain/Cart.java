package com.itheima.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    //存储N个购物项
    private Map<String,CartItem> map = new HashMap<String, CartItem>();
    private double totalMoney;

    public Map<String, CartItem> getMap() {
        return map;
    }

    public void setMap(Map<String, CartItem> map) {
        this.map = map;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
