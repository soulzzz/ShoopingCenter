package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckCouponAdatper;
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

public class CheckCouponActivity extends AppCompatActivity {
    CheckCouponAdatper couponAdatper;
    Context context;
    List<Coupon> coupons;
    Double price;
    RecyclerView couponrecycle;
    ImageView leave_bt;
    List<String> time = TimeUtil.getInstance().getFormatTime();

    @Override
    protected void onResume() {
        super.onResume();
        if(coupons.isEmpty())
            findViewById(R.id.nullshow).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.nullshow).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context  = this;
        initData();
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

        couponAdatper = new CheckCouponAdatper(context,coupons);
        couponrecycle  =findViewById(R.id.couponrecycle);
        couponrecycle.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        couponrecycle.setAdapter(couponAdatper);



    }


}