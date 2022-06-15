package com.xmut.shoppingcenter.okhttp.http;

import android.location.Address;

import java.io.Serializable;

public class HttpAddress {
    private static String[] args;
    final private static String user="user";
    final private static String coupon="coupon";
    final private static String store="store";
    final private static String useraddress="useraddress";
    final private static String shoppingcar="shoppingcar";
    final private static String product="product";
    final private static String order="order";
    final private static String notice="notice";
    final private static String file="file";
    final private static String email="email";
    final private static String orderreason="orderreason";
    final private static String collection = "collection";
    final private static String subscribe = "subscribe";
    public static String Orderreason(){
        return orderreason;
    }
    public static String Coupon() {
        return coupon;
    }
    public static String store() {
        return store;
    }

    public static String collection() {return collection;}

    public static String subscribe(){return subscribe;}

    public static String useraddress() {
        return useraddress;
    }

    public static String shoppingcar() {
        return shoppingcar;
    }

    public static String product() {
        return product;
    }

    public static String order() {
        return order;
    }

    public static String notice() {
        return notice;
    }

    public static String file() {
        return file;
    }

    public static String email() {
        return email;
    }

    public static String user(){
        return user;
    }




    /**
     *
     * @param address 首地址 例如：“user”
     * @param method 地址中的方法  例如： “insert”
     * @return
     */
    public static String[] get(String address,String method){
        switch (method){
            case "login":args=getLogin();
                break;
            case "insert":args=getInsert(address);
                break;
            case "update":args=getUpdate(address);
                break;
            case "list":args=getList(address);
                break;
        }
        return args;
    }
    /**
     *采用方法重载，分别处理两种情况，带id和不带id
     * @param address 首地址 例如：“user”
     * @param method 地址中的方法  例如： “delete”
     * @param id id则为相应参数  delete后的id参数
     * @return
     */
    public static String[] get(String address,String method,Serializable id){
        switch (method){
            case "delete":args=getDelete(address,id);
                break;
            case "line":args=getLine(address,id);
                break;
            case "list":args=getListById(address,id);

        }
        return args;
    }
    public static String[] get(String address,String method,String by,Serializable id){
        switch (method) {
            case "listby":
                args = getListby(address, by, id);
                break;
        }
        return args;
    }
    private static String[] getListby(String address, String by,Serializable id) {
        args = new String[]{address,by,String.valueOf(id)};
        return args;
    }

    private static String[] getLogin(){
        args=new String[]{user,"login"};
        return args;
    }
    private static String[] getInsert(String address){
        args=new String[]{address,"insert"};
        return args;
    }
    private static String[] getDelete(String address, Serializable id){
        args=new String[]{address,"delete", String.valueOf(id)};
        return args;
    }
    private static String[] getUpdate(String address){
        args=new String[]{address,"update"};
        return args;
    }
    private static String[] getLine(String address, Serializable id){
        args=new String[]{address,"line", String.valueOf(id)};
        return args;
    }
    private static String[] getList(String address){
        args=new String[]{address,"list"};
        return args;
    }
    private static String[] getListById(String address,Serializable id){
        args=new String[]{address,"list",String.valueOf(id)};
        return args;
    }
}
