package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckOrderDetailActivity;
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class CheckOrderDetailStoreAdapter extends RecyclerView.Adapter<CheckOrderDetailStoreAdapter.Myholder>{
    List<Store> storeList;
    Context context;

    public CheckOrderDetailStoreAdapter(Context context) {
        this.context = context;
    }
    public CheckOrderDetailStoreAdapter(Context context, List<Store> storeList) {
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
        View view = View.inflate(parent.getContext(), R.layout.item_pay_order_store, null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        CheckOrderDetailProductAdapter checkOrderDetailProductAdapter = new CheckOrderDetailProductAdapter(context,storeList.get(position).getStore_products());

        holder.product_div.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.product_div.setAdapter(checkOrderDetailProductAdapter );
        holder.store_name.setText(storeList.get(position).getStore_name());
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckStoreActivity.class,storeList.get(position).getStore_id(),"store_id");
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }



    class Myholder extends RecyclerView.ViewHolder {
        TextView store_name;
        RecyclerView product_div;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            store_name = itemView.findViewById(R.id.store_name);
            product_div = itemView.findViewById(R.id.product_div);

        }
    }
}

