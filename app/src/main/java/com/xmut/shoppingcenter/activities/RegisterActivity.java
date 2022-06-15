package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.xmut.shoppingcenter.utils.UserManage;

public class RegisterActivity extends AppCompatActivity {
    UserManage userManage;
    EditText reg_username;
    EditText reg_email;
    EditText reg_pwd;
    TextView signin;
    Context mContext;
    Button signup;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initUI();
        userManage = UserManage.getInstance();
    }

    private void initUI() {
        reg_username = findViewById(R.id.reg_username);
        reg_email = findViewById(R.id.reg_email);
        reg_pwd = findViewById(R.id.reg_pwd);
        reg_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(UserManage.trim(reg_pwd.getText().toString()).isEmpty()){
                    checkBox.setVisibility(View.GONE);
                }else
                    checkBox.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signup = findViewById(R.id.reg_signup);
        signin = findViewById(R.id.reg_signin);
        checkBox = findViewById(R.id.pwd_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    reg_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    reg_pwd.setSelection(reg_pwd.getText().length());
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    reg_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    reg_pwd.setSelection(reg_pwd.getText().length());
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManage.Jump(RegisterActivity.this,LoginActivity.class);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userManage.trim(reg_username.getText().toString());
                String email = userManage.trim(reg_email.getText().toString());
                String pwd = userManage.trim(reg_pwd.getText().toString());

                if (username.length() <= 3 ) {
                    Toast.makeText(mContext, "用户名不能少于3位", Toast.LENGTH_SHORT).show();
                    return;

                }else if (pwd.length()<= 3) {
                    Toast.makeText(mContext, "用户密码不能少于3位", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!email.contains("@") || !email.contains(".") )
                {
                    Toast.makeText(mContext, "用户邮箱格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User(username,pwd,email);
                Result result= DatabaseUtil.insert(user,
                        HttpAddress.get(HttpAddress.user(),"insert"));
                if(result.getCode()!=200)
                {
                    Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                }else{
                    User user1=DatabaseUtil.getEntity(result,User.class);
                    userManage.saveUserInfo(mContext,user1);
                    userManage.Jump(RegisterActivity.this,LoginActivity.class);
                }

            }
        });
    }
}