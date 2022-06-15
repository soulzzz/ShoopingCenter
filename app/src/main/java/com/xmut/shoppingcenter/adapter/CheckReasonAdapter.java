package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.OrderReason;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;
import com.xmut.shoppingcenter.utils.StringUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

public class CheckReasonAdapter extends RecyclerView.Adapter<CheckReasonAdapter.Myholder> {
    Context context;
    List<OrderReason> orderReasonList;

    public CheckReasonAdapter(Context context, List<OrderReason> orderReasonList) {
        this.context = context;
        this.orderReasonList = orderReasonList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =View.inflate(context, R.layout.item_reason,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        if(orderReasonList.get(position).getCommit_person() ==0){
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.time2.setText(TimeUtil.getInstance().FormatHourTime(orderReasonList.get(position).getCommit_date() ));
            holder.reason2.setText( orderReasonList.get(position).getCommit_reason() );


            if(!UserManage.isEmpty( orderReasonList.get(position).getCommit_img())) {
                CommentImgAdapter commentImgAdapter =new CommentImgAdapter(context,StringUtil.splitBySymbol(orderReasonList.get(position).getCommit_img(),"|"));
                holder.rec_reason_img2.setAdapter(commentImgAdapter );
                holder.rec_reason_img2.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,true));
            }

        }else{
            if(orderReasonList.get(position).getCommit_person() == 1){
                holder.role1.setText("商家");
            }
            holder.time1.setText(TimeUtil.getInstance().FormatHourTime(orderReasonList.get(position).getCommit_date() ));
            holder.reason1.setText( orderReasonList.get(position).getCommit_reason() );
            if(!UserManage.isEmpty( orderReasonList.get(position).getCommit_img())) {
                CommentImgAdapter commentImgAdapter =new CommentImgAdapter(context,StringUtil.splitBySymbol(orderReasonList.get(position).getCommit_img(),"|"));
                holder.rec_reason_img1.setLayoutManager(new FlowLayoutManager(context,true));
                holder.rec_reason_img1.setAdapter(commentImgAdapter );

            }
        }

    }

    @Override
    public int getItemCount() {
        return orderReasonList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        RelativeLayout left,right;
        TextView role1,role2,time1,time2,reason1,reason2;
        RecyclerView rec_reason_img1,rec_reason_img2;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);
            role1 = itemView.findViewById(R.id.role1);
            role2 = itemView.findViewById(R.id.role2);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);
            reason1 = itemView.findViewById(R.id.reason1);
            reason2 = itemView.findViewById(R.id.reason2);
            rec_reason_img1 =itemView.findViewById(R.id.rec_reason_img1);
            rec_reason_img2 = itemView.findViewById(R.id.rec_reason_img2);
        }

    }
}
