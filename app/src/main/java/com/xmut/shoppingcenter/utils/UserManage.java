package com.xmut.shoppingcenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class UserManage {
    SharedPreferences sp;
    private static UserManage instance;


    public void Jump(Activity a1, Class a2, Serializable serializable, String method){
        Intent it = new Intent(a1,a2);
        it.putExtra(method,serializable);
        a1.startActivity(it);
    }
    public void Jump(Activity a1, Class a2){
        Intent it = new Intent(a1,a2);
        a1.startActivity(it);
    }
    public void FinishJump(Activity a1, Class a2){
        Intent it = new Intent(a1,a2);
        a1.startActivity(it);
        a1.finish();
    }
    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }
    public boolean getShouldRecommend(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        }
        return sp.getBoolean(getUserInfo(context).getId()+ConstantValue.ShouldRecommend, true);

    }
    public void setShouldRecommend(Context context,boolean s){
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getUserInfo(context).getId()+ConstantValue.ShouldRecommend,s).apply();

    }
    public void clearUserInfo(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        }
        Log.d("GetSharedPreferences","Cleared user info");
        sp.edit().remove(ConstantValue.CurUser).apply();
    }

    public void saveUserInfo(Context context, User user) {
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        }

        clearUserInfo(context);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(ConstantValue.CurUser, json);
        editor.commit();
        Log.d("GetSharedPreferences","Saved user info");
    }

    public void saveFoot(Context context, Product p) {
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        }


        List<Product> products = getFoot(context);
        if (products.size() >=50){
            products.remove(49);
        }
        for(Iterator<Product> it = products.iterator(); it.hasNext();){
            Product i = it.next();
            if(i.getProduct_id().equals(p.getProduct_id())){
                it.remove();
                break;
            }
        }

        products.add(0,p);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(products);

        clearFoot(context);

        editor.putString(getUserInfo(context).getId()+ConstantValue.Foot, json);
        editor.apply();
    }


    public void clearFoot(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        }
        sp.edit().remove(getUserInfo(context).getId()+ConstantValue.Foot).apply();
    }
    public List<Product> getFoot(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        }
        String json = sp.getString(getUserInfo(context).getId()+ConstantValue.Foot, null);
        Gson gson = new Gson();
        List<Product> products=new ArrayList<>();
        if (json != null) {
            try{
                JsonArray arry = JsonParser.parseString(json).getAsJsonArray();
                for(JsonElement jsonElement:arry){
                    products.add(gson.fromJson(jsonElement,Product.class) ); }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return products;
    }
    public User getUserInfo(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            Log.d("GetSharedPreferences","Got user info");
        }
        Gson gson = new Gson();
        String json = sp.getString(ConstantValue.CurUser, null);
        if(json !=null){
            User user = gson.fromJson(json, User.class);
            return user;
        }else
            return null;
    }

    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || value.trim().length() == 0;
    }
    public static String trim(String input){

        return Pattern.compile("\t|\r|\n| ").matcher(input).replaceAll("");
    }
}
