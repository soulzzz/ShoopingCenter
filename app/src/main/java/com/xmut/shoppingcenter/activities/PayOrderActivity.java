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
import android.provider.ContactsContract;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xmut.shoppingcenter.fragments.ShoppingCarFragment.shouldrefresh;

public class PayOrderActivity extends AppCompatActivity {
    List<Store> storeList;
    RecyclerView rec_product;
    PayOrderStoreAdapter payOrderStoreAdapter;
    List<String> timelist; // 0 for formatted
    TextView price, pay, address, name, tel, canuse, time, coupon_saved;
    RelativeLayout userinfo, coupon_goto, coupon_saved_part;
    ImageView leave_bt;
    public static int addressCode = 5;
    public static int couponCode = 6;
    UserAddress userAddress;
    Coupon coupon;
    Double originPrice, afterPrice;
    Context context;
    String orderid;
    List<Coupon> coupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        context = this;
        storeList = (List<Store>) getIntent().getSerializableExtra("order");
        CalculatePrice();
        initCouponData();
        timelist = TimeUtil.getInstance().getFormatTime();
        orderid = CodeUtil.createOrderId();
        init();


    }


    private void init() {
        coupon_saved_part = findViewById(R.id.coupon_saved_part);
        coupon_saved = findViewById(R.id.coupon_saved);
        price = findViewById(R.id.price);
        price.setText(OrderUtil.PriceFix(afterPrice));
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == userAddress) {
                    Toast.makeText(PayOrderActivity.this, "未选择收货地址", Toast.LENGTH_SHORT).show();
                } else {
                    //插入订单
                    Order order = new Order();
                    order.setOrder_id(orderid);
                    order.setUser_id(UserManage.getInstance().getUserInfo(context).getId());
                    order.setUser_address(userAddress.getUser_address());
                    order.setUser_name(userAddress.getUser_name());
                    order.setUser_tel(userAddress.getUser_tel());

                    order.setOrder_price(afterPrice);
                    order.setOrder_date(timelist.get(1));
                    List<Product> tempproduct = new ArrayList<>();
                    int totalnum = 0;
                    for (Store store : storeList) {
                        for (Product product : store.getStore_products()) {
                            totalnum += product.getProduct_num();
                        }
                    }
                    int i = 0;
                    for (Store store : storeList) {
                        for (Product product : store.getStore_products()) {
                            product.setOrder_state(OrderUtil.waitsend());
                            product.setOrder_id(orderid);
                            product.setProduct_sales(product.getProduct_sales()+1);

                            if (coupon != null) {
                                //System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix( OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ), OrderUtil.doubleMutiply(coupon.getValue(),OrderUtil.doubleDivide(product.getProduct_num(),totalnum)  ) )   )) );
                                product.setDetail_price(OrderUtil.PriceFloorFix(OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(), product.getProduct_num()),
                                        OrderUtil.doubleMutiply(coupon.getValue(), OrderUtil.doubleDivide(product.getProduct_num(), totalnum)))));
                            } else {
                                product.setDetail_price(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(), product.getProduct_num())));
                                // System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ) )) );
                            }
                            tempproduct.add(product);
                        }
                    }
                    order.setOrderProducts(tempproduct);
                    Result result = DatabaseUtil.insert(order, HttpAddress.get(HttpAddress.order(), "insert"));
                    if (result.getCode() != 200) {
                        Toast.makeText(context, "提交订单失败", Toast.LENGTH_SHORT).show();
                    } else {
                        if (coupon != null) {
                            coupon.setUsed(1);
                            coupon.setOrder_id(orderid);
                            Result result2 = DatabaseUtil.updateById(coupon, HttpAddress.get(HttpAddress.Coupon(), "update"));
                            if (result2.getCode() != 200) {

                            } else
                                Toast.makeText(context, "提交订单成功", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(context, "提交订单成功", Toast.LENGTH_SHORT).show();
                        }
                        for (Product product : tempproduct) {
                            Map<String, String> paramsMap = new HashMap<>();
                            paramsMap.put("product_id", product.getProduct_id());
                            paramsMap.put("user_id", String.valueOf(UserManage.getInstance().getUserInfo(context).getId()));
                            Result result1 = DatabaseUtil.postParamters(paramsMap, HttpAddress.shoppingcar(), "delete2");
                            Result result2 = DatabaseUtil.updateById(tempproduct,"product","updatelistssales");
                            shouldrefresh = true;

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
        if (!isCouponable()) {
            canuse.setText("暂无可用优惠券");
        }
        time = findViewById(R.id.time);
        time.setText(timelist.get(0));
        coupon_goto = findViewById(R.id.coupon_goto);
        coupon_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(PayOrderActivity.this, ChooseCouponActivity.class);
                it.putExtra("price", originPrice);
                it.putExtra("coupon", (Serializable) coupons);
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                    Intent data = result.getData();
                    int resultCode = result.getResultCode();
                    if (resultCode == couponCode) {
                        coupon = (Coupon) data.getSerializableExtra("coupon");
                        canuse.setText("当前已优惠 -" + coupon.getValue());
                        afterPrice = originPrice - coupon.getValue();
                        price.setText(OrderUtil.PriceFix(afterPrice));
                        coupon_saved.setText("¥" + coupon.getValue());
                        coupon_saved_part.setVisibility(View.VISIBLE);

                    } else {
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
                    if (resultCode == addressCode) {
                        userAddress = (UserAddress) data.getSerializableExtra("useraddress");
                        name.setText(userAddress.getUser_name());
                        address.setText(userAddress.getUser_address());
                        tel.setText(userAddress.getUser_tel());

                    } else {
//                        Toast.makeText(PayOrderActivity.this, "没选地址", Toast.LENGTH_SHORT).show();

                    }

                }).launch(new Intent(PayOrderActivity.this, ChooseAddressActivity.class));
            }
        });
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAddress != null) { //选择地址后才可以保存订单 没有的话直接返回

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否保存订单？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Order order = new Order();
                            order.setOrder_id(orderid);
                            order.setUser_id(UserManage.getInstance().getUserInfo(context).getId());
                            order.setUser_address(userAddress.getUser_address());
                            order.setUser_name(userAddress.getUser_name());
                            order.setUser_tel(userAddress.getUser_tel());

                            order.setOrder_price(afterPrice);
                            order.setOrder_date(timelist.get(1));
                            List<Product> tempproduct = new ArrayList<>();
                            int totalnum = 0;
                            for (Store store : storeList) {
                                for (Product product : store.getStore_products()) {
                                    totalnum += product.getProduct_num();
                                }
                            }
                            int i = 0;
                            for (Store store : storeList) {
                                for (Product product : store.getStore_products()) {
                                    product.setOrder_state(OrderUtil.waitpay());
                                    product.setOrder_id(orderid);

                                    if (coupon != null) {
                                        //System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix( OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ), OrderUtil.doubleMutiply(coupon.getValue(),OrderUtil.doubleDivide(product.getProduct_num(),totalnum)  ) )   )) );
                                        product.setDetail_price(OrderUtil.PriceFloorFix(OrderUtil.doublesubtract(OrderUtil.doubleMutiply(product.getProduct_price(), product.getProduct_num()), OrderUtil.doubleMutiply(coupon.getValue(), OrderUtil.doubleDivide(product.getProduct_num(), totalnum)))));
                                    } else {
                                        product.setDetail_price(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(), product.getProduct_num())));
                                        // System.out.println(totalnum +"第"+i++ +" 个 "+(OrderUtil.PriceFloorFix(OrderUtil.doubleMutiply(product.getProduct_price(),product.getProduct_num() ) )) );
                                    }
                                    tempproduct.add(product);
                                }
                            }

                            order.setOrderProducts(tempproduct);
                            Result result = DatabaseUtil.insert(order, HttpAddress.get(HttpAddress.order(), "insert"));
                            if (result.getCode() != 200) {
                                Toast.makeText(context, "保存订单失败", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(context, "保存订单成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
                    //    设置一个NegativeButton
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });
                    builder.show();
                } else {
                    finish();
                }
            }

        });
        rec_product = findViewById(R.id.rec_product);
        payOrderStoreAdapter = new PayOrderStoreAdapter(this, storeList);
        rec_product.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rec_product.setAdapter(payOrderStoreAdapter);


    }

    private void CalculatePrice() {
        double temp = 0;
        for (int i = 0; i < storeList.size(); i++) {
            for (int j = 0; j < storeList.get(i).getStore_products().size(); j++) {
                if (storeList.get(i).getStore_products().get(j).isSelected()) {
                    temp = OrderUtil.doubleAdd(temp, OrderUtil.doubleMutiply(storeList.get(i).getStore_products().get(j).getProduct_price(), storeList.get(i).getStore_products().get(j).getProduct_num()));
                }
            }
        }
        temp = OrderUtil.PriceFloorFix(temp);
        originPrice = temp;
        afterPrice = originPrice;
    }

    private void initCouponData() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", (UserManage.getInstance().getUserInfo(context).getUsername()));
        paramsMap.put("used", String.valueOf(0));
        Result result = DatabaseUtil.postParamters(paramsMap, HttpAddress.Coupon(), "list");
        if (result.getCode() != 200) {
            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
        } else {
            coupons = DatabaseUtil.getObjectList(result, Coupon.class);
            coupons.sort(new Comparator<Coupon>() {
                @Override
                public int compare(Coupon o1, Coupon o2) {
                    if (o2.getValue() - o1.getValue() == 0)
                        return 0;
                    return o2.getValue() - o1.getValue() > 0 ? 1 : -1;
                }
            });
        }
    }

    private boolean isCouponable() {
        if (null != coupons)
            for (Coupon coupon : coupons) {
                if (coupon.getStarttime().compareTo(timelist.get(1)) < 0 && coupon.getEndtime().compareTo(timelist.get(1)) > 0 && coupon.getRequirement() < originPrice) {
                    return true;
                }
            }
        return false;
    }
}