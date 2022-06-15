package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;

import java.util.List;

public class CommentImgAdapter extends RecyclerView.Adapter<CommentImgAdapter.Myholder>{
    List<String> imgList;
    Context context;

    public CommentImgAdapter(Context context, List<String> imgList) {
        this.imgList = imgList;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_comment_img,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        Glide.with(context).load(imgList.get(position)).placeholder(R.drawable.ic_error).into(holder.comment_img);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        ImageView comment_img;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            comment_img = itemView.findViewById(R.id.comment_img);
        }
    }
}
