package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckOrderDetailActivity;
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.activities.CommentProductActivity;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.utils.ListUtil;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class  CheckOrderStoreAdapter extends  RecyclerView.Adapter<CheckOrderStoreAdapter.Myholder> {
    List<Store> storeList;
    Context context;

    public CheckOrderStoreAdapter(Context context) {
        this.context = context;
    }
    public CheckOrderStoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_check_order_store, null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        CheckOrderProductAdapter checkOrderProductAdapter = new CheckOrderProductAdapter(context,storeList.get(position).getStore_products());
        checkOrderProductAdapter.setSecondListenerListener(new CheckOrderProductAdapter.SecondListener() {
            @Override
            public void onUpdate(Product product) {

                System.out.println("Store Part");
                firstListener.onUpdate(product);
            }
        });
        holder.relative_product_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckOrderDetailActivity.class,storeList.get(position).getStore_products().get(0).getOrder_id(),"orderid");
            }
        });
        holder.product_div.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.product_div.setAdapter(checkOrderProductAdapter);
        holder.store_name.setText(storeList.get(position).getStore_name());
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckStoreActivity.class,storeList.get(position).getStore_id(),"store_id");
            }
        });
        Double price =0.0;
       for(Product product :storeList.get(position).getStore_products()){
           price += product.getDetail_price();
       }
       holder.actual_pay.setText("¥"+OrderUtil.PriceFix(price) );
        if(storeList.get(holder.getAdapterPosition() ).getStore_products().get(0).getOrder_state() == OrderUtil.waitpay() ){
            holder.state.setText("待付款");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstListener.onUpdate(storeList.get(position).getStore_products().get(0) );
                }
            });


        }else if(storeList.get(holder.getAdapterPosition() ).getStore_products().get(0).getOrder_state() == OrderUtil.waitreceive() ){
            holder.state.setText("确认收货");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Product> temp = null;
                    try {
                        temp = ListUtil.deepCopy(storeList.get(position).getStore_products());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    for(Product p:temp){
                        if(p.getOrder_state() == OrderUtil.waitreceive())
                            p.setOrder_state( OrderUtil.success() );
                    }
                    Result result = DatabaseUtil.updateById(temp,"order","updatelist");
                    if(result.getCode()!=200){
                        Toast.makeText(context, "更新收货失败", Toast.LENGTH_SHORT).show();
                    }else{
                        storeList.remove( holder.getAdapterPosition());
                        notifyItemRemoved( holder.getAdapterPosition() );
                        Toast.makeText(context, "更新收货成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(storeList.get(position).getStore_products().get(0).getOrder_state() ==OrderUtil.success() ){
            holder.state.setText("待评价");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserManage.getInstance().Jump((Activity)context, CommentProductActivity.class,(Serializable)storeList.get(position).getStore_products(),"product");
                }
            });
        }else if(storeList.get(position).getStore_products().get(0).getOrder_state() ==OrderUtil.successcomment() ){
            holder.state.setText("已完成");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "已完成该订单", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(storeList.get(position).getStore_products().get(0).getOrder_state() ==OrderUtil.waitsend() ){
            holder.state.setText("待发货");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "请耐心等待发货", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(storeList.get(position).getStore_products().get(0).getOrder_state() ==OrderUtil.canceled() ){
            holder.state.setText("已取消");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "订单已取消", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(storeList.get(position).getStore_products().get(0).getOrder_state() ==OrderUtil.refundsuccess() ){
            holder.state.setText("退款成功");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else{
            holder.state.setText("退款中");
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public interface FirstListener {

        void onUpdate(Product product);

    }

    private FirstListener firstListener;

    public FirstListener getFirstListener() {
        return firstListener;
    }

    public void setFirstListener(FirstListener firstListener) {
        this.firstListener = firstListener;
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView store_name,actual_pay,state;
        RecyclerView product_div;
        RelativeLayout relative_product_rec;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            relative_product_rec = itemView.findViewById(R.id.relative_product_rec);
            store_name = itemView.findViewById(R.id.store_name);
            product_div = itemView.findViewById(R.id.product_div);
            actual_pay = itemView.findViewById(R.id.actual_pay);
            state = itemView.findViewById(R.id.state);
        }
    }
}
