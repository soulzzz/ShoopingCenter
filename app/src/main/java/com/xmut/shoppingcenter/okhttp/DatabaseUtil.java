package com.xmut.shoppingcenter.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.http.HttpUrl;
import com.xmut.shoppingcenter.okhttp.http.OKHttpUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

    /**
     * 增删改查
     * 增：post请求
     * 删：delete请求
     * 改：put请求
     * 查：get请求
     */
    public class DatabaseUtil {
        final private static String baseUrl= HttpUrl.getBaseUrl();
        //final private static Gson gson= JsonBean.getGson();
        final private static Gson gson=new GsonBuilder().serializeNulls().create();
        private static String jsonForm=null;
        private static String jsonResult=null;
        private static Result result=new Result();

        public static Result getParamters(Map<String, String> paramsMap,String... args){
            try {
                jsonForm= "";
                jsonResult= OKHttpUtil.getAsyncRequest(baseUrl,paramsMap,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//删除成功

                    }else if(result.getCode()==400){//删除失败
                        result.setInfo("删除失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return result;

        }
        public static Result postParamters(Map<String, String> paramsMap,String... args){
            try {
                jsonForm= "";
                jsonResult= OKHttpUtil.postparamter(baseUrl,paramsMap,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//删除成功

                    }else if(result.getCode()==400){//删除失败
                        result.setInfo("删除失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

                return result;

        }
        public static Result putParamters(Map<String, String> paramsMap,String... args){
            try {
                jsonForm= "";
                jsonResult= OKHttpUtil.putparamter(baseUrl,paramsMap,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//删除成功

                    }else if(result.getCode()==400){//删除失败
                        result.setInfo("删除失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return result;

        }
        public static Result uploadFile(File file, String... args){
            try {
                jsonForm= "";
                jsonResult= OKHttpUtil.uploadFile(baseUrl,file,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//删除成功

                    }else if(result.getCode()==400){//删除失败
                        result.setInfo("删除失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return result;

        }
        /**
         * 通过异步post请求
         * 数据库插入操作----增        --例子
         * @param bean bean对象       user
         * @param args 请求地址参数   {"user","insert"}
         * @return
         */
        public static Result insert(Object bean,String... args){
            try {
                try{
                    jsonForm=gson.toJson(bean);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                jsonResult= OKHttpUtil.postAsyncRequest(baseUrl,jsonForm,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//插入成功

                    }else if(result.getCode()==400){//插入失败
                        result.setInfo("插入失败",null);
                    }
                }

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return result;

        }
        public static Result login(Object bean,String... args){
            try {
                try{
                    jsonForm=gson.toJson(bean);
                    System.out.println(jsonForm);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                jsonResult= OKHttpUtil.postAsyncRequest(baseUrl,jsonForm,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//登录成功

                    }else if(result.getCode()==400){//登录失败
                       result.setInfo("登录失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
                return result;
        }

        /**
         * 通过异步delete请求
         * 数据库插入操作----通过 id 删除
         * @param args 请求地址参数 args[]={"user","delete",id}
         * @return
         */
        public static Result deleteById(String... args){
            try {
                jsonForm= "";
                jsonResult= OKHttpUtil.deleteAsyncRequest(baseUrl,jsonForm,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//删除失败

                    }else if(result.getCode()==400){//删除失败
                        result.setInfo("删除失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
                return result;
        }
        /**
         * 通过异步put请求
         * 数据库更新操作----通过 id 改    ---例子
         * @param bean                     user
         * @param args                    {"user","update"}
         * @return
         */
        public static Result updateById(Object bean,String... args){
            try {
                try{
                    jsonForm=gson.toJson(bean);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                jsonResult= OKHttpUtil.putAsyncRequest(baseUrl,jsonForm,args);
                if(jsonResult==null){//请求失败
                    result.setInfo("发送请求错误！",null);;
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//插入成功

                    }else if(result.getCode()==400){//
                        result.setInfo("插入失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

                return result;

        }

        /**
         * 数据库操作--查询，查询单个对象
         * @param args 请求地址参数 {"user","line",id}
         * @return
         */
        public static Result selectById(String... args){
            try {
                jsonResult= OKHttpUtil.getAsyncRequest(baseUrl,args);
                if(jsonResult==null){//请求失败
                    result.setMsg("发送请求错误！");
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//查询成功

                    }else if(result.getCode()==400){//查询失败
                        result.setInfo("查询失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();

        }
            return result;
        }

        /**
         * 数据库操作--查询，查询对象集合
         * @param args 请求地址参数 {"user","list"}
         * @return
         */
        public static Result selectList(String... args){
            try {
                jsonResult= OKHttpUtil.getAsyncRequest(baseUrl,args);
                if(jsonResult==null){//请求失败
                    result.setMsg("发送请求错误！");
                }else{//请求成功
                    result=gson.fromJson(jsonResult,Result.class);
                    if(result.getCode()==200){//查询成功

                    }else if(result.getCode()==400){//查询失败
                        result.setInfo("查询失败",null);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return result;
        }
        /**
         * 数据库操作--查询，查询单个对象
         * @param args 请求地址参数 {"user","line",id}
         * @return
         */
        public static Result selectUserAddressbyUserid(String... args){
            try {
                jsonResult= OKHttpUtil.getAsyncRequest(baseUrl,args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(jsonResult==null){//请求失败
                result.setMsg("发送请求错误！");
            }else{//请求成功
                result=gson.fromJson(jsonResult,Result.class);
                if(result.getCode()==200){//查询成功

                }else if(result.getCode()==400){//查询失败

                }
            }
            return result;
        }



        /**
         * 通过Result获取bean对象
         * @param result
         * @param cls
         * @param <T>
         * @return
         */
        public static <T> T getEntity(Result result,Class<?> cls){
            T entity=null;
            try {
                entity= (T) gson.fromJson(result.getResult(),cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return entity;
        }


        /**
         * 通过Result获取对象集合
         * @param result
         * @return
         */
        public static <T> List<T> getObjectList(Result result, Class<T> cls) {
            List<T> list=new ArrayList<>();
            try{
                JsonArray arry =  JsonParser.parseString(result.getResult()).getAsJsonArray();
                for(JsonElement jsonElement:arry){
                    list.add(gson.fromJson(jsonElement,cls));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }
        //DatabaseUtil.getObjectList2(result,Product[].class);
        public static <T> List<T> getObjectList2(Result result, Class<T[]> cls) {

           T[] list=  gson.fromJson(result.getResult(),cls);
            return  Arrays.asList(list);
        }

        public static boolean  isNetworkConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    System.out.println("网络可用");
                    return mNetworkInfo.isAvailable();
                }
            }
            return false;
        }
        public static boolean isWifiConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        /**
         * 判断MOBILE网络是否可用
         *
         * @param context
         * @return
         */
        public static boolean isMobileConnected(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    return mMobileNetworkInfo.isAvailable();
                }
            }
            return false;
        }

        /**
         * 获取当前网络连接的类型信息
         *
         * @param context
         * @return
         */
        public static int getConnectedType(Context context) {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                    return mNetworkInfo.getType();
                }
            }
            return -1;
        }

        /**
         * 连接上一个没有外网连接的WiFi
         * 或者有线就会出现极端的情况
         *
         * @return
         */
        public static final boolean ping() {
            String result = null;
            try {
                String ip = "soulzzz.top";// ping 的地址，可以换成任何一种可靠的外网
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
                // 读取ping的内容，可以不加
                InputStream input = p.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                StringBuffer stringBuffer = new StringBuffer();
                String content = "";
                while ((content = in.readLine()) != null) {
                    stringBuffer.append(content);
                }
                Log.d("------ping-----", "result content : " + stringBuffer.toString());
                // ping的状态
                int status = p.waitFor();
                if (status == 0) {
                    result = "success";
                    return true;
                } else {
                    result = "failed";
                    return false;
                }
            } catch (IOException e) {
                result = "IOException";
                return false;
            } catch (InterruptedException e) {
                result = "InterruptedException";
                return false;
            } finally {
                Log.d("----result---", "result = " + result);
            }
        }
    }
