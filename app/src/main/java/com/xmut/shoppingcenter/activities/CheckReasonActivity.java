package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckReasonAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;

public class CheckReasonActivity extends AppCompatActivity {
    ImageView leave;
    RecyclerView  rec_reason;
    CheckReasonAdapter checkReasonAdapter;
    Product product;
    TextView add_reason;
    boolean shouldshowaddreason = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_reason);
        product = (Product)getIntent().getSerializableExtra("product");
        shouldshowaddreason = getIntent().getBooleanExtra("shouldshowaddreason",true);

        if(product ==null)
            return;
        initData();
        init();
    }

    private void initData() {

    }

    public void init(){
        add_reason = findViewById(R.id.add_reason);
        if(shouldshowaddreason){

            add_reason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(CheckReasonActivity.this,ApplyForRefundActivity.class);
                    it.putExtra("order_id",product.getOrder_id());
                    it.putExtra("product_id",product.getProduct_id());
                    it.putExtra("state",product.getOrder_state());
                    startActivity(it);
                    finish();
                }
            });
        }else
            add_reason.setVisibility(View.GONE);

        leave = findViewById(R.id.leave);
       rec_reason = findViewById(R.id.rec_reason);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       checkReasonAdapter = new CheckReasonAdapter(CheckReasonActivity.this,product.getOrder_reason());
       rec_reason.setAdapter(checkReasonAdapter);
       rec_reason.setLayoutManager(new LinearLayoutManager(CheckReasonActivity.this,RecyclerView.VERTICAL,false));


   }
}