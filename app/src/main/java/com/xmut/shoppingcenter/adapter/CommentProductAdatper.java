package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentProductAdatper extends RecyclerView.Adapter<CommentProductAdatper.Myholder>{
    List<Product> productList;
    Context context;
    Map<Integer,List<Uri>> test;
    public CommentProductAdatper(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.fragment_comment_product,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        List<Uri> myresult = new ArrayList<>();
        Glide.with(context).load(productList.get(holder.getAdapterPosition()).getProduct_img()).placeholder(R.drawable.ic_error).into(holder.product_icon);
        holder.product_name.setText(productList.get(holder.getAdapterPosition()).getProduct_name());
        holder.uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadListener.onUpload(holder.getAdapterPosition());
            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.rec_img.setLayoutManager(new FlowLayoutManager(context,true));
        GridImageAdapter  gridImageAdapter = new GridImageAdapter(context, myresult);
        holder.rec_img.setAdapter(gridImageAdapter);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface UploadListener{
        void onUpload(int index);
    }
    UploadListener uploadListener;

    public UploadListener getUploadListener() {
        return uploadListener;
    }

    public void setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    class Myholder extends RecyclerView.ViewHolder{
        ImageView product_icon;
        TextView product_name,uploadImg,submit;
        EditText comment;
        RatingBar ratingbar;
        RecyclerView rec_img;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_icon = itemView.findViewById(R.id.product_icon);
            product_name = itemView.findViewById(R.id.product_name);
            uploadImg = itemView.findViewById(R.id.uploadImg);
            submit = itemView.findViewById(R.id.submit);
            comment = itemView.findViewById(R.id.comment);
            ratingbar = itemView.findViewById(R.id.ratingbar);
            rec_img = itemView.findViewById(R.id.rec_img);
        }
    }
}
