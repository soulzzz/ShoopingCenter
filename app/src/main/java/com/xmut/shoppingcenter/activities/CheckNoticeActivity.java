package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Notice;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

public class CheckNoticeActivity extends AppCompatActivity {
    TextView title;
    TextView message;
    ImageView leave_bt;
    TextView time;
    Button check_detail;
    Notice notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notice);
        notice = (Notice) getIntent().getSerializableExtra("notice");
        init();
    }
    private void init(){
        title  =findViewById(R.id.notice_title);
        message = findViewById(R.id.message);
        leave_bt = findViewById(R.id.leave_bt);
        time = findViewById(R.id.notice_time);
        check_detail = findViewById(R.id.check_detail);

        title.setText(notice.getTitle());
        message.setText("\u3000\u3000" +notice.getMessage());
        time.setText(TimeUtil.getInstance().FormatHourTime(notice.getSend_time()));
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 0为退款成功消息  1为退款中商家回复的信息
        Product p = new Product();
        p.setProduct_id(notice.getProduct_id());
        p.setOrder_id(notice.getOrder_id());
        if(notice.getType() == 0){
            check_detail.setVisibility(View.VISIBLE);
            check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p.setOrder_state(10);
                    UserManage.getInstance().Jump(CheckNoticeActivity.this,CheckOrderDetailActivity.class,p,"product");
                }
            });
        }else if(notice.getType() == 1){
            check_detail.setVisibility(View.VISIBLE);
            check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p.setOrder_state(10);
                    UserManage.getInstance().Jump(CheckNoticeActivity.this,CheckOrderDetailActivity.class,p,"product");
                }
            });
        }else if(notice.getType() ==2){ //订单发货状态更新
            check_detail.setVisibility(View.VISIBLE);
            check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p.setOrder_state(OrderUtil.waitreceive());
                    UserManage.getInstance().Jump(CheckNoticeActivity.this,CheckOrderDetailActivity.class,p,"product");
                }
            });
        }
    }
}