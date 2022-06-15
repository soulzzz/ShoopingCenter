package com.xmut.shoppingcenter.entity;

import java.io.Serializable;

public class OrderReason implements Serializable {
    private static final long serialVersionUID = 397024324530822278L;

    private int id;

    private String order_id;

    private String product_id;

    private String commit_date;

    private Integer commit_person;

    private String commit_reason;

    private String commit_img;

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

    public String getCommit_date() {
        return commit_date;
    }

    public void setCommit_date(String commit_date) {
        this.commit_date = commit_date;
    }

    public Integer getCommit_person() {
        return commit_person;
    }

    public void setCommit_person(Integer commit_person) {
        this.commit_person = commit_person;
    }

    public String getCommit_reason() {
        return commit_reason;
    }

    public void setCommit_reason(String commit_reason) {
        this.commit_reason = commit_reason;
    }

    public String getCommit_img() {
        return commit_img;
    }

    public void setCommit_img(String commit_img) {
        this.commit_img = commit_img;
    }
}
