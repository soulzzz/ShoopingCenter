package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckOrderStoreAdapter;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.fragments.PersonalityFragment;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckOrderActivity extends AppCompatActivity {
    TextView nullshow, allclick, waitpay, waitsend, waitreceive, success,refund;
    ImageView leave_bt;
    RecyclerView rec_order;
    Context context;
    User user;
    int curitem = -1;
    static boolean shouldrefresh =false;

    CheckOrderStoreAdapter checkOrderStoreAdapter;
    List<Store> orderstores;

    @Override
    public void finish() {
        PersonalityFragment.shouldRefresh = true;
        super.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        context = this;
        user = UserManage.getInstance().getUserInfo(context);
        checkOrderStoreAdapter =new CheckOrderStoreAdapter(context);
        init();
        int clickwhich = Integer.parseInt( (String)getIntent().getSerializableExtra("choice"));
        switch (clickwhich) {
            case 0:
                allclick.performClick();
                break;
            case 1:
                waitpay.performClick();
                break;
            case 2:
                waitsend.performClick();
                break;
            case 3:
                waitreceive.performClick();
                break;
            case 4:
                success.performClick();
                break;
            case 5:
                refund.performClick();
                break;
            default:
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldrefresh && curitem!=-1){
            orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), curitem);
            checkOrderStoreAdapter.setStoreList(orderstores);
            shouldrefresh = false;
        }
        checkOrderStoreAdapter.setFirstListener(new CheckOrderStoreAdapter.FirstListener() {
            @Override
            public void onUpdate(Product product) {
                //待付款的订单 进入PayOrderActivity
                if(product.getOrder_state()!=OrderUtil.waitpay())
                    UserManage.getInstance().Jump(CheckOrderActivity.this,CheckOrderDetailActivity.class,product,"product");
                else{

                    Intent it = new Intent(context,PayOrderActivityForRepay.class);
                    it.putExtra("order_id",product.getOrder_id() );
                    it.putExtra("product_id",product.getProduct_id() );
                    it.putExtra("order_state",product.getOrder_state() );
                    startActivity(it);
                }
            }
        });
    }

    private void init() {
        refund = findViewById(R.id.refund);
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 5;
                orderstores = getRefundOrderByUserid(UserManage.getInstance().getUserInfo(context).getId() );
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);
            }
        });
        rec_order = findViewById(R.id.rec_order);
        rec_order.setAdapter(checkOrderStoreAdapter);

        rec_order.setLayoutManager(new LinearLayoutManager(context));

        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nullshow = findViewById(R.id.nullshow);

        allclick = findViewById(R.id.allclick);
        allclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 0;
                orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), OrderUtil.all());
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);
            }
        });
        waitpay = findViewById(R.id.waitpay);
        waitpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 1;
                orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), OrderUtil.waitpay());
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);
            }
        });
        waitsend = findViewById(R.id.waitsend);
        waitsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 2;
                orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), OrderUtil.waitsend());
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);
            }
        });
        waitreceive = findViewById(R.id.waitreceive);
        waitreceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 3;
                orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), OrderUtil.waitreceive());
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);
            }
        });
        success = findViewById(R.id.success);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curitem = 4;
                orderstores = getOrderByUseridAndState(UserManage.getInstance().getUserInfo(context).getId(), OrderUtil.success());
                checkOrderStoreAdapter.setStoreList(orderstores);
                setSelect(curitem);


            }
        });


    }
        //allclick, waitpay, waitsend, waitreceive, success;
    public void setSelect(int num){
        switch (num){
            case 0:
                allclick.setTextColor(Color.parseColor("#F1824E"));
                waitpay.setTextColor(Color.parseColor("#B0000000"));
                waitsend.setTextColor(Color.parseColor("#B0000000"));
                waitreceive.setTextColor(Color.parseColor("#B0000000"));
                success.setTextColor(Color.parseColor("#B0000000"));
                refund.setTextColor(Color.parseColor("#B0000000"));
                break;
            case 1:
                allclick.setTextColor(Color.parseColor("#B0000000"));
                waitpay.setTextColor(Color.parseColor("#F1824E"));
                waitsend.setTextColor(Color.parseColor("#B0000000"));
                waitreceive.setTextColor(Color.parseColor("#B0000000"));
                success.setTextColor(Color.parseColor("#B0000000"));
                refund.setTextColor(Color.parseColor("#B0000000"));
                break;
            case 2:
                allclick.setTextColor(Color.parseColor("#B0000000"));
                waitpay.setTextColor(Color.parseColor("#B0000000"));
                waitsend.setTextColor(Color.parseColor("#F1824E"));
                waitreceive.setTextColor(Color.parseColor("#B0000000"));
                success.setTextColor(Color.parseColor("#B0000000"));
                refund.setTextColor(Color.parseColor("#B0000000"));
                break;
            case 3:
                allclick.setTextColor(Color.parseColor("#B0000000"));
                waitpay.setTextColor(Color.parseColor("#B0000000"));
                waitsend.setTextColor(Color.parseColor("#B0000000"));
                waitreceive.setTextColor(Color.parseColor("#F1824E"));
                success.setTextColor(Color.parseColor("#B0000000"));
                refund.setTextColor(Color.parseColor("#B0000000"));
                break;
            case 4:
                allclick.setTextColor(Color.parseColor("#B0000000"));
                waitpay.setTextColor(Color.parseColor("#B0000000"));
                waitsend.setTextColor(Color.parseColor("#B0000000"));
                waitreceive.setTextColor(Color.parseColor("#B0000000"));
                success.setTextColor(Color.parseColor("#F1824E"));
                refund.setTextColor(Color.parseColor("#B0000000"));
                break;
            case 5:
                allclick.setTextColor(Color.parseColor("#B0000000"));
                waitpay.setTextColor(Color.parseColor("#B0000000"));
                waitsend.setTextColor(Color.parseColor("#B0000000"));
                waitreceive.setTextColor(Color.parseColor("#B0000000"));
                success.setTextColor(Color.parseColor("#B0000000"));
                refund.setTextColor(Color.parseColor("#F1824E"));
                break;

        }
        if (orderstores == null || orderstores.isEmpty()) {
            nullshow.setVisibility(View.VISIBLE);
        } else {
            nullshow.setVisibility(View.GONE);
        }
    }

    private List<Store> getRefundOrderByUserid(int userid) {

        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("user_id",String.valueOf(userid));

        Result result = DatabaseUtil.postParamters(paramsMap,"order","list","refundbyuserid");
        List<Store> stores = new ArrayList<>();
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取订单失败", Toast.LENGTH_SHORT).show();
        } else {
            //因为state相同 所以不考虑退款的商品
            //同一订单 同一个商店归并 不同商店另起一个item加入store
            List<Order> temp = DatabaseUtil.getObjectList(result, Order.class);
            int j=-1;
            for(Order o:temp){
                List<Product> products = o.getOrderProducts();

                Map<Integer,List<Product> > stringListMap = products.stream().collect(Collectors.groupingBy(Product::getStore_id));
                products.clear();
                //将map集合转为set集合遍历
                Set<Map.Entry<Integer, List<Product>>> entries = stringListMap.entrySet();
                for(Map.Entry m : entries){
                    products.addAll((List<Product>)m.getValue() ) ;
                }
                for(int i=0;i<products.size();i++){

                    if(i==0){
                        Store store = new Store();
                        store.setStore_name(products.get(i).getStore_name() );
                        store.setStore_id( products.get(i).getStore_id());
                        List<Product> products1 = new ArrayList<>();
                        products1.add(products.get(i));
                        store.setStore_products(products1);
                        stores.add(store);

                        j++;
                    }else{
                        //当此一个商品和前一个商品 同属一个store并且state相同 加入
                        if(products.get(i).getStore_id() == products.get(i-1).getStore_id() && products.get(i).getOrder_state() == products.get(i-1).getOrder_state() ){
                            stores.get(j).getStore_products().add(products.get(i));

                        }else{  //当不同属 new一个store 加入stores 并j++;
                            Store store = new Store();

                            store.setStore_name(products.get(i).getStore_name() );
                            store.setStore_id( products.get(i).getStore_id());

                            List<Product> products1 = new ArrayList<>();
                            products1.add(products.get(i));
                            store.setStore_products(products1);
                            stores.add(store);

                            j++;
                        }
                    }
                }

            }
        }
        return  stores;
    }

    private List<Store> getOrderByUseridAndState(int userid, int state) {

        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",String.valueOf(userid));
        paramsMap.put("state",String.valueOf(state));
        Result result = DatabaseUtil.postParamters(paramsMap,"order","list","byuseridandstate");
        List<Store> stores = new ArrayList<>();
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取订单失败", Toast.LENGTH_SHORT).show();
        } else {
            //因为state相同 所以不考虑退款的商品
            //同一订单 同一个商店归并 不同商店另起一个item加入store
            List<Order> temp = DatabaseUtil.getObjectList(result, Order.class);
            int j=-1;
            for(Order o:temp){
                List<Product> products = o.getOrderProducts();

                Map<Integer,List<Product> > stringListMap = products.stream().collect(Collectors.groupingBy(Product::getStore_id));
                products.clear();
                //将map集合转为set集合遍历
                Set<Map.Entry<Integer, List<Product>>> entries = stringListMap.entrySet();
                for(Map.Entry m : entries){
                    products.addAll((List<Product>)m.getValue() ) ;
                }
                for(int i=0;i<products.size();i++){

                    if(i==0){
                        Store store = new Store();
                        store.setStore_name(products.get(i).getStore_name() );
                        store.setStore_id( products.get(i).getStore_id());
                        List<Product> products1 = new ArrayList<>();
                        products1.add(products.get(i));
                        store.setStore_products(products1);
                        stores.add(store);

                        j++;
                    }else{
                        //当此一个商品和前一个商品 同属一个store并且state相同 加入
                        if(products.get(i).getStore_id() == products.get(i-1).getStore_id() && products.get(i).getOrder_state() == products.get(i-1).getOrder_state() ){
                            stores.get(j).getStore_products().add(products.get(i));

                        }else{  //当不同属 new一个store 加入stores 并j++;
                            Store store = new Store();

                            store.setStore_name(products.get(i).getStore_name() );
                            store.setStore_id( products.get(i).getStore_id());

                            List<Product> products1 = new ArrayList<>();
                            products1.add(products.get(i));
                            store.setStore_products(products1);
                            stores.add(store);

                            j++;
                        }
                    }
                }

            }
    }
        return  stores;
}
}