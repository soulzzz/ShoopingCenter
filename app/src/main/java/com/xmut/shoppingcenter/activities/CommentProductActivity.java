package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.GridImageAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.fragments.CommentProductFragment;
import com.xmut.shoppingcenter.utils.fragmentUtil.MyFragmentPageAdatper;
import com.xmut.shoppingcenter.utils.fragmentUtil.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class CommentProductActivity extends AppCompatActivity {
    ImageView leave;
    MyViewPager myViewPager;
    List<Product> productList;
    List<Fragment> fragments;
    MyFragmentPageAdatper adapter;
   int  Curindex = 0;
   int total = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_product);
        productList = (List<Product>)getIntent().getSerializableExtra("product");
        total =productList.size();
        init();


    }
    void init(){

        fragments = new ArrayList<>();
        myViewPager = findViewById(R.id.viewpager);
        leave = findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckOrderDetailActivity.instance!=null){
                    CheckOrderDetailActivity.instance.finish();
                }
                CheckOrderActivity.shouldrefresh =true;
                finish();
            }
        });
        for(int i=0 ; i<productList.size();i++){
            CommentProductFragment temp = new CommentProductFragment(productList.get(i),i);
            temp.setGonextListener(new CommentProductFragment.GoNextListener() {
                @Override
                public void goNext(int curindx) {

                    if(curindx<total-1 ){
                        curindx++;
                        myViewPager.setCurrentItem(curindx);
                    }else{
                        if(CheckOrderDetailActivity.instance!=null){
                            CheckOrderDetailActivity.instance.finish();
                        }
                        CheckOrderActivity.shouldrefresh =true;
                        finish();
                    }
                }
            });
            fragments.add(temp);
        }
         adapter = new MyFragmentPageAdatper(getSupportFragmentManager(),fragments);
        myViewPager.setAdapter(adapter);


    }
}