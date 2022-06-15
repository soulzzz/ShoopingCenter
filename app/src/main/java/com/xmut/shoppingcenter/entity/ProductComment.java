package com.xmut.shoppingcenter.entity;

import java.io.Serializable;

public class ProductComment implements Serializable {
    private int id;

    private String order_id;

    private String product_id;

    private int user_id;

    private String user_name;

    private double product_score;

    private String product_comment;

    private String comment_img;

    private String createtime;

    public String getComment_img() {
        return comment_img;
    }

    public void setComment_img(String comment_img) {
        this.comment_img = comment_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getProduct_score() {
        return product_score;
    }

    public void setProduct_score(double product_score) {
        this.product_score = product_score;
    }

    public String getProduct_comment() {
        return product_comment;
    }

    public void setProduct_comment(String product_comment) {
        this.product_comment = product_comment;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
