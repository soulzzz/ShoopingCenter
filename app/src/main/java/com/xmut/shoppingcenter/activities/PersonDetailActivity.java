package com.xmut.shoppingcenter.activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.AvatarUtil;
import com.xmut.shoppingcenter.utils.DialogUitl;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.UserManage;

public class PersonDetailActivity extends AppCompatActivity {
    Uri imgUri;
    RadiuImageView avatar_img;
    RelativeLayout address_line;
    RelativeLayout avatar_div,password_div,tel_div,email_div;
    DialogUitl dialogUitl =new DialogUitl();
    Context context;
    ImageView leave_bt;
    PopupLayout popupLayout;
    TextView tel_num,email_num;
    public static int imgrequestCode = 10;
    User user = UserManage.getInstance().getUserInfo(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_person_detail);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = UserManage.getInstance().getUserInfo(context);
        email_num.setText(UserManage.getInstance().getUserInfo(context).getEmail()!=null?UserManage.getInstance().getUserInfo(context).getEmail():"");
        tel_num.setText(UserManage.getInstance().getUserInfo(context).getTel()!=null?UserManage.getInstance().getUserInfo(context).getTel():"");
    }
    private void init(){
        requestMyPermissions();
        tel_num = findViewById(R.id.tel_num);
        email_num  =findViewById(R.id.email_num);
        email_div =findViewById(R.id.email_div);
        email_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=View.inflate( context,R.layout.popup_changeitem,null);
                PopupLayout popupLayout = PopupLayout.init(context,view);
                TextView submit,name;
                EditText value;
                name = view.findViewById ( R.id.name );
                name.setText("邮箱");
                value = view.findViewById ( R.id.value );
                value.setText(UserManage.getInstance().getUserInfo(context).getEmail() );
//                value.setText(UserManage.getInstance().getUserInfo(context).getTel().isEmpty()?"":UserManage.getInstance().getUserInfo(context).getTel());
                submit = view.findViewById ( R.id.submit );
                submit.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        String tel = UserManage.trim(value.getText().toString());
                        if(!tel.contains("@")|| !tel.contains(".") || tel.length()<5 ){
                            Toast.makeText(PersonDetailActivity.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                        }else{
                            User user  =UserManage.getInstance ().getUserInfo(context);
                            user.setEmail ( tel );
                            Result result = DatabaseUtil.updateById ( user,HttpAddress.get(HttpAddress.user(),"update") );
                            if(result.getCode ()!=200){
                                Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                            }else{
                                UserManage.getInstance ().saveUserInfo ( context,DatabaseUtil.getEntity ( result,User.class ) );
                                Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                                email_num.setText(tel );
                                popupLayout.dismiss ();
                            }
                        }

                    }
                } );
                WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE);
                Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
                popupLayout.setHeight ( (int) (d.getHeight ()*0.5),false);
                popupLayout.show ();
            }
        });
        tel_div = findViewById(R.id.tel_div);
        tel_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=View.inflate( context,R.layout.popup_changeitem,null);
                PopupLayout popupLayout = PopupLayout.init(context,view);
                TextView submit,name;
                EditText value;
                name = view.findViewById ( R.id.name );
                name.setText("手机号");
                value = view.findViewById ( R.id.value );
                value.setText(UserManage.getInstance().getUserInfo(context).getTel() );
