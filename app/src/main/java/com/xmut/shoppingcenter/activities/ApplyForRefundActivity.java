package com.xmut.shoppingcenter.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.GridImageAdapter;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.OrderReason;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.AvatarUtil;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyForRefundActivity extends AppCompatActivity {
    String order_id, product_id;
    int order_state;
    RecyclerView recyclerView;
    ImageView leaveorder;
    TextView order_id1, reason, uploadImg,product_name,product_num,product_price;
    RadiuImageView product_icon;
    Context context;
    List<Uri> myresult = new ArrayList<>();
    GridImageAdapter gridImageAdapter;
    Order order;
    TextView submit;
    public static int imgrequestCode = 10;
    String imgPath ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_refund);
        context = this;
        order_id = getIntent().getStringExtra("order_id");
        product_id = getIntent().getStringExtra("product_id");
        order_state = getIntent().getIntExtra("order_state", -1);

        initData();
        init();

    }

    private void initData() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("orderid", order_id);
        paramsMap.put("productid", product_id);
        paramsMap.put("state", String.valueOf(order_state));
        Result result = DatabaseUtil.postParamters(paramsMap, "order", "lineforrefund");
        if (result.getCode() != 200) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            order = DatabaseUtil.getEntity(result, Order.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

            uploadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myresult.size()<5){
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        //把所有照片显示出来
                        intent.setType("image/*");
                        startActivityForResult(intent, imgrequestCode);
                    }else{
                        Toast.makeText(context, "图片已超过5张", Toast.LENGTH_SHORT).show();
                    }

                }
            });


    }

    public void init () {

        leaveorder = findViewById(R.id.leaveorder);
        leaveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        product_price = findViewById(R.id.product_price);
        product_price.setText(OrderUtil.PriceFix( order.getOrderProducts().get(0).getDetail_price()));

            product_icon = findViewById(R.id.product_icon);
            Glide.with(context).load(order.getOrderProducts().get(0).getProduct_img() ).into(product_icon);
            recyclerView = findViewById(R.id.rec_img);
            order_id1 = findViewById(R.id.order_id);
            order_id1.setText("订单编号: "+order.getOrder_id() );
        product_name =findViewById(R.id.product_name);
        product_name.setText(order.getOrderProducts().get(0).getProduct_name() );
        product_num = findViewById(R.id.product_num);
        product_num.setText(String.valueOf( "X"+ order.getOrderProducts().get(0).getProduct_num() ));

            reason = findViewById(R.id.reason);
            submit = findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UserManage.isEmpty( reason.getText().toString() )){
                        Toast.makeText(context, "请输入申请理由", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for(int i=0;i<myresult.size();i++){


                        Bitmap imgbitmap = null;
                        try {
                            imgbitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), myresult.get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Result result1 = DatabaseUtil.uploadFile(AvatarUtil.getInstance().getFile(imgbitmap),"file","upload");
                        if(result1.getCode()!=200){
                            Toast.makeText(context, "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            if(imgPath !=""){
                                imgPath +=('|'+result1.getResult() );
                            }else{
                                imgPath +=result1.getResult();
                            }
                        }
                    }
                    OrderReason orderReason = new OrderReason();
                    orderReason.setCommit_date(TimeUtil.getInstance().getFormatTime().get(1));
                    orderReason.setCommit_img(imgPath);
                    orderReason.setCommit_person(0);
                    orderReason.setCommit_reason(reason.getText().toString() );
                    orderReason.setOrder_id(order_id);
                    orderReason.setProduct_id(product_id);
                    Result result = DatabaseUtil.insert(orderReason, HttpAddress.get(HttpAddress.Orderreason(),"insert"));
                    if(result.getCode()!=200){
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                        Product product = order.getOrderProducts().get(0);

                        if(product.getOrder_state()==OrderUtil.waitsend() )
                            product.setOrder_state(OrderUtil.refundwaitsend() );
                        else if(product.getOrder_state() == OrderUtil.waitreceive() )
                            product.setOrder_state(OrderUtil.refundwaitreceive()  );
                        else if(product.getOrder_state() == OrderUtil.success()  )
                            product.setOrder_state(OrderUtil.refundafterreceived());
                        else if(product.getOrder_state() ==OrderUtil.successcomment())
                            product.setOrder_state(OrderUtil.refundafterreceivedandcommented() );
                        Result result1 = DatabaseUtil.updateById(product,HttpAddress.get(HttpAddress.order(),"update"));
                        if(result1.getCode()!=200){
                            Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                            if(CheckOrderDetailActivity.instance!=null){
                                CheckOrderDetailActivity.instance.finish();
                            }
                            CheckOrderActivity.shouldrefresh =true;
                        }

                    }
                }
            });
            gridImageAdapter = new GridImageAdapter(context, myresult);
            recyclerView.setAdapter(gridImageAdapter);
            recyclerView.setLayoutManager(new FlowLayoutManager(context,true));
            uploadImg = findViewById(R.id.uploadImg);




        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imgrequestCode  && data!=null){
            myresult.add(data.getData() );
            gridImageAdapter.notifyDataSetChanged();
//            System.out.println(myresult.size() );
        } else {
            Toast.makeText(context, "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }

}