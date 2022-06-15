package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.DialogUitl;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {
    UserManage userManage;
    EditText username;
    EditText userpwd;
    TextView register;
    Context mContext;
    CheckBox checkBox;
    FloatingActionButton loginbt;
    TextView forgetpwd;
    DialogUitl dialogUitl = DialogUitl.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userManage = UserManage.getInstance();
        mContext = this;
        initUI();
//        if(DatabaseUtil.ping() ){
            autologin();
//        }else
//        {
//            Toast.makeText(mContext, "服务器暂无连接", Toast.LENGTH_SHORT).show();
//        }



    }

    private void initUI() {
        forgetpwd = findViewById(R.id.forgetpwd);
        username = (EditText) findViewById(R.id.login_Username);
        userpwd = (EditText)findViewById(R.id.login_pwd);
        register = findViewById(R.id.registerGOTO);
        loginbt = findViewById(R.id.loginbt);
        checkBox = (CheckBox)findViewById(R.id.pwd_checkbox);
        userpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(UserManage.trim(userpwd.getText().toString()).isEmpty()){
                    checkBox.setVisibility(View.GONE);
                }else
                    checkBox.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    userpwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    userpwd.setSelection(userpwd.getText().length());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    userpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    userpwd.setSelection(userpwd.getText().length());
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManage.FinishJump(LoginActivity.this,RegisterActivity.class);
            }
        });
        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = UserManage.trim(username.getText().toString());
                String password= UserManage.trim(userpwd.getText().toString());
                if(UserManage.isEmpty(uname) || UserManage.isEmpty(password))
                {
                    Toast.makeText(mContext, "用户名或密码为空", Toast.LENGTH_SHORT).show();
                    return ;
                }
                User user1=new User(uname,password);
                //log in
                Result result= DatabaseUtil.login(user1,HttpAddress.get(HttpAddress.user(),"login"));
                if(result.getCode()!=200)
                {
                    Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
                }
                else{
                    User user=DatabaseUtil.getEntity(result,User.class);
                    userManage.saveUserInfo(mContext,user);
                    userManage.FinishJump(LoginActivity.this,MainActivity.class);

                }
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1 = new Dialog(mContext);
                View contentView1 = LayoutInflater.from(mContext).inflate(
                        R.layout.dialog_forgetpwd, null);
                dialog1.setContentView(contentView1);
                TextView cancel = contentView1.findViewById(R.id.cancel);
                Button successbt = contentView1.findViewById(R.id.success_change_bt);
                EditText username = contentView1.findViewById(R.id.username);
                EditText email = contentView1.findViewById(R.id.email);

                username.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!UserManage.isEmpty(username.getText().toString())&& !UserManage.isEmpty(email.getText().toString()) ) {
                            successbt.setPressed(true);
                            successbt.setTextColor(Color.WHITE);
                        }
                        else
                        {
                            successbt.setPressed(false);
                            successbt.setTextColor(getResources().getColor(R.color.darkgray));
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!UserManage.isEmpty(username.getText().toString())&& !UserManage.isEmpty(email.getText().toString()) ) {
                            successbt.setPressed(true);
                            successbt.setTextColor(Color.WHITE);
                        }
                        else
                        {
                            successbt.setPressed(false);
                            successbt.setTextColor(getResources().getColor(R.color.darkgray));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dialogUitl.setDialogTransparentandCircleFromRight(dialog1);
                dialogUitl.setDialogMatchParent(mContext,dialog1);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                successbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,String> paramsMap=new HashMap<>();
                        paramsMap.put("username",UserManage.trim(username.getText().toString()));
                        paramsMap.put("email",UserManage.trim(email.getText().toString()));
                        Result result= DatabaseUtil.postParamters(paramsMap,
                                "email","reset");
                        if(result.getCode()!=200)
                        {
                            Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                        else{
                            System.out.println(result);
                        }
                    }
                });
                dialog1.show();


            }
        });
    }

    private void autologin(){
        User user = userManage.getUserInfo(mContext);
        if (user != null) {
            Result result = DatabaseUtil.login(user,
                    HttpAddress.get(HttpAddress.user(), "login"));
            if (result.getCode() != 200) {
                Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                userManage.saveUserInfo(mContext, DatabaseUtil.getEntity(result,User.class) );
                userManage.FinishJump(LoginActivity.this, MainActivity.class);

            }
        }
    }

}