//                value.setText(UserManage.getInstance().getUserInfo(context).getTel().isEmpty()?"":UserManage.getInstance().getUserInfo(context).getTel());
                submit = view.findViewById ( R.id.submit );
                submit.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        String tel = UserManage.trim(value.getText().toString());
                        if(tel.length()!=11){
                            Toast.makeText(PersonDetailActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                        }else{
                                User user  =UserManage.getInstance ().getUserInfo(context);
                                user.setTel ( tel );
                                Result result = DatabaseUtil.updateById ( user,HttpAddress.get(HttpAddress.user(),"update") );
                                if(result.getCode ()!=200){
                                    Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                                }else{
                                    UserManage.getInstance ().saveUserInfo ( context,DatabaseUtil.getEntity ( result,User.class ) );
                                    tel_num.setText(tel );
                                    Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                                    popupLayout.dismiss ();
                                }
                            }

                    }
                } );
                WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE);
                Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
                popupLayout.setHeight ( (int) (d.getHeight ()*0.5),false);
                popupLayout.show ();
            }
        });
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        password_div =findViewById(R.id.password_div);
        password_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=View.inflate( context,R.layout.popup_changeitem,null);
                PopupLayout popupLayout = PopupLayout.init(context,view);
                TextView submit,name;
                EditText value;
                name = view.findViewById ( R.id.name );
                name.setText("新密码");
                value = view.findViewById ( R.id.value );
                submit = view.findViewById ( R.id.submit );
                submit.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        String newpwd = UserManage.trim(value.getText().toString());
                        if(newpwd.length()<=3){
                            Toast.makeText(PersonDetailActivity.this, "密码长度小于三位", Toast.LENGTH_SHORT).show();
                        }else{
                            if(value.getText().toString().equals(UserManage.getInstance().getUserInfo(context).getPassword())){
                                Toast.makeText ( context, "新旧密码相同，请修改新密码", Toast.LENGTH_SHORT ).show ();
                            }else{
                                User user  =UserManage.getInstance ().getUserInfo(context);
                                user.setPassword ( newpwd );

                                Result result = DatabaseUtil.updateById ( user,HttpAddress.get(HttpAddress.user(),"update") );
                                if(result.getCode ()!=200){
                                    Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                                }else{
                                    UserManage.getInstance ().saveUserInfo ( context,DatabaseUtil.getEntity ( result,User.class ) );

                                    Toast.makeText ( context, result.getMsg(), Toast.LENGTH_SHORT ).show ();
                                    popupLayout.dismiss ();
                                }
                            }
                        }

                    }
                } );
                WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE);
                Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
                popupLayout.setHeight ( (int) (d.getHeight ()*0.5),false);
                popupLayout.show ();
            }
        });
        avatar_div =findViewById(R.id.avatar_div);
        avatar_img  =findViewById(R.id.avatar_img);
        Glide.with(context).load(UserManage.getInstance().getUserInfo(context).getAvatar() ).into(avatar_img);
        address_line = findViewById(R.id.address_line);
        address_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,ChooseAddressActivity.class);
                it.putExtra("checkaddress",true);
                startActivity(it);
            }
        });
        avatar_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                    Intent data = result.getData();
                    int resultCode = result.getResultCode();
                    if(resultCode ==imgrequestCode){
                         String temp = data.getStringExtra("avatar");
                        Glide.with(context).load(temp ).into(avatar_img);
                    }

                }).launch(new Intent(PersonDetailActivity.this, ChooseAvatarActivity.class));
            }

        });
    }
    private void requestMyPermissions() {

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(PersonDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//        } else {
//            Log.d("A", "requestMyPermissions: 有写SD权限");
//        }
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //没有授权，编写申请权限代码
//            ActivityCompat.requestPermissions(PersonDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
//        } else {
//            Log.d("A", "requestMyPermissions: 有读SD权限");
//        }
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestCode识别，找到我自己定义的requestCode
        if (requestCode==101) {
            boolean grantedAll=true;
            for (int rangtResult:grantResults) {
                //判断用户是否给予权限
                if (rangtResult!=PackageManager.PERMISSION_GRANTED){
                    grantedAll=false;
                    break;
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void showAvatarDialog(){
        Dialog dialog1 = new Dialog(context);
        View contentView1 = LayoutInflater.from(context).inflate(
                R.layout.dialog_changeavatar, null);
        dialog1.setContentView(contentView1);
        Button successbt = contentView1.findViewById(R.id.success_change_bt);
        RadiuImageView radiuImageView = contentView1.findViewById(R.id.img);
        radiuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.GET_CONTENT");
                //把所有照片显示出来
                intent.setType("image/*");
                startActivityForResult(intent,123);

                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                    Intent data = result.getData();
                    int resultCode = result.getResultCode();
                    System.out.println(resultCode);
                    if (resultCode == imgrequestCode) {
                        imgUri = data.getData();
                        Glide.with(context).load(imgUri).into(radiuImageView);
                        Result result1 = DatabaseUtil.uploadFile(AvatarUtil.uri2File(imgUri,context),"file","upload");
                        if(result1.getCode()!=200){
                            successbt.setEnabled(false);
                            Toast.makeText(PersonDetailActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                        }else{
                            user = UserManage.getInstance().getUserInfo(context);
                            user.setAvatar(result1.getResult() );
                            successbt.setEnabled(true);
                        }
                    } else {
                        imgUri = null;
                        Toast.makeText(context, "未选择头像", Toast.LENGTH_SHORT).show();
                    }

                }).launch(intent);
            }
        });

        TextView cancel = contentView1.findViewById(R.id.cancel);

        successbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user !=null){
                    Result result = DatabaseUtil.updateById(user, HttpAddress.get(HttpAddress.user(),"update"));
                    if(result.getCode()!=200){
                        Toast.makeText(PersonDetailActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PersonDetailActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        Glide.with(context).load(imgUri).into(avatar_img);
                    }
                }
                dialog1.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialogUitl.setDialogTransparentandCircleFromRight(dialog1);
        dialogUitl.setDialogMatchParent(context, dialog1);
        dialog1.show();
    }
}