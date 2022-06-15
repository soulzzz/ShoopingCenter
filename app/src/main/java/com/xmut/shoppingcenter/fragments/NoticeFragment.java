package com.xmut.shoppingcenter.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.LoginActivity;
import com.xmut.shoppingcenter.activities.MainActivity;
import com.xmut.shoppingcenter.adapter.NoticeAdapter;
import com.xmut.shoppingcenter.entity.Notice;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.List;


public class NoticeFragment extends Fragment {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private List<Notice> noticeList;
    private NoticeAdapter noticeAdapter;
    private LinearLayoutManager linearLayoutManager;
    RelativeLayout nullshow;
    public  static NoticeFragment instance;
    RefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        instance = this;
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_notice, container, false);

        initData();
        init();

        return view;
    }
    public  void shouldrefresh(){
        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.notice(), "list", UserManage.getInstance().getUserInfo(context).getId()));
        if (result.getCode() != 200) {
            Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            noticeList = DatabaseUtil.getObjectList(result, Notice.class);
            noticeAdapter.setNotices(noticeList);
        }
    }
    private void init() {
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                shouldrefresh();
                refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });

        nullshow =view.findViewById(R.id.nullshow);

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        noticeAdapter = new NoticeAdapter(context);
        noticeAdapter.setNotices(noticeList);
        noticeAdapter.setOnDelListener(new NoticeAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.notice(),"delete",noticeList.get(pos).getId()));
                if(result.getCode()!=200){

                }else{
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    noticeList.remove(pos);
                    noticeAdapter.notifyItemRemoved(pos);
                    MainActivity.instance.redrawNoticeIcon();
                }
            }

            @Override
            public void onClick(int pos) {
                if(noticeList.get(pos).getRead_state()!=1 ){
                    noticeList.get(pos).setRead_state(1);
                    Result result = DatabaseUtil.updateById(noticeList.get(pos),HttpAddress.get(HttpAddress.notice(),"update"));
                    if(result.getCode()!=200){

                    }else{
                        noticeAdapter.notifyItemChanged(pos);
                        MainActivity.instance.redrawNoticeIcon();
                    }
                }
            }

        });
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(noticeAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (noticeList == null) {
            Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.notice(), "list", UserManage.getInstance().getUserInfo(context).getId()));
            if (result.getCode() != 200) {
                Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
            } else {

                noticeList = DatabaseUtil.getObjectList(result, Notice.class);
                if(noticeList ==null)
                    noticeList = new ArrayList<>();
            }
        }
        if(noticeList.isEmpty())
            nullshow.setVisibility(View.VISIBLE);
        else{
            nullshow.setVisibility(View.GONE);
        }

    }

    public void initData() {

        if (noticeList == null) {
            Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.notice(), "list", UserManage.getInstance().getUserInfo(context).getId()));
            if (result.getCode() != 200) {
                Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
            } else {

                noticeList = DatabaseUtil.getObjectList(result, Notice.class);
                if(noticeList ==null)
                    noticeList = new ArrayList<>();
            }
        }
    }
}