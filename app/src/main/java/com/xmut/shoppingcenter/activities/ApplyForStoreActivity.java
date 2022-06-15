package com.xmut.shoppingcenter.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Application;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.utils.AvatarUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApplyForStoreActivity extends AppCompatActivity {
    String imgpath ="";
    EditText store_name,store_des,apply_reason;
    TextView store_img,apply;
    public static int imgrequestCode = 10;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imgrequestCode  && data!=null){

            Bitmap imgbitmap = null;
            try {
                imgbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Result result1 = DatabaseUtil.uploadFile(AvatarUtil.getInstance().getFile(imgbitmap),"file","upload");
            if(result1.getCode()!=200){
                Toast.makeText(this, "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                return;
            }else{
                store_img.setText("已选择");
                imgpath = result1.getResult();

            }
//            System.out.println(myresult.size() );
        } else {
            Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_store);

        store_des = findViewById(R.id.store_des);
        store_name =findViewById(R.id.store_name);
        apply_reason = findViewById(R.id.apply_reason);
        store_img = findViewById(R.id.store_img);
        apply = findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserManage.isEmpty(store_name.getText().toString() ) ||imgpath =="" || UserManage.isEmpty(apply_reason.getText().toString() ) )
                    return;
                Application application =new Application();
                application.setApply_reason(UserManage.trim( apply_reason.getText().toString()));
                application.setApply_state(0);
                application.setSend_time(TimeUtil.getInstance().getFormatTime().get(1));
                application.setUsername(UserManage.getInstance().getUserInfo(getApplicationContext()).getUsername() );
                application.setStore_name(UserManage.trim( store_name.getText().toString()));
                application.setStore_des(UserManage.trim( store_des.getText().toString()));
                application.setStore_img(imgpath);

                Result result  = DatabaseUtil.insert(application,"application","insert");
                if(result.getCode()!=200){
                    Toast.makeText(ApplyForStoreActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ApplyForStoreActivity.this, "请求成功,请耐心等待", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        store_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                //把所有照片显示出来
                intent.setType("image/*");
                startActivityForResult(intent, imgrequestCode);
            }
        });

    }
}