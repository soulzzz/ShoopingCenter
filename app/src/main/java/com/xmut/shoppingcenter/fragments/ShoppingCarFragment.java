package com.xmut.shoppingcenter.fragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.PayOrderActivity;
import com.xmut.shoppingcenter.adapter.ShoppingCarAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Shoppingcar;
import com.xmut.shoppingcenter.entity.ShoppingcarDetail;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.okhttp.http.HttpUrl;
import com.xmut.shoppingcenter.okhttp.http.OKHttpUtil;
import com.xmut.shoppingcenter.utils.ListUtil;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShoppingCarFragment extends Fragment {
    public static boolean shouldrefresh = false;
    private View view;
    private RelativeLayout select_div,pay_div;
    private TextView manage,price;
    private boolean isManaging =false;
    private Button pay,delete_bt,select_bt;
    private Context context;
    private RecyclerView recyclerview;
    private List<Store> storeList; //传给ShoppingcarAdapter
    private List<ShoppingcarDetail> shoppingcarDetails;
    private ShoppingCarAdapter shoppingCarAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean allSelected = false;
    private RelativeLayout nullshow;
    private boolean alreadydel = false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.fragment_shopping_car,container, false);
        init();
        initData();
        shoppingCarAdapter =new ShoppingCarAdapter(context);
        shoppingCarAdapter.setStoreList(storeList);

        shoppingCarAdapter.setFirstListener(new ShoppingCarAdapter.FirstListener() {
            @Override
            public void onSelect() {
                if(storeList.isEmpty()) {
                    allSelected = false;
                    select_bt.setSelected(false);
                    nullshow.setVisibility(View.VISIBLE);
                }else{
                   CalculatePrice();
                    //循环判断是否有一个
                    for(int i=0;i<storeList.size();i++){
                        if( !storeList.get(i).isSelected() ){
                            allSelected =false;
                            select_bt.setSelected(false);
                            return;
                        }
                    }
                    allSelected =true;
                    select_bt.setSelected(true);


                     }
            }
        });
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(shoppingCarAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldrefresh){
            initData();
            shoppingCarAdapter.setStoreList(storeList);
            shouldrefresh= false;
        }
    }

    private void init() {
        nullshow = view.findViewById(R.id.nullshow);
        select_bt =view.findViewById(R.id.select_bt);
        recyclerview = view.findViewById(R.id.recyclerview);
        select_div =view.findViewById(R.id.select_div);
        manage = view.findViewById(R.id.manage);
        pay_div = view.findViewById(R.id.pay_div);
        price = view.findViewById(R.id.price);
        pay =view.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Store> temp = null;
                try {
                    temp = ListUtil.deepCopy(storeList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Iterator it0 = temp.iterator();
                while(it0.hasNext()){
                    Store store = (Store)it0.next();

                    Iterator it = store.getStore_products().iterator();
                    while(it.hasNext()){
                        Product product = (Product)it.next();
                        if (!product.isSelected()) {
                            it.remove(); //移除该对象
                        }
                    }

                    if(store.getStore_products().isEmpty()){
                        it0.remove();
                    }

                }

                if(temp.isEmpty()){
                    Toast.makeText(context, "未选中任意商品", Toast.LENGTH_SHORT).show();
                }else{
                    UserManage.getInstance().Jump((Activity) context, PayOrderActivity.class,(Serializable)temp,"order");
                }

            }
        });
        delete_bt =view.findViewById(R.id.delete_bt);
        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!storeList.isEmpty()){

                    Iterator<Store> it = storeList.iterator();
                    while(it.hasNext()){
                        Store store = it.next();
                        Iterator<Product> it2 = store.getStore_products().iterator();

                        while(it2.hasNext()){
                            Product product = it2.next();

                            if(product.isSelected()){

                                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.shoppingcar(),"delete",product.getShoppingcar_id() ) );
                                if(result.getCode()!=200){
                                    Toast.makeText(context,product.getProduct_name()+result.getMsg(),Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    //数据库删除成功 将list中的product删除
                                    it2.remove();
                                    alreadydel =true;
                                }
                            }
                        }
                        //商品删除完 若商店中全部商品为空则删除商店
                        if(store.getStore_products().isEmpty() ){
                            it.remove();
                        }
                    }

                }
                shoppingCarAdapter.notifyDataSetChanged();
                if(alreadydel) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    isManaging = false;
                    manage.setText("管理");
                    delete_bt.setVisibility(View.GONE);
                    pay_div.setVisibility(View.VISIBLE);
                    alreadydel = false;
                    if (storeList.isEmpty()) {
                        allSelected = false;
                        select_bt.setSelected(false);
                        nullshow.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isManaging ==true){
                    isManaging =false;
                    manage.setText("管理");
                    delete_bt.setVisibility(View.GONE);
                    pay_div.setVisibility(View.VISIBLE);
                }else{
                    isManaging =true;
                    manage.setText("取消");
                    delete_bt.setVisibility(View.VISIBLE);
                    pay_div.setVisibility(View.GONE);
                }
            }
        });
        select_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!allSelected){
                    for(int i=0;i<storeList.size();i++){
                        storeList.get(i).setSelected(true);
                        for(int j=0;j<storeList.get(i).getStore_products().size();j++){
                            storeList.get(i).getStore_products().get(j).setSelected(true);
                        }

                    }
                    shoppingCarAdapter.notifyDataSetChanged();

                    select_bt.setSelected(true);
                }else
                {
                    for(int i=0;i<storeList.size();i++){
                        storeList.get(i).setSelected(false);
                        for(int j=0;j<storeList.get(i).getStore_products().size();j++){
                            storeList.get(i).getStore_products().get(j).setSelected(false);
                        }
                    }
                    shoppingCarAdapter.notifyDataSetChanged();
                    select_bt.setSelected(false);
                }
                CalculatePrice();
                allSelected = !allSelected;

            }
        });

    }

    private void initData(){
        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.shoppingcar(),"list", UserManage.getInstance().getUserInfo(context).getId() ) ) ;
        shoppingcarDetails = DatabaseUtil.getObjectList(result, ShoppingcarDetail.class);

        Map<Integer,List<ShoppingcarDetail> > stringListMap = shoppingcarDetails.stream().collect(Collectors.groupingBy(ShoppingcarDetail::getStore_id));
        shoppingcarDetails.clear();
        //将map集合转为set集合遍历
        Set<Map.Entry<Integer, List<ShoppingcarDetail>>> entries = stringListMap.entrySet();
        for(Map.Entry m : entries){
            shoppingcarDetails.addAll((List<ShoppingcarDetail>)m.getValue() ) ;
        }
        storeList = new ArrayList<>();
        int temp = 0;
        for(int i=0,j=-1;i<shoppingcarDetails.size();i++){
            Product product = new Product();
            product.setStore_id(shoppingcarDetails.get(i).getStore_id());
            product.setShoppingcar_id(shoppingcarDetails.get(i).getId() );
            product.setProduct_id(shoppingcarDetails.get(i).getProduct_id() );
            product.setProduct_name( shoppingcarDetails.get(i).getProduct_name());
            product.setProduct_img( shoppingcarDetails.get(i).getProduct_img());
            product.setProduct_price(shoppingcarDetails.get(i).getProduct_price());
            product.setProduct_stock( shoppingcarDetails.get(i).getProduct_stock());
            product.setProduct_isshow(shoppingcarDetails.get(i).getProduct_isshow() );
            product.setProduct_num(1);
            product.setSelected(false);
            if(i==0){
                j++;
                temp = shoppingcarDetails.get(i).getStore_id();
                Store store = new Store();
                store.setStore_id( shoppingcarDetails.get(i).getStore_id());
                store.setStore_name(shoppingcarDetails.get(i).getStore_name());
                List<Product> products = new ArrayList<>();
                products.add(product);
                store.setStore_products(products);

                storeList.add(store);
            }else{
                if(temp != shoppingcarDetails.get(i).getStore_id() ){
                    j++;
                    temp = shoppingcarDetails.get(i).getStore_id();
                    Store store = new Store();
                    store.setStore_id( shoppingcarDetails.get(i).getStore_id());
                    store.setStore_name(shoppingcarDetails.get(i).getStore_name());
                    List<Product> products = new ArrayList<>();
                    products.add(product);
                    store.setStore_products(products);

                    storeList.add(store);

                }else{
                    List<Product> templist = storeList.get(j).getStore_products();
                    templist.add(product);
                    storeList.get(j).setStore_products(templist ) ;
                }
            }
        }
        if(storeList.isEmpty()){
            nullshow.setVisibility(View.VISIBLE);
        }else{
            nullshow.setVisibility(View.GONE);
        }
    }
    private void CalculatePrice(){
        double temp = 0;
        for(int i=0;i<storeList.size();i++){
            for(int j = 0;j<storeList.get(i).getStore_products().size();j++){
                if(storeList.get(i).getStore_products().get(j).isSelected()){
                    temp += (storeList.get(i).getStore_products().get(j).getProduct_price()*storeList.get(i).getStore_products().get(j).getProduct_num() );
                }
            }
        }
        price.setText(OrderUtil.PriceFix(temp));
    }
}