package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCarDetailAdapter extends RecyclerView.Adapter<ShoppingCarDetailAdapter.Myholder> {

    List<Product> productList;
    Context context;
    public ShoppingCarDetailAdapter(Context context,List<Product> productList) {
        this.context=  context;
        this.productList = productList;

    }


    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_shoppingcar_product,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        //用于ShoppingcarAdatper 的NoticeDatasetchange();
        if(productList.get(holder.getAdapterPosition()).isSelected() ){

            holder.selector_product.setSelected(true);
        }else{
            holder.selector_product.setSelected(false);

        }

        if(productList.get(holder.getAdapterPosition()).getProduct_num()>1){
            holder.subtract_bt.setVisibility(View.VISIBLE);
        }else{holder.subtract_bt.setVisibility(View.GONE);}
        Glide.with(context).load(productList.get(position).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.radiuImageView) ;
        holder.add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int temp =  productList.get(holder.getAdapterPosition()).getProduct_num()+1;
               if(temp>=2 ){
                   holder.subtract_bt.setVisibility(View.VISIBLE);
               }

               if(temp < productList.get(position).getProduct_stock() ){
                   productList.get(holder.getAdapterPosition()).setProduct_num(Math.min(temp,productList.get(position).getProduct_stock()));
                   holder.product_num.setText( String.valueOf(productList.get(position).getProduct_num()));

               }else{
                   Toast.makeText(context, "超出库存", Toast.LENGTH_SHORT).show();
               }
                secondListener.onSelect();
            }
        });
        holder.subtract_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp =  productList.get(holder.getAdapterPosition()).getProduct_num()-1;
                if(temp<2){
                    holder.subtract_bt.setVisibility(View.GONE);
                }
                productList.get(holder.getAdapterPosition()).setProduct_num( Math.max(temp,1) );
                holder.product_num.setText(String.valueOf(productList.get(holder.getAdapterPosition()).getProduct_num() ));
                secondListener.onSelect();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondListener.onDel(holder.getAdapterPosition());

            }
        });
        holder.product_num.setText( String.valueOf(productList.get(position).getProduct_num() ));
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText( String.valueOf(productList.get(position).getProduct_price()) );
        holder.selector_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当已被选中时，再次点击则取消选中
                if(productList.get( holder.getAdapterPosition() ).isSelected()){
                    productList.get( holder.getAdapterPosition() ).setSelected(false);
                    holder.selector_product.setSelected(false);


                }else{
                    productList.get( holder.getAdapterPosition() ).setSelected(true);
                    holder.selector_product.setSelected(true);

                }
                secondListener.onSelect();

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface SecondListener {
        void onDel(int pos);

        void onSelect();

    }

    private SecondListener secondListener;

    public SecondListener getSecondListenerListener() {
        return secondListener;
    }

    public void setSecondListenerListener(SecondListener secondListener) {
        this.secondListener = secondListener;
    }

    static class Myholder extends RecyclerView.ViewHolder{
        TextView product_num,product_name;
        RadiuImageView radiuImageView;
        ImageView add_bt,subtract_bt;
        Button selector_product;
        TextView product_price;
        Button btnDelete;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_num = itemView.findViewById(R.id.product_num);
            product_name = itemView.findViewById(R.id.product_name);
            radiuImageView = itemView.findViewById(R.id.product_icon);
            selector_product = itemView.findViewById(R.id.selector_product);
            add_bt = itemView.findViewById(R.id.add_bt);
            subtract_bt = itemView.findViewById(R.id.subtract_bt);
            product_price = itemView.findViewById(R.id.product_price);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
