package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.CheckCollectionAdapter;
import com.xmut.shoppingcenter.entity.UserCollection;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;

public class CheckCollectionActivity extends AppCompatActivity {
    ImageView leave;
    List<UserCollection> userCollections = new ArrayList<>();
    RecyclerView rec_collection;
    CheckCollectionAdapter checkCollectionAdapter;


    @Override
    protected void onResume() {
        super.onResume();
        if(userCollections.isEmpty())
            findViewById(R.id.nullshow).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.nullshow).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_collection);
        init();
        initData();
    }

    public void init() {
        checkCollectionAdapter = new CheckCollectionAdapter(this, userCollections);
        rec_collection =findViewById(R.id.rec_collection);
        leave =  findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rec_collection.setAdapter(checkCollectionAdapter);
        rec_collection.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));




    }

    public void initData() {
        Result result = DatabaseUtil.selectList(HttpAddress.get(HttpAddress.collection(), "list", UserManage.getInstance().getUserInfo(this).getId()));
        if (result.getCode() != 200) {
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }else
        {
            userCollections.addAll(DatabaseUtil.getObjectList(result, UserCollection.class) );
            checkCollectionAdapter.notifyDataSetChanged();
        }
    }
}
