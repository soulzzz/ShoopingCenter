package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.ImageAdatper1;
import com.xmut.shoppingcenter.adapter.ProductCommentAdapter;
import com.xmut.shoppingcenter.adapter.StoreProductGridLayoutAdapter;
import com.xmut.shoppingcenter.entity.UserCollection;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Shoppingcar;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.xmut.shoppingcenter.fragments.ShoppingCarFragment.shouldrefresh;

public class CheckProductActivity extends AppCompatActivity {
    UserCollection userCollection;
    Boolean isCollected = false;
    String product_id;
    Product product;
    Store store;
    Banner banner;
    ImageView leave_bt;
    RelativeLayout search_bar,store_part;
    TextView product_price,product_name,store_name,store_goto,collect,addtocar,pay,nullcomment,check_all,product_des;
    RecyclerView rec_comment,recommend_product;
    RadiuImageView store_img;
    EditText edit_box;
    List<String> imglist;
    List<Product> recommends;
    boolean shouldshow = true;
    Thread thread;
    boolean isAlive = true;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_product);
        progressBar = findViewById(R.id.pb);
        product_id = (String) getIntent().getSerializableExtra("product_id");
        shouldshow = getIntent().getBooleanExtra("shouldshow",true);
        collect = findViewById(R.id.collect);
        initData();
        init();
        AddFoot();
        recommend();
    }

    @Override
    protected void onDestroy() {
        isAlive =false;
        super.onDestroy();
    }

    private void recommend() {
        if(!UserManage.getInstance().getShouldRecommend(CheckProductActivity.this) || !shouldshow ){
            return;
        }
        new Thread(() -> {
            try {
                Thread.sleep(3000);

                if(CheckProductActivity.this ==null || !isAlive ){
                    return;
                }

                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Map<String,String> paramsMap=new HashMap<>();
                        paramsMap.put("product_id",String.valueOf(product.getProduct_id()));
                        Result result  =DatabaseUtil.postParamters(paramsMap,"order","recommend");
                        if(result.getCode()!=200){

                        } else{
                            PopupLayout popupLayout ;
                            recommends =new ArrayList<>();
                            recommends.addAll( DatabaseUtil.getObjectList(result,Product.class));

                            if(!recommends.isEmpty()){
                                int index = new Random().nextInt(recommends.size() );
                                System.out.println("ZZZ"+index+ "ZZZ"+ recommends.size() );
                                View view=View.inflate(CheckProductActivity.this, R.layout.popup_recommend,null);
                                popupLayout=PopupLayout.init(CheckProductActivity.this,view);
                                TextView category = view.findViewById(R.id.category);
                                ImageView iv_del = view.findViewById(R.id.iv_del);
                                RadiuImageView product_img = view.findViewById(R.id.product_img);
                                TextView product_name = view.findViewById(R.id.product_name);
                                category.setText(recommends.get(index).getProduct_category());
                                iv_del.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupLayout.dismiss();
                                    }
                                });
                                product_name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it =new Intent(CheckProductActivity.this,CheckProductActivity.class);
                                        it.putExtra("shouldshow",false);
                                        it.putExtra("product_id",recommends.get(index).getProduct_id());
                                        startActivity(it);
                                        popupLayout.dismiss();

                                    }
                                });
                                product_img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it =new Intent(CheckProductActivity.this,CheckProductActivity.class);
                                        it.putExtra("shouldshow",false);
                                        it.putExtra("product_id",recommends.get(index).getProduct_id());
                                        startActivity(it);
                                        popupLayout.dismiss();
                                    }
                                });
                                product_name.setText(recommends.get(index).getProduct_name() );
                                Glide.with(getApplicationContext() ).load(recommends.get(index).getProduct_img()).placeholder(R.drawable.ic_error).into(product_img);
                                popupLayout.setHeight(185,true);
                                popupLayout.setWidth(100,true);
                                popupLayout.show(PopupLayout.POSITION_RIGHT|PopupLayout.POSITION_BOTTOM);
                            }


                        }
                    }

                });




            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }

    private void AddFoot() {
        UserManage.getInstance().saveFoot(this,product);
    }

    private void initData(){
        progressBar.setVisibility(View.VISIBLE);

        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.product(),"line",product_id));
        if(result.getCode()!=200){
            Toast.makeText(this, "获取信息失败", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            product = DatabaseUtil.getEntity(result,Product.class);
            store = DatabaseUtil.getEntity(DatabaseUtil.selectById(HttpAddress.get(HttpAddress.store(),"line",product.getStore_id())),Store.class);
            if(store ==null){
                Toast.makeText(this, "数据获取失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if(product.getProductCommentList().get(0).getOrder_id()==null){
                product.getProductCommentList().remove(0);
            }
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_id", String.valueOf(UserManage.getInstance().getUserInfo(this).getId()));
        paramsMap.put("product_id", product_id);

        Result result1 = DatabaseUtil.postParamters(paramsMap,"collection","line");
        if(result1.getCode()!=200){

        }else {
            userCollection = DatabaseUtil.getEntity(result1, UserCollection.class);
           if(userCollection!=null){
               isCollected = true;
               collect.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_collect_orange,0,0);
           }
        }
        progressBar.setVisibility(View.GONE);


    }
    private void init(){
        product_des = findViewById(R.id.product_des);
        product_des.setText(product.getProduct_des() );
        edit_box = findViewById(R.id.edit_box);
        edit_box.setFocusable(false);
        edit_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(CheckProductActivity.this,SearchActivity.class);
            }
        });
        check_all = findViewById(R.id.check_all);
        check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(CheckProductActivity.this,CheckAllCommentActivity.class,(Serializable) product.getProductCommentList(),"productcomments");
            }
        });
        recommend_product = findViewById(R.id.recommend_product);
        Iterator it =  store.getStore_products().iterator();
        while (it.hasNext()){
            Product temp = (Product)it.next();
            if(temp.getProduct_id().equals(product.getProduct_id())){
                it.remove();
                break;
            }
        }

        StoreProductGridLayoutAdapter storeProductGridLayoutAdapter = new StoreProductGridLayoutAdapter(this,store.getStore_products()){
            @Override
            public int getItemCount() {
                return Math.min(store.getStore_products().size(),6);
            }
        };
        recommend_product.setAdapter(storeProductGridLayoutAdapter);
        recommend_product.setLayoutManager(new GridLayoutManager(this,store.getStore_products().size()>2?3:2));
        nullcomment = findViewById(R.id.nullcomment);
        addtocar  =findViewById(R.id.addtocar);
        addtocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shoppingcar shoppingcar = new Shoppingcar(product_id,UserManage.getInstance().getUserInfo(CheckProductActivity.this).getId());
                Result result = DatabaseUtil.insert(shoppingcar,HttpAddress.get(HttpAddress.shoppingcar(),"insert"));
                if(result.getCode()!=200){
                    Toast.makeText(CheckProductActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CheckProductActivity.this, "已添加至购物车", Toast.LENGTH_SHORT).show();
                    shouldrefresh =true;
                }
            }
        });
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Store> stores = new ArrayList<>();
                Store store = new Store();
                store.setStore_id(product.getStore_id() );
                store.setStore_name(product.getStore_name());
                List<Product> products = new ArrayList<>();
                product.setProduct_num(1);
                product.setSelected(true);
                products.add(product);
                store.setStore_products(products);
                stores.add(store);

                UserManage.getInstance().Jump(CheckProductActivity.this,PayOrderActivity.class,(Serializable)stores,"order");
            }
        });
        rec_comment = findViewById(R.id.rec_comment);

        if(product.getProductCommentList().isEmpty()){
            rec_comment.setVisibility(View.GONE);
            nullcomment.setVisibility(View.VISIBLE);
        }else{
            rec_comment.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
            rec_comment.setAdapter(new ProductCommentAdapter(this,product.getProductCommentList()));

        }


        leave_bt =findViewById(R.id.leave_bt);
        search_bar = findViewById(R.id.search_bar);
        product_price =findViewById(R.id.product_price);
        product_name = findViewById(R.id.product_name);
        store_name = findViewById(R.id.store_name);
        store_goto = findViewById(R.id.store_goto);



        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCollected ){
                    Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.collection(),"delete", userCollection.getId()));
                    if(result.getCode()!=200){
                        Toast.makeText(CheckProductActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CheckProductActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        collect.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_collect,0,0);
                        isCollected = false;
                    }
                }else{
                    UserCollection tem = new UserCollection();
                    tem.setProduct_id(product.getProduct_id());
                    tem.setProduct_name(product.getProduct_name() );
                    tem.setProduct_img(product.getProduct_img());
                    tem.setStore_id(product.getStore_id());
                    tem.setUser_id(UserManage.getInstance().getUserInfo(CheckProductActivity.this).getId() );
                    tem.setStore_name(product.getStore_name() );
                    Result result = DatabaseUtil.insert(tem,HttpAddress.get(HttpAddress.collection(),"insert"));
                    if(result.getCode()!=200){
                        Toast.makeText(CheckProductActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }else{
                        userCollection = DatabaseUtil.getEntity(result,UserCollection.class);
                        Toast.makeText(CheckProductActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        collect.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_collect_orange,0,0);
                        isCollected = true;
                    }
                }


            }
        });

        banner = findViewById(R.id.product_img);
        store_img = findViewById(R.id.store_img);
        Glide.with(this).load(store.getStore_img()).placeholder(R.drawable.ic_error).into(store_img);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        store_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(CheckProductActivity.this,CheckStoreActivity.class,product.getStore_id(),"store_id");
            }
        });

        store_part =findViewById(R.id.store_part);
        store_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(CheckProductActivity.this,CheckStoreActivity.class,product.getStore_id(),"store_id");
            }
        });

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(CheckProductActivity.this,SearchActivity.class);
            }
        });
        product_price.setText(String.valueOf(product.getProduct_price() ));
        product_name.setText(product.getProduct_name());
        store_name.setText(product.getStore_name());

        imglist = StringUtil.splitBySymbol(product.getProduct_img(),"|");

        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new ImageAdatper1(imglist))
                .setIndicator(new CircleIndicator(this));

//        Drawable drawable = getDrawable(R.drawable.ic_collect_full);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
    }

}