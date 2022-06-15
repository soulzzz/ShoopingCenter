package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckSubscribeAdapter;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Subscribe;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;

public class CheckSubscribeActivity extends AppCompatActivity {
    ImageView leave;
    List<Subscribe> subscribes = new ArrayList<>();
    RecyclerView rec_subscribe;
    CheckSubscribeAdapter checkSubscribeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_subscribe);
        init();
        initData();
    }

    public void init() {
        checkSubscribeAdapter = new CheckSubscribeAdapter(this,subscribes);
        rec_subscribe =findViewById(R.id.rec_subscribe);
        leave =  findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rec_subscribe.setAdapter(checkSubscribeAdapter);
        rec_subscribe.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));




    }

    public void initData() {
        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.subscribe(), "list", UserManage.getInstance().getUserInfo(this).getId()));
        if (result.getCode() != 200) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }else
        {
            subscribes.addAll(DatabaseUtil.getObjectList(result,Subscribe.class) );
            checkSubscribeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(subscribes.isEmpty())
            findViewById(R.id.nullshow).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.nullshow).setVisibility(View.GONE);
    }
}
