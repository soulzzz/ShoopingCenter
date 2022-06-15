package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckFootAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;

public class CheckFootActivity extends AppCompatActivity {
    ImageView leave;
    List<Product> productList = new ArrayList<>();
    RecyclerView rec_product;
    CheckFootAdapter checkFootAdapter;
    TextView clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_foot);
        init();
    }
    void init(){
        leave = findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rec_product=  findViewById(R.id.rec_product);
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().clearFoot(CheckFootActivity.this);
                productList.clear();
                checkFootAdapter.notifyDataSetChanged();
                Toast.makeText(CheckFootActivity.this, "清空成功", Toast.LENGTH_SHORT).show();
                if(productList.isEmpty())
                    findViewById(R.id.nullshow).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.nullshow).setVisibility(View.GONE);
            }
        });
        productList.addAll(UserManage.getInstance().getFoot(this) ) ;
        checkFootAdapter = new CheckFootAdapter(this,productList);
        rec_product.setAdapter(checkFootAdapter);
        rec_product.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(productList.isEmpty())
            findViewById(R.id.nullshow).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.nullshow).setVisibility(View.GONE);

    }
}