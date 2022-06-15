package com.xmut.shoppingcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Coupon;
import com.xmut.shoppingcenter.utils.TimeUtil;

import java.util.List;

public class CheckCouponAdatper extends RecyclerView.Adapter<CheckCouponAdatper.Myholder> {
    Context context;
    List<Coupon> couponList;
    List<String> time = TimeUtil.getInstance().getFormatTime();


    public CheckCouponAdatper(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_coupon,null);
        return new Myholder(view);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.coupon_name.setText(couponList.get(position).getName() );
        if(couponList.get(position).getRequirement()!=0){
            holder.requirement.setText("满"+couponList.get(position).getRequirement()+"元可用");
        }else{
            holder.requirement.setText("无门槛");
        }
        holder.value.setText(String.valueOf(couponList.get(position).getValue() ));

        if(couponList.get(position).getStarttime().compareTo(time.get(1) ) <=0 && couponList.get(position).getEndtime().compareTo(time.get(1) ) >=0 ){
            holder.state.setText("可使用");
        }else{
            holder.state.setText("已过期");
        }
//        //当 最晚的日期 小于当前日期 即 最晚的日期 - 当前日期 <0 已过期
//        if( couponList.get(position).getEndtime().compareTo(time.get(0) ) <0 ){
//            holder.state.setText("已过期");
//            Toast.makeText(context, "无法使用该优惠券", Toast.LENGTH_SHORT).show();
//        }else{ //还未过期
//
//            //当 开始日期 大于当前日期 即 开始日期 - 当前日期 >0 未到使用时间 无法使用
//            if( couponList.get(position).getStarttime().compareTo(time.get(0) )>0 ){
//                holder.state.setText("无法使用");
//                Toast.makeText(context, "无法使用该优惠券", Toast.LENGTH_SHORT).show();
//            }else{
//                //当 开始日期 小于当前日期 即 开始日期 - 当前日期 < 0 可以选择 根据金额判断使用
//                if(price > couponList.get(position).getRequirement() ){
//                    holder.state.setText("可使用");
//                    holder.coupon_div.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            firstListener.onSelect(holder.getAdapterPosition() );
//                        }
//                    });
//                }else{
//                    holder.state.setText("无法使用");
//                    Toast.makeText(context, "无法使用该优惠券", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }
        holder.starttime.setText(TimeUtil.getInstance().FormatDayTime( couponList.get(position).getStarttime() ));
        holder.endtime.setText(TimeUtil.getInstance().FormatDayTime( couponList.get(position).getEndtime() ));




    }
    public interface FirstListener {

        void onSelect(int pos);
    }
    private FirstListener firstListener;

    public FirstListener getFirstListener() {
        return firstListener;
    }

    public void setFirstListener(FirstListener firstListener) {
        this.firstListener = firstListener;
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView coupon_name,value,requirement,starttime,endtime,state;
        RelativeLayout coupon_div;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            coupon_div = itemView.findViewById(R.id.coupon_div);
            coupon_name = itemView.findViewById(R.id.coupon_name);
            requirement= itemView.findViewById(R.id.requirement);
            value = itemView.findViewById(R.id.value);
            starttime = itemView.findViewById(R.id.time1);
            endtime = itemView.findViewById(R.id.time2);
            state = itemView.findViewById(R.id.state);

        }
    }
}
