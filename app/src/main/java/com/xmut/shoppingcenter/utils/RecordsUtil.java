package com.xmut.shoppingcenter.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class RecordsUtil {
    SharedPreferences sp;
    private static RecordsUtil instance;

    public static RecordsUtil getInstance() {
        if (instance == null) {
            instance = new RecordsUtil();
        }
        return instance;
    }

    public void removeRecord(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("records", Context.MODE_PRIVATE);
        }
        sp.edit().remove(UserManage.getInstance().getUserInfo(context).getId()+ConstantValue.Record).commit();
    }


    public void saveRecords(Context context, String record) {
        if (sp == null) {
            sp = context.getSharedPreferences("records", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        }
        List<String> records = getRecords(context);
        if( records.indexOf(record)!=- 1){
            records.remove(records.indexOf(record));
        }

        records.add(0,record);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(records);

        removeRecord(context);

        editor.putString(UserManage.getInstance().getUserInfo(context).getId()+ConstantValue.Record, json);
        editor.apply();
    }


    public List<String> getRecords(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("records", Context.MODE_PRIVATE);

        }
        Gson gson = new Gson();
        String json = sp.getString(UserManage.getInstance().getUserInfo(context).getId()+ConstantValue.Record, null);
        List<String> records=new ArrayList<>();
        if (json != null) {
            try{
                JsonArray arry = JsonParser.parseString(json).getAsJsonArray();
                for(JsonElement jsonElement:arry){
                    records.add(gson.fromJson(jsonElement,String.class) ); }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return records;
    }

}
