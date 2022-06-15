package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckProductActivity;
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class ProductLinearLayoutAdapter extends RecyclerView.Adapter<ProductGridLayoutAdapter.Myholder>{
        Context context;
        List<Product> productList;
        String productimg;

public ProductLinearLayoutAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList =productList;
        }

public void setProductList(List<Product> productList) {
        this.productList =productList;
        notifyDataSetChanged();
        }

@NonNull
@Override
public ProductGridLayoutAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = View.inflate(parent.getContext(),R.layout.item_product_list,null);
    return new ProductGridLayoutAdapter.Myholder(view);
        }

@Override
public void onBindViewHolder(@NonNull ProductGridLayoutAdapter.Myholder holder, int position) {
    productimg = StringUtil.getFirstSplitBySymbol(productList.get(position).getProduct_img(),"|");
    Glide.with(context).load(productimg).placeholder(R.drawable.ic_error).into(holder.product_img);
    holder.product_name.setText(productList.get(position).getProduct_name());
    holder.product_price.setText(String.valueOf(productList.get(position).getProduct_price()));
    holder.product_sales.setText(String.valueOf(productList.get(position).getProduct_sales()) );
    holder.store_name.setText(productList.get(position).getStore_name());
    holder.store_name.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserManage.getInstance().Jump((Activity)context, CheckStoreActivity.class,productList.get(position).getStore_id(),"store_id");
        }
    });
    holder.product_div.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserManage.getInstance().Jump((Activity)context, CheckProductActivity.class);
        }
    });
        }

@Override
public int getItemCount() {
        return productList.size();
        }

    static class Myholder extends RecyclerView.ViewHolder{
        TextView product_name,product_price,product_sales,store_name;
        ImageView product_img;
        RelativeLayout product_div;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_sales = itemView.findViewById(R.id.product_sales);
            store_name = itemView.findViewById(R.id.store_name);
            product_div = itemView.findViewById(R.id.product_div);

        }
    }
}
