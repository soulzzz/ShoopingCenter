package com.xmut.shoppingcenter.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;

import java.util.List;

public class CheckExpressAdapter extends RecyclerView.Adapter<CheckExpressAdapter.Myholder>{
    Context context;
    List<Product> productList;
    private ClipData mClipData;   //剪切板Data对象
    private ClipboardManager mClipboardManager;   //剪切板管理工具类

    public void setmClipData(ClipData mClipData) {
        this.mClipData = mClipData;
    }

    public void setmClipboardManager(ClipboardManager mClipboardManager) {
        this.mClipboardManager = mClipboardManager;
    }

    public CheckExpressAdapter(Context context) {
        this.context = context;
    }
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_express,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        Glide.with(context).load(productList.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.product_icon);
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.exprss.setText("物流单号(点击复制): "+productList.get(position).getExpress_num());
        holder.exprss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipData = ClipData.newPlainText("Simple text",productList.get(position).getExpress_num() );
                //把clip对象放在剪贴板中
                mClipboardManager.setPrimaryClip(mClipData);
                Toast.makeText(context, "复制成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        RadiuImageView product_icon;
        TextView product_name,exprss;

        public Myholder(@NonNull View itemView) {

            super(itemView);
            product_icon = itemView.findViewById(R.id.product_icon);
            product_name=  itemView.findViewById(R.id.product_name);
            exprss = itemView.findViewById(R.id.exprss);
        }
    }
}
