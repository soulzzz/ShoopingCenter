package com.xmut.shoppingcenter.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * (Subscribe)实体类
 *
 * @author makejava
 * @since 2022-03-10 11:29:32
 */
public class Subscribe implements Serializable {
    private static final long serialVersionUID = 246873246455153303L;

    private Integer id;

    private String store_img;

    private String store_name;

    private Integer store_id;

    public String getStore_img() {
        return store_img;
    }

    public void setStore_img(String store_img) {
        this.store_img = store_img;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    private Integer user_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}