package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder>{
    private final List<Uri> list;
    private int selectMax = 5;
    Context context;

    public GridImageAdapter(Context context, List<Uri> result) {
        this.context = context;
        this.list =result;
    }

    public void remove(int position) {
        if (position < list.size()) {
            list.remove(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.item_grid_img, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap temp = null;
        try {
            temp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), list.get(holder.getAdapterPosition()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.mImg.setImageBitmap(temp);
        holder.mIvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.img);
            mIvDel = view.findViewById(R.id.iv_del);
        }
    }

}
