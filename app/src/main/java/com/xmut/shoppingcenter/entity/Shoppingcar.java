package com.xmut.shoppingcenter.entity;



import java.io.Serializable;


public class Shoppingcar implements Serializable {
    
    private Integer id;
    
    private String product_id;
    
    private int user_id;

    public Shoppingcar(String product_id, int user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }
}