package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.UserAddress;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Myholder> {
    Context context;
    List<UserAddress> userAddresses;

    public AddressAdapter(Context context, List<UserAddress> userAddresses) {
        this.context = context;
        this.userAddresses = userAddresses;
    }
    public void setUserAddresses(List<UserAddress> userAddresses){
        this.userAddresses = userAddresses;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.item_address,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.address.setText(userAddresses.get(position).getUser_address());
        holder.address_tel.setText(userAddresses.get(position).getUser_tel() );
        holder.address_name.setText(userAddresses.get(position).getUser_name());
        holder.address_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstListener.onSelect(holder.getAdapterPosition() );
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstListener.onDelete(holder.getAdapterPosition() );
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstListener.onEdit(holder.getAdapterPosition() );
            }
        });

    }

    @Override
    public int getItemCount() {
        return userAddresses.size();
    }

    public interface FirstListener {

        void onSelect(int pos);
        void onDelete(int pos);
        void onEdit(int pos);
    }
    private FirstListener firstListener;

    public FirstListener getFirstListener() {
        return firstListener;
    }

    public void setFirstListener(FirstListener firstListener) {
        this.firstListener = firstListener;
    }
    class Myholder extends RecyclerView.ViewHolder{
        TextView address_name,address_tel,address;
        Button btnEdit,btnDelete;
        RelativeLayout address_part;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            address_name = itemView.findViewById(R.id.address_name);
            address_tel = itemView.findViewById(R.id.address_tel);
            address = itemView.findViewById(R.id.address);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete=  itemView.findViewById(R.id.btnDelete);
            address_part = itemView.findViewById(R.id.address_part);
        }
    }
}
