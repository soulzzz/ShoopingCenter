package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckStoreActivity;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.Store;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.Myholder> {
    private String Tag="ShoppingCarAdapter";
    private Context context;
    private List<Store> storeList;
    public ShoppingCarAdapter(Context context,List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;

    }

    public ShoppingCarAdapter(Context context) {
        this.context = context;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_shoppingcar_store,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        holder.store_name.setText(storeList.get(position).getStore_name() );
        ShoppingCarDetailAdapter shoppingCarDetailAdapter =new ShoppingCarDetailAdapter(context,storeList.get(holder.getAdapterPosition()).getStore_products() );
        shoppingCarDetailAdapter.setSecondListenerListener(new ShoppingCarDetailAdapter.SecondListener() {
            @Override
            public void onDel(int pos) {

                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.shoppingcar(), "delete", storeList.get(holder.getAdapterPosition()).getStore_products().get(pos).getShoppingcar_id()));
                if (result.getCode() != 200) {
                    Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                    //数据库删除成功 将此item从shoppingCarDetailAdapter中通知删除
                    storeList.get(holder.getAdapterPosition()).getStore_products().remove(pos);
                    shoppingCarDetailAdapter.notifyItemRemoved(pos);

                    //更新查看删除了此商品后商店的list是否为空 为空则删除此商店并通知ShoppingcarAdatper
                    if (storeList.get(holder.getAdapterPosition()).getStore_products().isEmpty()) {
                        storeList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    } else {  //判断删除后 是否需要勾选 商店旁的全选按钮
                        for (int i = 0; i < storeList.get(holder.getAdapterPosition()).getStore_products().size(); i++) {
                            if (!storeList.get(holder.getAdapterPosition()).getStore_products().get(i).isSelected()) {
                                storeList.get(holder.getAdapterPosition()).setSelected(false);
                                holder.selector_store.setSelected(false);
                                break;
                            }
                            if (i == storeList.get(holder.getAdapterPosition()).getStore_products().size() - 1) {
                                storeList.get(holder.getAdapterPosition()).setSelected(true);
                                holder.selector_store.setSelected(true);
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        }
                    }
                    //通知NoticeFragment中是否更新下方的全选按钮
                    firstListener.onSelect();

                }
            }

            @Override
            public void onSelect() {
                //判断选中后 是否需要勾选 商店旁的全选按钮
                for(int i=0;i<storeList.get(holder.getAdapterPosition()).getStore_products().size();i++){
                    if( !storeList.get(holder.getAdapterPosition()).getStore_products().get(i).isSelected() ){
                        storeList.get(holder.getAdapterPosition() ).setSelected(false);
                        holder.selector_store.setSelected(false);
                        firstListener.onSelect();
                        return;
                    }
                }
                //需要勾选
                storeList.get(holder.getAdapterPosition() ).setSelected(true);
                holder.selector_store.setSelected(true);
                notifyItemChanged(holder.getAdapterPosition() );

                //通知NoticeFragment中是否更新下方的全选按钮
                firstListener.onSelect();


            }
        });


        holder.product_div.setAdapter(shoppingCarDetailAdapter);
        holder.product_div.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

        //用于NoticeFragment 的全选按钮 For ShoppingAdatper.NoticeDatasetchange();
        if(storeList.get( holder.getAdapterPosition() ).isSelected() ){
            holder.selector_store.setSelected(true);
            shoppingCarDetailAdapter.notifyDataSetChanged();
        }else{
            holder.selector_store.setSelected(false);
            shoppingCarDetailAdapter.notifyDataSetChanged();
        }

        //商店旁的全选按钮 选中后将这个DetailAdatper中product selected全true并DetailAdapter.notifyDataSetChanged();
        holder.selector_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storeList.get( holder.getAdapterPosition() ).isSelected()){
                    storeList.get( holder.getAdapterPosition() ).setSelected(false);
                    holder.selector_store.setSelected(false);
                    for(int i=0;i<storeList.get( holder.getAdapterPosition() ).getStore_products().size();i++){
                        storeList.get(holder.getAdapterPosition() ).getStore_products().get(i).setSelected(false);
                    }
                    shoppingCarDetailAdapter.notifyDataSetChanged();
                }else{
                    storeList.get(holder.getAdapterPosition()).setSelected(true);
                    holder.selector_store.setSelected(true);
                    for(int i=0;i<storeList.get( holder.getAdapterPosition() ).getStore_products().size();i++){
                        storeList.get(holder.getAdapterPosition()).getStore_products().get(i).setSelected(true);
                    }
                    shoppingCarDetailAdapter.notifyDataSetChanged();
                }
                firstListener.onSelect();
//                notifyDataSetChanged();
            }
        });

        //通过传Store_id到Store_Activity
        holder.jumptostore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckStoreActivity.class,storeList.get(holder.getAdapterPosition() ).getStore_id(),"store_id");
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public interface FirstListener {

        void onSelect();

    }

    private FirstListener firstListener;

    public FirstListener getFirstListener() {
        return firstListener;
    }

    public void setFirstListener(FirstListener firstListener) {
        this.firstListener = firstListener;
    }
    static class Myholder extends RecyclerView.ViewHolder{
        TextView store_name;
        Button selector_store;
        ImageView jumptostore;
        RecyclerView product_div;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            store_name = (TextView)itemView.findViewById(R.id.store_name);
            selector_store= itemView.findViewById(R.id.selector_store);
            jumptostore = itemView.findViewById(R.id.jumptostore);
            product_div = itemView.findViewById(R.id.product_div);

        }
    }
}
