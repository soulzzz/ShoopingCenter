package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.utils.UserManage;

public class WelcomeActivity extends AppCompatActivity {
//    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        imageView = findViewById(R.id.img);
//        Glide.with(this).asGif().load(R.drawable.test).into(imageView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserManage.getInstance().FinishJump(WelcomeActivity.this,LoginActivity.class);
                finish();
            }
        }, 1000);
    }
}