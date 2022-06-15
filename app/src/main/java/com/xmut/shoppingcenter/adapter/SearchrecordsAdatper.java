package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.AfterSearchActivity;
import com.xmut.shoppingcenter.activities.SearchActivity;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;

public class SearchrecordsAdatper extends RecyclerView.Adapter<SearchrecordsAdatper.Myholder>{
    Context context;
    List<String> searchrecords;

    public SearchrecordsAdatper(Context context, List<String> searchrecords) {
        this.context = context;
        this.searchrecords = searchrecords;

    }

    public void setrecords(List<String> records) {
        this.searchrecords = records;
        notifyDataSetChanged();
    }
    public void  clearrecords(){
        this.searchrecords.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchrecordsAdatper.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_searchrecord,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchrecordsAdatper.Myholder holder, int position) {
        holder.textView.setText(searchrecords.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump( (Activity) context, AfterSearchActivity.class , searchrecords.get(position),"searchcontent");
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchrecords.size();
    }

    static class Myholder extends RecyclerView.ViewHolder{
        TextView textView;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.record);

        }
    }
}
