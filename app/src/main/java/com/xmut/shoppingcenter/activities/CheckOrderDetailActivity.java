package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckExpressAdapter;
import com.xmut.shoppingcenter.adapter.CheckOrderDetailStoreAdapter;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.DialogUitl;
import com.xmut.shoppingcenter.utils.ListUtil;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckOrderDetailActivity extends AppCompatActivity {
    Context context;
    Order order;
    List<Store> stores;
    List<Product> products;
    Product product;
    TextView state,check_express,time,orderid,address,order_title ,check_reason;
    RecyclerView recyclerview;
    CheckOrderDetailStoreAdapter checkOrderDetailStoreAdapter;
    ImageView leaveorder;
    ClipboardManager  mClipboardManager;
    public static CheckOrderDetailActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order_detail);
        instance =this;
        context =this;
        product =  (Product)getIntent().getSerializableExtra("product");
        initData();
        if(order!=null) {
            try {
                init();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void initData(){
        stores = new ArrayList<>();
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("orderid",product.getOrder_id());
        paramsMap.put("productid",product.getProduct_id());
        paramsMap.put("state",String.valueOf(product.getOrder_state() ));
        Result result = DatabaseUtil.postParamters(paramsMap,"order","lineorlist");
        if(result.getCode()!=200){
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            order = DatabaseUtil.getEntity(result,Order.class);
            if(order ==null){
                Toast.makeText(context, "暂无该订单信息", Toast.LENGTH_SHORT).show();
                finish();
            }
             products = order.getOrderProducts();
            Map<Integer,List<Product> > stringListMap = products.stream().collect(Collectors.groupingBy(Product::getStore_id));
            products.clear();
            //将map集合转为set集合遍历
            Set<Map.Entry<Integer, List<Product>>> entries = stringListMap.entrySet();
            for(Map.Entry m : entries){
                products.addAll((List<Product>)m.getValue() ) ;
            }
            for(int i=0,j=-1;i<products.size();i++){
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
                    //当此一个商品和前一个商品 同属一个store 加入
                    if(products.get(i).getStore_id() == products.get(i-1).getStore_id()  ){
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
    private void init() throws IOException, ClassNotFoundException {
        check_reason = findViewById(R.id.check_reason);
        leaveorder = findViewById(R.id.leaveorder);
        leaveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        order_title = findViewById(R.id.order_title);
        state = findViewById(R.id.state);
        check_express = findViewById(R.id.check_express);

        recyclerview = findViewById(R.id.recyclerview);
        time = findViewById(R.id.time);
        address = findViewById(R.id.address);
        orderid = findViewById(R.id.orderid);
        orderid.setText("订单编号: "+order.getOrder_id());
        order_title.setText(OrderUtil.getStateName(stores.get(0).getStore_products().get(0).getOrder_state() ) );
        time.setText("订单时间: "+ TimeUtil.getInstance().FormatHourTime( order.getOrder_date()));
        address.setText("收货信息: "+order.getUser_name()+" "+order.getUser_tel()+" "+order.getUser_address());

        checkOrderDetailStoreAdapter = new CheckOrderDetailStoreAdapter(context);
        checkOrderDetailStoreAdapter.setStoreList(stores);
        recyclerview.setAdapter(checkOrderDetailStoreAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

        if(stores.get(0).getStore_products().get(0).getOrder_state() == OrderUtil.waitsend()){
            state.setText("待发货");
        }
        else if(stores.get(0).getStore_products().get(0).getOrder_state() == OrderUtil.waitreceive()){
            check_express.setVisibility(View.VISIBLE);
            check_express.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                      mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);




                            Dialog dialog1 = new Dialog(context);
                            View contentView1 = LayoutInflater.from(context).inflate(
                                    R.layout.dialog_showexpress, null);
                            dialog1.setContentView(contentView1);
                            RecyclerView expresses = contentView1.findViewById(R.id.recyclerview);

                            CheckExpressAdapter checkExpressAdapter = new CheckExpressAdapter(context);

                            checkExpressAdapter.setProductList(products);
                    checkExpressAdapter.setmClipboardManager(mClipboardManager);



                            expresses.setAdapter(checkExpressAdapter);
                            expresses.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
                            Button close_bt = contentView1.findViewById(R.id.close_bt);
                            close_bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                            DialogUitl.getInstance().setDialogTransparentandCircleFromBottom(dialog1);
                            DialogUitl.getInstance().setDialogMatchParentWidthAndHeightPersantage(context, dialog1,1.0,0.6);
                            dialog1.show();


                }
            });

            state.setText("确认收货");
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Product> products =new ArrayList<>();
                    for(Store s:stores){
                        for(Product p:s.getStore_products() ){
                            p.setOrder_state(OrderUtil.success() );
                            p.setProduct_sales( p.getProduct_sales()+1);
                            products.add(p);
                        }
                    }

                    Result result = DatabaseUtil.updateById(products,"order","updatelist" );
                    if(result.getCode()!=200){
                        Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        CheckOrderActivity.shouldrefresh = true;
                        Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                        Result result1 = DatabaseUtil.updateById(products,HttpAddress.get(HttpAddress.product(),"updatelistssales") );
                        finish();
                    }
                }
            });
        }

        else if(stores.get(0).getStore_products().get(0).getOrder_state() >= OrderUtil.refundwaitsend() && stores.get(0).getStore_products().get(0).getOrder_state() <= OrderUtil.refundafterreceivedandcommented() ){
            state.setText("取消退款");
            check_reason.setVisibility(View.VISIBLE);
            check_reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserManage.getInstance().Jump(CheckOrderDetailActivity.this,CheckReasonActivity.class,products.get(0),"product");
                }
            });
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = null;
                    try {
                        product = (Product) ListUtil.deepCopy1( stores.get(0).getStore_products().get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(stores.get(0).getStore_products().get(0).getOrder_state()  ==OrderUtil.refundwaitsend()){
                        product.setOrder_state(OrderUtil.waitsend());
                    }else if(stores.get(0).getStore_products().get(0).getOrder_state()  ==OrderUtil.refundwaitreceive()){
                        product.setOrder_state(OrderUtil.waitreceive());
                    }else if(stores.get(0).getStore_products().get(0).getOrder_state()  ==OrderUtil.refundafterreceived() )
                        product.setOrder_state(OrderUtil.success());
                    else if(stores.get(0).getStore_products().get(0).getOrder_state()  ==OrderUtil.refundafterreceivedandcommented() )
                        product.setOrder_state(OrderUtil.successcomment());
                    Result result = DatabaseUtil.updateById(product,HttpAddress.get(HttpAddress.order(),"update"));
                    if(result.getCode()!=200){
                        Toast.makeText(context, "取消退款失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "取消退款成功", Toast.LENGTH_SHORT).show();
                        CheckOrderActivity.shouldrefresh = true;
                        finish();
                    }
                }
            });


        }
        else if(stores.get(0).getStore_products().get(0).getOrder_state() ==OrderUtil.success() ){
            state.setText("待评价");
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserManage.getInstance().Jump((Activity)context,CommentProductActivity.class,(Serializable)products,"product");
                }
            });
        }else if(stores.get(0).getStore_products().get(0).getOrder_state() ==OrderUtil.refundsuccess())
            state.setText("退款成功");
        else if(stores.get(0).getStore_products().get(0).getOrder_state() ==OrderUtil.successcomment())
            state.setText("已完成");

        }


}