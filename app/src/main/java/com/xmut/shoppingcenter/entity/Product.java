package com.xmut.shoppingcenter.entity;

import java.io.Serializable;
import java.util.List;

public class Product  implements Serializable {



    private String product_id;

    private Integer store_id;

    private String product_name;

    private String product_img;

    private double product_price;

    private String product_des;

    private int product_sales;

    private int product_stock;

    private String product_category;

    private int product_isshow;

    private int product_weight;

    private String starttime;

    private String endtime;

    private String store_name;

    private List<ProductComment> productCommentList;

    private String order_id; //ordermapper
    private int product_num;  //ordermapper
    private int order_state; //ordermapper
    private double detail_price; //ordermapper
    private List<OrderReason> order_reason; //ordermapper
    private String express_num;


    private boolean isSelected;

    private int shoppingcar_id;

    public List<OrderReason> getOrder_reason() {
        return order_reason;
    }

    public void setOrder_reason(List<OrderReason> order_reason) {
        this.order_reason = order_reason;
    }

    public String getExpress_num() {
        return express_num;
    }

    public void setExpress_num(String express_num) {
        this.express_num = express_num;
    }

    public double getDetail_price() {
        return detail_price;
    }

    public void setDetail_price(double detail_price) {
        this.detail_price = detail_price;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public List<ProductComment> getProductCommentList() {
        return productCommentList;
    }

    public void setProductCommentList(List<ProductComment> productCommentList) {
        this.productCommentList = productCommentList;
    }
    public int getShoppingcar_id() {
        return shoppingcar_id;
    }

    public void setShoppingcar_id(int shoppingcar_id) {
        this.shoppingcar_id = shoppingcar_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_des() {
        return product_des;
    }

    public void setProduct_des(String product_des) {
        this.product_des = product_des;
    }

    public int getProduct_sales() {
        return product_sales;
    }

    public void setProduct_sales(int product_sales) {
        this.product_sales = product_sales;
    }

    public int getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(int product_stock) {
        this.product_stock = product_stock;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }

    public int getProduct_isshow() {
        return product_isshow;
    }

    public void setProduct_isshow(int product_isshow) {
        this.product_isshow = product_isshow;
    }

    public int getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(int product_weight) {
        this.product_weight = product_weight;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selectd) {
        isSelected = selectd;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!product_id.equals(product.product_id)) return false;
        return product_id.equals(product.product_id);
    }
    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", store_id=" + store_id +
                ", order_id='" + order_id + '\'' +
                ", product_num=" + product_num +"}";
    }
}

