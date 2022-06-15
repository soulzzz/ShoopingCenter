package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckProductActivity;
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.entity.UserCollection;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class CheckCollectionAdapter extends RecyclerView.Adapter<CheckCollectionAdapter.Myholder>{
    List<UserCollection> userCollections;
    Context context;

    public CheckCollectionAdapter( Context context,List<UserCollection> userCollections) {
        this.userCollections = userCollections;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_check_foot,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.product_name.setText(userCollections.get(position).getProduct_name() );
        Glide.with(context).load(StringUtil.getFirstSplitBySymbol(userCollections.get(position).getProduct_img(),"|") ).placeholder(R.drawable.ic_error).into(holder.product_icon);
        holder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckProductActivity.class, userCollections.get(position).getProduct_id(),"product_id");
            }
        });
        holder.product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckProductActivity.class, userCollections.get(position).getProduct_id(),"product_id");
            }
        });
        holder.store_name.setText(userCollections.get(holder.getAdapterPosition()).getStore_name() );
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckStoreActivity.class, userCollections.get(position).getProduct_id(),"store_id");
            }
        });
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.collection(),"delete", userCollections.get(holder.getAdapterPosition()).getId()));
                if(result.getCode()!=200){
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    userCollections.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return userCollections.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        RadiuImageView product_icon;
        TextView product_name;
        TextView store_name;
        Button bt_delete;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_icon = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            store_name = itemView.findViewById(R.id.store_name);
            bt_delete = itemView.findViewById(R.id.bt_delete);
        }
    }
}
