package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xmut.shoppingcenter.R;

public class CheckRefundActivity extends AppCompatActivity {
    String order_id, product_id;
    int order_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_refund);
        order_id = getIntent().getStringExtra("order_id");
        product_id = getIntent().getStringExtra("product_id");
        order_state = getIntent().getIntExtra("order_state", -1);
    }
}