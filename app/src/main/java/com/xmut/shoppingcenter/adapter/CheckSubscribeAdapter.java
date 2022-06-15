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
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Subscribe;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class CheckSubscribeAdapter extends RecyclerView.Adapter<CheckSubscribeAdapter.Myholder>{
    List<Subscribe> subscribes;
    Context context;

    public CheckSubscribeAdapter( Context context,List<Subscribe> subscribes) {
        this.subscribes = subscribes;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_subscribe,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.store_name.setText(subscribes.get(position).getStore_name() );
        Glide.with(context).load(StringUtil.getFirstSplitBySymbol(subscribes.get(position).getStore_img(),"|") ).placeholder(R.drawable.ic_error).into(holder.store_img);

        holder.store_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckStoreActivity.class,subscribes.get(position).getStore_id(),"store_id");
            }
        });
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckStoreActivity.class,subscribes.get(position).getStore_id(),"store_id");
            }
        });
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.subscribe(),"delete",subscribes.get(holder.getAdapterPosition()).getId()));
                if(result.getCode()!=200){
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "退订成功", Toast.LENGTH_SHORT).show();
                    subscribes.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return subscribes.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        RadiuImageView store_img;
        TextView store_name;
        Button bt_delete;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            store_img = itemView.findViewById(R.id.store_img);
            store_name = itemView.findViewById(R.id.store_name);
            bt_delete = itemView.findViewById(R.id.bt_delete);
        }
    }
}
