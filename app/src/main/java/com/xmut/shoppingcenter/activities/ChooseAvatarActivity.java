package com.xmut.shoppingcenter.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.AvatarUtil;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.UserManage;

import org.w3c.dom.Text;

import java.io.IOException;

import static com.xmut.shoppingcenter.activities.PayOrderActivity.addressCode;

public class ChooseAvatarActivity extends AppCompatActivity {
    RadiuImageView img;
    Uri imgUri;
    Context context;
    Button successbt;
    public static int imgrequestCode = 10;
    User user;
    TextView cancel;

    Bitmap imgbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);
        context = this;
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imgrequestCode && data!=null){
            imgUri=data.getData();
            try {
                imgbitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            img.setImageBitmap(imgbitmap);
            Glide.with(context).load(imgUri).into(img);

            Result result1 = DatabaseUtil.uploadFile(AvatarUtil.getInstance().getFile(imgbitmap),"file","upload");
            if(result1.getCode()!=200){
                successbt.setEnabled(false);
                successbt.setPressed(false);
                successbt.setTextColor(getResources().getColor(R.color.darkgray));
                Toast.makeText(ChooseAvatarActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
            }else{
                user = UserManage.getInstance().getUserInfo(context);
                user.setAvatar(result1.getResult() );
                successbt.setEnabled(true);
                successbt.setPressed(true);
                successbt.setTextColor(Color.WHITE);
            }
        } else {
            imgUri = null;
            Toast.makeText(context, "未选择头像", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        cancel = findViewById(R.id.cancel);
        img = findViewById(R.id.img);
        Glide.with(context).load(UserManage.getInstance().getUserInfo(context).getAvatar()).placeholder(R.drawable.ic_error).into(img);
        successbt = findViewById(R.id.success_change_bt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                //把所有照片显示出来
                intent.setType("image/*");
                startActivityForResult(intent, imgrequestCode);
//                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//
//                    Intent data = result.getData();
//                    int resultCode = result.getResultCode();
//                    System.out.println(resultCode);
//                    if (resultCode == imgrequestCode) {
//                        imgUri = data.getData();
//                        Glide.with(context).load(imgUri).into(img);
//                        Result result1 = DatabaseUtil.uploadFile(AvatarUtil.uri2File(imgUri,context),"file","upload");
//                        if(result1.getCode()!=200){
//                            successbt.setEnabled(false);
//                            Toast.makeText(ChooseAvatarActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
//                        }else{
//                            user = UserManage.getInstance().getUserInfo(context);
//                            user.setAvatar(result1.getResult() );
//                            successbt.setEnabled(true);
//                        }
//                    } else {
//                        imgUri = null;
//                        Toast.makeText(context, "未选择头像", Toast.LENGTH_SHORT).show();
//                    }
//
//                }).launch(intent);
//            }
            }
        });

        successbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user !=null){
                    Result result = DatabaseUtil.updateById(user, HttpAddress.get(HttpAddress.user(),"update"));
                    if(result.getCode()!=200){
                        Toast.makeText(ChooseAvatarActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChooseAvatarActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        User user = DatabaseUtil.getEntity(result,User.class);
                        UserManage.getInstance().saveUserInfo(context,user);
                        Intent it = getIntent();
                        it.putExtra("avatar", user.getAvatar());
                        setResult(imgrequestCode,it);
                        finish();
                    }
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}