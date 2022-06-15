package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.ChooseCouponAdatper;
import com.xmut.shoppingcenter.entity.Coupon;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xmut.shoppingcenter.activities.PayOrderActivity.couponCode;

public class ChooseCouponActivity extends AppCompatActivity {
    ChooseCouponAdatper couponAdatper;
    Context context;
    List<Coupon> coupons;
    Double price;
    RelativeLayout nullshow;
    RecyclerView couponrecycle;
    ImageView leave_bt;
    List<String> time = TimeUtil.getInstance().getFormatTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context  = this;
        price = getIntent().getDoubleExtra("price",-1);
        coupons= (List<Coupon>)getIntent().getSerializableExtra("coupon");
        setContentView(R.layout.activity_choose_coupon);

//        initData(); //从PayOrderActivity中传List 因为要判断是否有可用的优惠券
        init();
    }
    private void initData(){
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
    private void init(){
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nullshow = findViewById(R.id.nullshow);
        couponAdatper = new ChooseCouponAdatper(context,coupons,price);
        couponAdatper.setFirstListener(new ChooseCouponAdatper.FirstListener() {
            @Override
            public void onSelect(int pos) {
                Intent it = getIntent();
                it.putExtra("coupon", coupons.get(pos));
                setResult(couponCode,it);
                finish();
            }


        });
        couponrecycle  =findViewById(R.id.couponrecycle);
        couponrecycle.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        couponrecycle.setAdapter(couponAdatper);
        if(coupons.isEmpty()){
            nullshow.setVisibility(View.VISIBLE);
        }else {
            nullshow.setVisibility(View.GONE);
        }


    }


}