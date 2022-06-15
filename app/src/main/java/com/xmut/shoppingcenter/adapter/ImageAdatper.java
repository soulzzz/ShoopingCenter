package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xmut.shoppingcenter.activities.CheckProductActivity;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.utils.UserManage;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class ImageAdatper extends BannerAdapter<String, ImageAdatper.BannerViewHolder> {
    List<String> productsid;
    Context context;
    public ImageAdatper(List<String> datas,List<String> productsid,Context context) {
        super(datas);
        this.productsid=  productsid;
        this.context = context;
    }

    public List<String> getProductsid() {
        return productsid;
    }

    public void setProductsid(List<String> productsid) {
        this.productsid = productsid;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        Glide.with(holder.itemView)
                .load(data)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckProductActivity.class,productsid.get(position),"product_id");
            }
        });
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
