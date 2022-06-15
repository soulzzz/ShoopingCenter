package com.xmut.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.CheckNoticeActivity;
import com.xmut.shoppingcenter.entity.Notice;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Myholder>{
    private List<Notice> notices;
    private Context context;

    public NoticeAdapter(Context context,List<Notice> notices) {
        this.context = context;
        this.notices = notices;

    }

    public NoticeAdapter(Context context) {
        this.context = context;
    }

    public void setNotices(List<Notice> notices) {
        this.notices =notices;
        notifyDataSetChanged();
    }


    @NonNull
    @Override

    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_notice,null);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(true);//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        holder.title.setText(notices.get(position).getTitle());
        holder.message.setText(notices.get(position).getMessage());
        if(notices.get(position).getRead_state() !=0 ){
            holder.red_state.setVisibility(View.GONE);
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
        holder.click_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckNoticeActivity.class,notices.get(holder.getAdapterPosition() ),"notice");
                mOnSwipeListener.onClick(holder.getAdapterPosition());
            }
        });

        holder.time.setText(TimeUtil.getInstance().FormatHourTime(notices.get(holder.getAdapterPosition()).getSend_time() ));


    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public interface onSwipeListener {
        void onDel(int pos);

        void onClick(int pos);

    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    static class Myholder extends RecyclerView.ViewHolder{
        Button btnDelete;
        TextView title;
        TextView message;
        TextView red_state;
        TextView time;
        RelativeLayout click_bar;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            red_state = itemView.findViewById(R.id.notice_red);
            time = itemView.findViewById(R.id.time);
            click_bar = itemView.findViewById(R.id.click_bar);
        }
    }
}
