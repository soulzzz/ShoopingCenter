package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.ProductCommentAdapter2;
import com.xmut.shoppingcenter.entity.ProductComment;

import java.util.ArrayList;
import java.util.List;

public class CheckAllCommentActivity extends AppCompatActivity {
    List<ProductComment> productComments;
    ImageView leave;
    RecyclerView recyclerView;
    RelativeLayout nullshow;
    ProductCommentAdapter2 productCommentAdapter2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_comment);
        productComments = (List<ProductComment>)getIntent().getSerializableExtra("productcomments");
        if(productComments == null || productComments.size()==0 ){
            productComments = new ArrayList<>();
        }
        init();
    }
    void init(){
        productCommentAdapter2 = new ProductCommentAdapter2(CheckAllCommentActivity.this,productComments);
        leave = findViewById(R.id.leave);
        nullshow = findViewById(R.id.nullshow);
        if(productComments.isEmpty())
            nullshow.setVisibility(View.VISIBLE);
        else
            nullshow.setVisibility(View.GONE);
        recyclerView =findViewById(R.id.rec_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(productCommentAdapter2);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}