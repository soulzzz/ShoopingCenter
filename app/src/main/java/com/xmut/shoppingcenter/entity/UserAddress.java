package com.xmut.shoppingcenter.entity;

import java.io.Serializable;

public class UserAddress implements Serializable {
    private int address_id;

    private int user_id;

    private String user_address;

    private String user_name;

    private String user_tel;

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public UserAddress(int user_id, String user_address, String user_name, String user_tel) {
        this.user_id = user_id;
        this.user_address = user_address;
        this.user_name = user_name;
        this.user_tel = user_tel;
    }
}