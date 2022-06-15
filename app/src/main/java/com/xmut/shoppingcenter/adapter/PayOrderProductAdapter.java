package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.StringUtil;

import java.util.List;

public class PayOrderProductAdapter extends RecyclerView.Adapter<PayOrderProductAdapter.Myholder> {
    Context context;
    List<Product> productList;

    public PayOrderProductAdapter(Context context) {
        this.context = context;
    }

    public PayOrderProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_pay_order_product,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        Glide.with(context).load(StringUtil.getFirstSplitBySymbol(productList.get(position).getProduct_img(),"|")).placeholder(R.drawable.ic_error).into(holder.radiuImageView) ;
        holder.product_num.setText( String.valueOf(productList.get(position).getProduct_num() ));
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText( OrderUtil.PriceFix(productList.get(position).getProduct_price()) );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView product_num,product_name;
        RadiuImageView radiuImageView;
        TextView product_price;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_num = itemView.findViewById(R.id.product_num);
            product_name = itemView.findViewById(R.id.product_name);
            radiuImageView = itemView.findViewById(R.id.product_icon);

            product_price = itemView.findViewById(R.id.product_price);

        }
    }
}
