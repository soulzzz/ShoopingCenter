package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.ProductComment;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;
import com.xmut.shoppingcenter.utils.ListUtil;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductCommentAdapter extends RecyclerView.Adapter<ProductCommentAdapter.Myholder> {
    Context context;
    List<ProductComment> productComments;
    public ProductCommentAdapter(Context context, List<ProductComment> productComments) {
        this.context = context;
        this.productComments = productComments;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_productcomment,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.ratingBar.setRating((float)productComments.get(position).getProduct_score());
        holder.uid.setText(String.valueOf(productComments.get(position).getUser_name()));
        holder.time.setText(TimeUtil.getInstance().FormatHourTime( productComments.get(position).getCreatetime()) );
        holder.comment.setText(productComments.get(position).getProduct_comment());
        CommentImgAdapter commentImgAdapter = new CommentImgAdapter(context, StringUtil.splitBySymbol(productComments.get(position).getComment_img(),"|"));
        holder.rec_img.setAdapter(commentImgAdapter);
        holder.rec_img.setLayoutManager(new FlowLayoutManager(context,true));
    }


    @Override
    public int getItemCount() {
        return (Math.min(productComments.size(), 3));
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView uid,time,comment;
        RatingBar ratingBar;
        RecyclerView rec_img;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            uid = itemView.findViewById(R.id.uid);
            time = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            rec_img =itemView.findViewById(R.id.rec_img);
        }
    }
}
