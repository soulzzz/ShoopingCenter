package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

public class CheckOrderProductAdapter extends RecyclerView.Adapter<CheckOrderProductAdapter.Myholder> {
    Context context;
    List<Product> productList;

    public CheckOrderProductAdapter(Context context) {
        this.context = context;
    }

    public CheckOrderProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public CheckOrderProductAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_check_order_product,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOrderProductAdapter.Myholder holder, int position) {

        Glide.with(context).load(StringUtil.getFirstSplitBySymbol(productList.get(position).getProduct_img(),"|")).placeholder(R.drawable.ic_error).into(holder.radiuImageView);
        holder.product_num.setText( String.valueOf(productList.get(position).getProduct_num() ));
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText( OrderUtil.PriceFix(productList.get(position).getProduct_price()) );
        holder.store_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Product Part");
                secondListener.onUpdate(productList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface SecondListener {
        void onUpdate(Product product);

    }
    private SecondListener secondListener;

    public SecondListener getSecondListenerListener() {
        return secondListener;
    }

    public void setSecondListenerListener(SecondListener secondListener) {
        this.secondListener = secondListener;
    }
    class Myholder extends RecyclerView.ViewHolder{
        TextView product_num,product_name;
        RadiuImageView radiuImageView;
        TextView product_price;
        RelativeLayout store_div;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_num = itemView.findViewById(R.id.product_num);
            product_name = itemView.findViewById(R.id.product_name);
            radiuImageView = itemView.findViewById(R.id.product_icon);
            store_div = itemView.findViewById(R.id.store_div);
            product_price = itemView.findViewById(R.id.product_price);

        }
    }
}
