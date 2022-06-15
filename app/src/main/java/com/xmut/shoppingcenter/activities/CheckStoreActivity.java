package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.fonts.SystemFonts;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.StoreProductGridLayoutAdapter;
import com.xmut.shoppingcenter.adapter.StoreProductLinearLayoutAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.entity.Subscribe;
import com.xmut.shoppingcenter.entity.UserCollection;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;
import com.xmut.shoppingcenter.utils.scrollviewUtil.MyScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStoreActivity extends AppCompatActivity {
    EditText edit_box;
    Button search_bt;
    Subscribe subscribe;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StoreProductGridLayoutAdapter storeProductGridLayoutAdapter;
    StoreProductLinearLayoutAdapter storeProductLinearLayoutAdapter;
    int store_id;
    Store store;
    ImageView filter_3_icon,filter_3_icon2,leave_bt,sort_icon,sort_icon2;
    TextView store_name,store_des,filter_1_text,filter_2_text,filter_3_text,filter_1_text2,filter_2_text2,filter_3_text2,subs;
    RadiuImageView store_img;
    LinearLayout filter_div,filter_div2 ;
    RecyclerView  rec_product;
    MyScrollView scrollView;
    boolean isSubscribed =false;
    boolean asc = true,isGrid = true;
    int topHeight = 95;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_store);
        store_id =(int)getIntent().getSerializableExtra("store_id");
        subs = findViewById(R.id.subscribe);
        initData();

        init();
        filter_1_text.callOnClick();
    }
    public void init(){
        edit_box=  findViewById(R.id.edit_box);
        edit_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){

                }else{
//                    System.out.println("Z1");
                    storeProductGridLayoutAdapter.setProductList(store.getStore_products());
                    storeProductLinearLayoutAdapter.setProductList(store.getStore_products());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_bt = findViewById(R.id.search_bt);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserManage.isEmpty(edit_box.getText().toString() )){
                    List<Product> products = new ArrayList<>();
                    int i=0;
                    for(Product product :store.getStore_products()){
                        if(product.getProduct_name().contains(edit_box.getText().toString())){
                            products.add(product);
                            System.out.println(i++);
                        }
                    }
                    storeProductGridLayoutAdapter.setProductList(products);
                    storeProductLinearLayoutAdapter.setProductList(products);
                    if(products.isEmpty()){
                        Toast.makeText(CheckStoreActivity.this, "搜索不到您想要的宝贝", Toast.LENGTH_SHORT).show();
                        edit_box.setText("");
                    }

                }
            }
        });

        subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isSubscribed){
                    Subscribe temp = new Subscribe();
                    temp.setStore_id(store.getStore_id());
                    temp.setStore_name(store.getStore_name());
                    temp.setStore_img(store.getStore_img());
                    temp.setUser_id(UserManage.getInstance().getUserInfo(CheckStoreActivity.this).getId());
                    Result result =DatabaseUtil.insert(temp,HttpAddress.get(HttpAddress.subscribe(),"insert"));
                    if(result.getCode()!=200){
                        Toast.makeText(CheckStoreActivity.this, "订阅失败", Toast.LENGTH_SHORT).show();
                    }else{
                        subscribe = DatabaseUtil.getEntity(result,Subscribe.class);
                        Toast.makeText(CheckStoreActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
                        subs.setText("已订阅");
                        subs.setTextColor(getResources().getColor(R.color.darkgray));
                        isSubscribed = true;
                    }

                }else{
                    Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.subscribe(),"delete", subscribe.getId()));
                    if(result.getCode()!=200){
                        Toast.makeText(CheckStoreActivity.this, "取消订阅失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CheckStoreActivity.this, "取消订阅成功", Toast.LENGTH_SHORT).show();
                        subs.setText("+ 订阅");
                        subs.setTextColor(getResources().getColor(R.color.orange));
                        isSubscribed = false;
                    }

                }
            }
        });
        sort_icon= findViewById(R.id.sort_icon);
        sort_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isGrid){
                    rec_product.setAdapter(storeProductGridLayoutAdapter);
                    rec_product.setLayoutManager(gridLayoutManager);
                    sort_icon.setImageResource(R.drawable.ic_list);
                    sort_icon2.setImageResource(R.drawable.ic_list);
                }else{
                    rec_product.setAdapter(storeProductLinearLayoutAdapter);
                    rec_product.setLayoutManager(linearLayoutManager);
                    sort_icon.setImageResource(R.drawable.ic_grid);
                    sort_icon2.setImageResource(R.drawable.ic_grid);
                    }
                isGrid = !isGrid;
            }
        });
        sort_icon2= findViewById(R.id.sort_icon2);
        sort_icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGrid){
                    rec_product.setAdapter(storeProductGridLayoutAdapter);
                    rec_product.setLayoutManager(gridLayoutManager);
                    sort_icon.setImageResource(R.drawable.ic_list);
                    sort_icon2.setImageResource(R.drawable.ic_list);
                }else{
                    rec_product.setAdapter(storeProductLinearLayoutAdapter);
                    rec_product.setLayoutManager(linearLayoutManager);
                    sort_icon.setImageResource(R.drawable.ic_grid);
                    sort_icon2.setImageResource(R.drawable.ic_grid);
                }
                isGrid = !isGrid;
            }
        });
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scrollView = findViewById(R.id.scrollView);
        filter_div = findViewById(R.id.filter_div);
        filter_3_icon2 = findViewById(R.id.filter_3_icon2);
        filter_3_icon = findViewById(R.id.filter_3_icon);
        filter_1_text =findViewById(R.id.filter_1_text);
        filter_1_text2 = findViewById(R.id.filter_1_text2);
        filter_2_text =findViewById(R.id.filter_2_text);
        filter_2_text2 =findViewById(R.id.filter_2_text2);
        filter_3_text = findViewById(R.id.filter_3_text);
        filter_3_text2 = findViewById(R.id.filter_3_text2);

        filter_1_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(1);
                sortByWeight(store.getStore_products());
                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });
        filter_1_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(1);
                sortByWeight(store.getStore_products());
                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });

        filter_2_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(2);
                sortBySales(store.getStore_products());
                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });
        filter_2_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(2);
                sortBySales(store.getStore_products());
                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });
        filter_3_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(3);

                if(asc){
                    sortByPriceAsc(store.getStore_products());
                }else{
                    sortByPriceDesc(store.getStore_products());
                }
                asc =!asc;

                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });
        filter_3_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(3);
                if(asc){
                    sortByPriceAsc(store.getStore_products());
                }else{
                    sortByPriceDesc(store.getStore_products());
                }
                asc =!asc;

                storeProductGridLayoutAdapter.notifyDataSetChanged();
                storeProductLinearLayoutAdapter.notifyDataSetChanged();
            }
        });
        rec_product = findViewById(R.id.rec_product);
        filter_div2 = findViewById(R.id.filter_div2);
        LinearLayout filter_div = findViewById(R.id.filter_div);
        store_img = findViewById(R.id.store_img);
        store_des = findViewById(R.id.store_des);
        store_name = findViewById(R.id.store_name);
        store_name.setText(store.getStore_name());
        store_des.setText(store.getStore_des());
        Glide.with(this).load(store.getStore_img()).placeholder(R.drawable.ic_error).into(store_img);

        scrollView.setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y >= topHeight) {
                    //重点 通过距离变化隐藏内外固定栏实现
                    filter_div2.setVisibility(View.VISIBLE);
                    filter_div.setVisibility(View.GONE);
                    rec_product.setNestedScrollingEnabled(true);
                } else {
                    filter_div2.setVisibility(View.GONE);
                    filter_div.setVisibility(View.VISIBLE);
                    rec_product.setNestedScrollingEnabled(false);
                }
            }
        });

        storeProductGridLayoutAdapter = new StoreProductGridLayoutAdapter(this,store.getStore_products());
        storeProductLinearLayoutAdapter = new StoreProductLinearLayoutAdapter(this,store.getStore_products());
        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        gridLayoutManager = new GridLayoutManager(this,2);
        rec_product.setAdapter(storeProductGridLayoutAdapter);
        rec_product.setLayoutManager(gridLayoutManager);
    }
    public void initData(){
        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.store(),"line",store_id));
        if(result.getCode()!=200){
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
            return;
        }else {
            store = DatabaseUtil.getEntity(result,Store.class);
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("user_id", String.valueOf(UserManage.getInstance().getUserInfo(this).getId()));
            paramsMap.put("store_id", String.valueOf(store.getStore_id()));

            Result result1 =DatabaseUtil.postParamters(paramsMap,HttpAddress.subscribe(),"line");
            if(result1.getCode()!=200){

            }else {
                subscribe = DatabaseUtil.getEntity(result1, Subscribe.class);
                if(subscribe !=null){
                    isSubscribed = true;
                    subs.setText("已订阅");
                    subs.setTextColor(getResources().getColor(R.color.darkgray));
                }
            }
        }

    }
    public void setColor(int num){ //综合和销量  选中后的按钮颜色变化
        switch (num){
            case 1:

                filter_1_text.setTextColor(Color.parseColor("#F1824E"));
                filter_2_text.setTextColor(Color.parseColor("#B0000000"));
                filter_3_text.setTextColor(Color.parseColor("#B0000000"));
                filter_1_text2.setTextColor(Color.parseColor("#F1824E"));
                filter_2_text2.setTextColor(Color.parseColor("#B0000000"));
                filter_3_text2.setTextColor(Color.parseColor("#B0000000"));

                filter_3_icon.getDrawable().setTint(Color.parseColor("#C0C0C0"));
                filter_3_icon2.getDrawable().setTint(Color.parseColor("#C0C0C0"));

                break;
            case 2:

                filter_1_text.setTextColor(Color.parseColor("#B0000000"));
                filter_2_text.setTextColor(Color.parseColor("#F1824E"));
                filter_3_text.setTextColor(Color.parseColor("#B0000000"));
                filter_1_text2.setTextColor(Color.parseColor("#B0000000"));
                filter_2_text2.setTextColor(Color.parseColor("#F1824E"));
                filter_3_text2.setTextColor(Color.parseColor("#B0000000"));

                filter_3_icon.getDrawable().setTint(Color.parseColor("#C0C0C0"));
                filter_3_icon2.getDrawable().setTint(Color.parseColor("#C0C0C0"));


                break;
            case 3:


                filter_1_text.setTextColor(Color.parseColor("#B0000000"));
                filter_2_text.setTextColor(Color.parseColor("#B0000000"));
                filter_3_text.setTextColor(Color.parseColor("#F1824E"));
                filter_1_text2.setTextColor(Color.parseColor("#B0000000"));
                filter_2_text2.setTextColor(Color.parseColor("#B0000000"));
                filter_3_text2.setTextColor(Color.parseColor("#F1824E"));
                filter_3_icon2.getDrawable().setTint(Color.parseColor("#F1824E"));
                filter_3_icon.getDrawable().setTint(Color.parseColor("#F1824E"));

                    if(asc){
//                        Toast.makeText(this, "升序", Toast.LENGTH_SHORT).show();
                        filter_3_icon.setImageResource(R.drawable.ic_arrow_up);
                        filter_3_icon2.setImageResource(R.drawable.ic_arrow_up);

                    }
                    else {
//                        Toast.makeText(this, "降序", Toast.LENGTH_SHORT).show();
                        filter_3_icon.setImageResource(R.drawable.ic_arrow_down);
                        filter_3_icon2.setImageResource(R.drawable.ic_arrow_down);

                    }



                break;


        }
    }
    //降序  当前对象的值 < 比较对象的值 ， 位置排在前

    public void sortByWeight(List<Product> list){
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                int temp = o1.getProduct_weight()-o2.getProduct_weight();
                if(temp ==0){
                    return o2.getProduct_sales()-o1.getProduct_sales(); //权重相同按销量降序排
                }
                return o2.getProduct_weight()-o1.getProduct_weight();
            }
        });
    }
    public void sortBySales(List<Product> list){
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getProduct_sales()-o1.getProduct_sales();
            }
        });
    }
    public void sortByPriceDesc(List<Product> list){
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                double temp = o1.getProduct_price()-o2.getProduct_price();
                if(temp==0) return 0;
                return temp>0?-1:1;
            }
        });
    }
    public void sortByPriceAsc(List<Product> list){
        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                double temp = o1.getProduct_price()-o2.getProduct_price();
                if(temp==0) return 0;
                return temp<0?-1:1;
            }
        });
    }
}