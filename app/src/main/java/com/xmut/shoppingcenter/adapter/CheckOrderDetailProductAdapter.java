package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.ApplyForRefundActivity;
import com.xmut.shoppingcenter.activities.CheckOrderDetailActivity;
import com.xmut.shoppingcenter.activities.CheckReasonActivity;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class CheckOrderDetailProductAdapter extends RecyclerView.Adapter<CheckOrderDetailProductAdapter.Myholder> {
    Context context;
    List<Product> productList;

    public CheckOrderDetailProductAdapter(Context context) {
        this.context = context;
    }

    public CheckOrderDetailProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public CheckOrderDetailProductAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_check_order_detail_product,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        Glide.with(context).load(StringUtil.getFirstSplitBySymbol(productList.get(position).getProduct_img(),"|")).placeholder(R.drawable.ic_error).into(holder.radiuImageView);
        holder.product_num.setText("X "+ String.valueOf(productList.get(position).getProduct_num() ));
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText( OrderUtil.PriceFix(productList.get(position).getProduct_price()) );
        if(productList.get(position).getOrder_state() >1 &&  productList.get(position).getOrder_state() <=5 ){
            holder.product_state.setText("申请退款");
            holder.product_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent((Activity)context,ApplyForRefundActivity.class);
                    it.putExtra("order_id",productList.get(position).getOrder_id() );
                    it.putExtra("product_id",productList.get(position).getProduct_id());
                    it.putExtra("order_state",productList.get(position).getOrder_state());
                    context.startActivity(it);
                }
            });
        }
//        else if(productList.get(position).getOrder_state() >= OrderUtil.refundwaitsend() && productList.get(position).getOrder_state() >= OrderUtil.refundafterreceivedandcommented() ){
//            holder.product_state.setText("查看退款详情");
//
//        }
        else if(productList.get(position).getOrder_state() == OrderUtil.refundsuccess() ){
            holder.product_state.setText("查看退款详情");
            holder.product_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent((Activity)context, CheckReasonActivity.class);
                    it.putExtra("product",productList.get(holder.getAdapterPosition()));
                    it.putExtra("shouldshowaddreason",false);
                    context.startActivity(it);
                }
            });



        }
        else {
            holder.product_state.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView product_num,product_name;
        RadiuImageView radiuImageView;
        TextView product_price,product_state;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_num = itemView.findViewById(R.id.product_num);
            product_name = itemView.findViewById(R.id.product_name);
            radiuImageView = itemView.findViewById(R.id.product_icon);
            product_state = itemView.findViewById(R.id.product_state);
            product_price = itemView.findViewById(R.id.product_price);

        }
    }
}
