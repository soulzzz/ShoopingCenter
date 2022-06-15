package com.xmut.shoppingcenter.entity;

import java.io.Serializable;

public class Application implements Serializable {
    private Integer id;

    private String username;

    private String store_name;

    private String store_img;

    private String store_des;

    private String apply_reason;

    private int apply_state;

    private String send_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_img() {
        return store_img;
    }

    public void setStore_img(String store_img) {
        this.store_img = store_img;
    }

    public String getStore_des() {
        return store_des;
    }

    public void setStore_des(String store_des) {
        this.store_des = store_des;
    }

    public String getApply_reason() {
        return apply_reason;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }

    public int getApply_state() {
        return apply_state;
    }

    public void setApply_state(int apply_state) {
        this.apply_state = apply_state;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }
}
