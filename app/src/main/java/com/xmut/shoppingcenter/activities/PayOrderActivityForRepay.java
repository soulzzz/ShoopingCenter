package com.xmut.shoppingcenter.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.PayOrderStoreAdapter;
import com.xmut.shoppingcenter.entity.Coupon;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.entity.UserAddress;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.CodeUtil;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.Serializable;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xmut.shoppingcenter.fragments.ShoppingCarFragment.shouldrefresh;

public class PayOrderActivityForRepay extends AppCompatActivity {
    List<Store> storeList;
    Order order;
    RecyclerView rec_product;
    PayOrderStoreAdapter payOrderStoreAdapter;
    List<String> timelist; // 0 for formatted
    TextView  price,pay,address,name,tel,canuse,time,coupon_saved;
    RelativeLayout userinfo,coupon_goto,coupon_saved_part;
    ImageView leave_bt;
    public static int addressCode = 5;
    public static int couponCode = 6;
    UserAddress userAddress;
    Coupon coupon;
    Double originPrice,afterPrice;
    Context context;
    String orderid ;
    List<Coupon> coupons;
    String order_id,product_id;
    int order_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        context = this;
        order_id = getIntent().getStringExtra("order_id");
        product_id = getIntent().getStringExtra("product_id");
        order_state =getIntent().getIntExtra("order_state",1);
        timelist = TimeUtil.getInstance().getFormatTime();
        initData();
        CalculatePrice();
        initCouponData();
        init();



    }


    private void initData(){
        storeList = new ArrayList<>();
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("orderid",order_id);
        paramsMap.put("productid",product_id);
        paramsMap.put("state",String.valueOf(order_state) );
        Result result = DatabaseUtil.postParamters(paramsMap,"order","lineorlist");
        if(result.getCode()!=200){
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            order = DatabaseUtil.getEntity(result,Order.class);
            System.out.println("1   "+ order.getOrderProducts().get(0).getOrder_id() );
            List<Product> products = order.getOrderProducts();
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
                    storeList.add(store);
                    j++;
                }else{
                    //当此一个商品和前一个商品 同属一个store 加入
                    if(products.get(i).getStore_id() == products.get(i-1).getStore_id()  ){
                        storeList.get(j).getStore_products().add(products.get(i));
                    }else{  //当不同属 new一个store 加入stores 并j++;
                        Store store = new Store();
                        store.setStore_name(products.get(i).getStore_name() );
                        store.setStore_id( products.get(i).getStore_id());

                        List<Product> products1 = new ArrayList<>();
                        products1.add(products.get(i));
                        store.setStore_products(products1);
                        storeList.add(store);
                        j++;
                    }
                }
            }
        }
    }

    private void init(){
        coupon_saved_part =findViewById(R.id.coupon_saved_part);
        coupon_saved = findViewById(R.id.coupon_saved);
        price= findViewById(R.id.price);
        price.setText(OrderUtil.PriceFix(afterPrice ) );
        pay  = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null ==userAddress ){
                    Toast.makeText(context, "未选择地址", Toast.LENGTH_SHORT).show();
                }else{
                    order.setOrder_price(afterPrice);
                    List<Product> tempproduct =new ArrayList<>();
                    int totalnum = 0;
                    for(Store store:storeList){
                        for(Product product:store.getStore_products()){
                            totalnum += product.getProduct_num();
                        }
                    }
                    int i =0;
                    for(Store store:storeList){
                        for(Product product:store.getStore_products()){
                            product.setOrder_state(OrderUtil.waitsend());

                            if(coupon!=null){
                                //System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix( OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ), OrderUtil.doubleMutiply(coupon.getValue(),OrderUtil.doubleDivide(product.getProduct_num(),totalnum)  ) )   )) );
                                product.setDetail_price(OrderUtil.PriceFloorFix( OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ), OrderUtil.doubleMutiply(coupon.getValue(),OrderUtil.doubleDivide(product.getProduct_num(),totalnum)  ) )   ));
                            }
                            else{
                                product.setDetail_price(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ) )   );
                                // System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ) )) );
                            }
                            tempproduct.add(product);
                        }
                    }

                    order.setOrderProducts(tempproduct);
                    Result result = DatabaseUtil.insert(order, "order", "insert2");
                    if(result.getCode()!=200)
                    {
                        Toast.makeText(context, "提交订单失败", Toast.LENGTH_SHORT).show();
                    }else{
                        if(coupon!=null){
                            coupon.setUsed(1);
                            coupon.setOrder_id(orderid);
                            Result result2 = DatabaseUtil.updateById(coupon, HttpAddress.get(HttpAddress.Coupon(), "update"));
                            if(result2.getCode()!=200){

                            }else
                                Toast.makeText(context, "提交订单成功", Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(context, "提交订单成功", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    }

                }
            }
        });
        name = findViewById(R.id.name);
        tel = findViewById(R.id.tel);
        address = findViewById(R.id.address);
        canuse = findViewById(R.id.canuse);
        if(!isCouponable() ){
            canuse.setText("暂无可用优惠券");
        }
        time = findViewById(R.id.time);
        time.setText(timelist.get(0));
        coupon_goto =findViewById(R.id.coupon_goto);
        coupon_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,ChooseCouponActivity.class);
                it.putExtra("price",originPrice);
                it.putExtra("coupon", (Serializable) coupons);
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                    Intent data = result.getData();
                    int resultCode = result.getResultCode();
                    if(resultCode ==couponCode){
                        coupon = (Coupon)data.getSerializableExtra("coupon");
                        canuse.setText("当前已优惠 -"+coupon.getValue() );
                        afterPrice = originPrice - coupon.getValue();
                        price.setText(OrderUtil.PriceFix(afterPrice) );
                        coupon_saved.setText("¥"+coupon.getValue());
                        coupon_saved_part.setVisibility(View.VISIBLE);

                    }else{
//                        Toast.makeText(PayOrderActivity.this, "未选优惠券", Toast.LENGTH_SHORT).show();

                    }

                }).launch(it);
            }
        });
        userinfo = findViewById(R.id.userinfo);
        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent data = result.getData();
                    int resultCode = result.getResultCode();
                    if(resultCode ==addressCode){
                        userAddress = (UserAddress)data.getSerializableExtra("useraddress");
                        name.setText(userAddress.getUser_name());
                        address.setText(userAddress.getUser_address());
                        tel.setText(userAddress.getUser_tel());

                    }else{
//                        Toast.makeText(PayOrderActivity.this, "没选地址", Toast.LENGTH_SHORT).show();

                    }

                }).launch(new Intent(context, ChooseAddressActivity.class));
            }
        });
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }

        });
        rec_product =  findViewById(R.id.rec_product);
        payOrderStoreAdapter =new PayOrderStoreAdapter(this,storeList);
        rec_product.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rec_product.setAdapter(payOrderStoreAdapter);


    }
    private void CalculatePrice(){
        double temp = 0;
        for(int i=0;i<storeList.size();i++){
            for(int j = 0;j<storeList.get(i).getStore_products().size();j++){

                temp = OrderUtil.doubleAdd(temp,OrderUtil.doubleMutiply(storeList.get(i).getStore_products().get(j).getProduct_price(),storeList.get(i).getStore_products().get(j).getProduct_num() ) );

            }
        }
        temp = OrderUtil.PriceFloorFix(temp);
        originPrice = temp;
        afterPrice = originPrice;
    }
    private void initCouponData(){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("username",(UserManage.getInstance().getUserInfo(context).getUsername() ));
        paramsMap.put("used",String.valueOf(0));
        Result result  = DatabaseUtil.postParamters(paramsMap ,HttpAddress.Coupon(),"list" );
        if(result.getCode()!=200){
            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
        }else{
            coupons =DatabaseUtil.getObjectList(result,Coupon.class);
            coupons.sort(new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {
                    if(o2.getValue()-o1.getValue() ==0 )
                        return 0;
                    return o2.getValue()-o1.getValue()>0?1:-1;
                }
            });
        }
    }
    private boolean isCouponable() {
        if(null != coupons)
            for (Coupon coupon : coupons) {
                if (coupon.getStarttime().compareTo(timelist.get(1)) < 0 && coupon.getEndtime().compareTo(timelist.get(1)) > 0 && coupon.getRequirement() < originPrice) {
                    return  true;
                }
            }
        return false;
    }
}