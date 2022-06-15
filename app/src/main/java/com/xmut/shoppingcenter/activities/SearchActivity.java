package com.xmut.shoppingcenter.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.SearchrecordsAdatper;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;
import com.xmut.shoppingcenter.utils.RecordsUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView leavebt;
    TextView clearrecord;
    SearchrecordsAdatper searchrecordsAdatper;
    List<String> records;
    RecordsUtil recordsUtil =RecordsUtil.getInstance();
    RecyclerView recyclerView;
    Context context;
    CheckBox checkBox;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context =this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){
        clearrecord =  findViewById(R.id.clearrecord);
        checkBox = findViewById(R.id.recordswitch);
        checkBox.getButtonDrawable().setTint(Color.parseColor("#C0C0C0"));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });
        clearrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordsUtil.getInstance().removeRecord(context);
                searchrecordsAdatper.clearrecords();
            }
        });
        leavebt = findViewById(R.id.leave_bt);
        leavebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        records = RecordsUtil.getInstance().getRecords(context);

        editText = findViewById(R.id.edit_box);
        button = findViewById(R.id.search_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserManage.isEmpty(editText.getText().toString())){
                    RecordsUtil.getInstance().saveRecords(context,editText.getText().toString());
                    UserManage.getInstance().Jump(SearchActivity.this , AfterSearchActivity.class , UserManage.trim(editText.getText().toString()),"searchcontent");
                }else{
                    Toast.makeText(context, "搜索内容为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView = findViewById(R.id.searchrecords);
        searchrecordsAdatper = new SearchrecordsAdatper(context,records);
        recyclerView.setAdapter(searchrecordsAdatper);
        recyclerView.setLayoutManager(new FlowLayoutManager(context,true));
    }

    @Override
    protected void onResume() {
        super.onResume();

        records = RecordsUtil.getInstance().getRecords(context);
        searchrecordsAdatper.setrecords(records);

    }
}