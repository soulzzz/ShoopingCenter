package com.xmut.shoppingcenter.fragments;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;
import com.suke.widget.SwitchButton;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.ApplyForStoreActivity;
import com.xmut.shoppingcenter.activities.CheckCollectionActivity;
import com.xmut.shoppingcenter.activities.CheckCouponActivity;
import com.xmut.shoppingcenter.activities.CheckFootActivity;
import com.xmut.shoppingcenter.activities.CheckOrderActivity;
import com.xmut.shoppingcenter.activities.CheckProductActivity;
import com.xmut.shoppingcenter.activities.CheckSubscribeActivity;
import com.xmut.shoppingcenter.activities.ChooseAvatarActivity;
import com.xmut.shoppingcenter.activities.ChooseCouponActivity;
import com.xmut.shoppingcenter.activities.LoginActivity;
import com.xmut.shoppingcenter.activities.MainActivity;
import com.xmut.shoppingcenter.activities.PersonDetailActivity;
import com.xmut.shoppingcenter.adapter.CheckCollectionAdapter;
import com.xmut.shoppingcenter.entity.Order;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.User;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalityFragment extends Fragment {
    private View view;
    private Context context;
    private User user;
    RadiuImageView profile_img;
    RelativeLayout iconsection;
    TextView profile_username,allorder_goto,coupon,waitpay,waitsend,waitreceive,waitcomment,refund,foot,collect,subscribe,settings;
    ImageView person_goto;
    PopupLayout popupLayout;
    public static boolean shouldRefresh = false;
    TextView waitpay_num,waitsend_num,waitreceive_num,waitcomment_num,refund_num;
    int waitpay_num_count = 0,waitsend_num_count = 0,waitreceive_num_count = 0,waitcomment_num_count = 0,refund_num_count = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.fragment_personality,container, false);
        user = UserManage.getInstance().getUserInfo(context);
        initData();
        init();
        return view;
    }

    private void initData() {
        waitpay_num_count = waitsend_num_count =waitreceive_num_count =waitcomment_num_count =refund_num_count = 0;
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("user_id",String.valueOf(user.getId()));
        Result result = DatabaseUtil.postParamters(paramsMap,"order","list","byuserid");
        if(result.getCode()!=200){

        }else{
            List<Order> orders = DatabaseUtil.getObjectList(result,Order.class);
            //得出每个订单中每个商品的状态 同个订单中多个商品同一状态则算为一单 多个商品不同状态则另算
            for(Order order:orders){
                int state = -1;
                for(Product product :order.getOrderProducts()){
                    if(state !=product.getOrder_state()){
                        state = product.getOrder_state();
                        switch (state){
                            case 1: //待付款
                                waitpay_num_count++;
                                break;
                            case 2:  //待发货
                                waitsend_num_count++;
                                break;
                            case 3:
                                waitreceive_num_count++;
                                break;
                            case 4:
                                waitcomment_num_count++;
                                break;
                            default:
                                break;
                        }
                    }

                }
            }


        }
    }


    public void init(){
        iconsection = view.findViewById(R.id.iconsection);
        iconsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, PersonDetailActivity.class);
            }
        });
        waitpay_num = view.findViewById(R.id.waitpay_num);
        waitsend_num =view.findViewById(R.id.waitsend_num);
        waitreceive_num=  view.findViewById(R.id.waitreceive_num);
        waitcomment_num = view.findViewById(R.id.waitcomment_num);
        refund_num = view.findViewById(R.id.refund_num);
        if(waitpay_num_count> 0){
            waitpay_num.setText(String.valueOf(waitpay_num_count));
            waitpay_num.setVisibility(View.VISIBLE);
        }
        if(waitsend_num_count> 0){
            waitsend_num.setText(String.valueOf(waitsend_num_count));
            waitsend_num.setVisibility(View.VISIBLE);
        }
        if(waitreceive_num_count> 0){
            waitreceive_num.setText(String.valueOf(waitreceive_num_count));
            waitreceive_num.setVisibility(View.VISIBLE);
        }
        if(waitcomment_num_count> 0){
            waitcomment_num.setText(String.valueOf(waitcomment_num_count));
            waitcomment_num.setVisibility(View.VISIBLE);
        }

        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=View.inflate(context, R.layout.popup_settings,null);
                popupLayout= PopupLayout.init(context,view);
                ImageView leave_bt = view.findViewById(R.id.leave_bt);
                RelativeLayout line2 = view.findViewById(R.id.line2);
                line2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserManage.getInstance().Jump((Activity) context, ApplyForStoreActivity.class);
                    }

                });
                if(UserManage.getInstance().getUserInfo(context).getUserpermission()>=2){
                    line2.setVisibility(View.GONE);

                }

                leave_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupLayout.dismiss();
                    }
                });
                SwitchButton switchButton = view.findViewById(R.id.switch_button);
                if(UserManage.getInstance().getShouldRecommend(context))
                    switchButton.setChecked(true);
                switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        UserManage.getInstance().setShouldRecommend(context,isChecked);
                    }
                });

                TextView logout = view.findViewById(R.id.logout);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserManage.getInstance().clearUserInfo(context);
                        UserManage.getInstance().FinishJump((Activity) context, LoginActivity.class);
                        MainActivity.instance.finish();
                    }
                });
//                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
//                popupLayout.setWidth(d.getWidth(),false);
                popupLayout.show(PopupLayout.POSITION_LEFT);
            }
        });
        collect =view.findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckCollectionActivity.class);
            }
        });
        subscribe =view.findViewById(R.id.subscribe);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckSubscribeActivity.class);
            }
        });
        foot = view.findViewById(R.id.foot);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, CheckFootActivity.class);
            }
        });
        waitpay = view.findViewById(R.id.waitpay);
        waitsend = view.findViewById(R.id.waitsend);
        waitreceive = view.findViewById(R.id.waitreceive);
        waitcomment = view.findViewById(R.id.waitcomment);
        refund =view.findViewById(R.id.refund);
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"5","choice");
            }
        });
        waitpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"1","choice");
            }
        });
        waitsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"2","choice");
            }
        });
        waitreceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"3","choice");
            }
        });
        waitcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"4","choice");
            }
        });

        coupon=  view.findViewById(R.id.coupon);
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(getActivity(), CheckCouponActivity.class);
            }
        });
        allorder_goto = view.findViewById(R.id.allorder_goto);
        allorder_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity) context, CheckOrderActivity.class,"0","choice");
            }
        });

        profile_img = view.findViewById(R.id.profile_img);
        profile_username = view.findViewById(R.id.profile_username);
        profile_username.setText(user.getUsername() );

        person_goto = view.findViewById(R.id.person_goto);
        person_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump((Activity)context, PersonDetailActivity.class);
//                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//
//                    Intent data = result.getData();
//                    int resultCode = result.getResultCode();
//                    if(resultCode ==imgrequestCode){
//
//                    }
//
//                }).launch(new Intent((Activity)context, PersonDetailActivity.class));
//            }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(context).load(UserManage.getInstance().getUserInfo(context).getAvatar()).placeholder(R.drawable.originavatar).into(profile_img);
        if(shouldRefresh){
            System.out.println(shouldRefresh);
            shouldRefresh =false;
            waitpay_num_count = waitsend_num_count =waitreceive_num_count =waitcomment_num_count =refund_num_count = 0;
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("user_id",String.valueOf(user.getId()));
            Result result = DatabaseUtil.postParamters(paramsMap,"order","list","byuserid");
            if(result.getCode()!=200){

            }else{
                List<Order> orders = DatabaseUtil.getObjectList(result,Order.class);
                //得出每个订单中每个商品的状态 同个订单中多个商品同一状态则算为一单 多个商品不同状态则另算
                for(Order order:orders){
                    int state = -1;
                    for(Product product :order.getOrderProducts()){
                        if(state !=product.getOrder_state()){
                            state = product.getOrder_state();
                            switch (state){
                                case 1: //待付款
                                    waitpay_num_count++;
                                    break;
                                case 2:  //待发货
                                    waitsend_num_count++;
                                    break;
                                case 3:
                                    waitreceive_num_count++;
                                    break;
                                case 4:
                                    waitcomment_num_count++;
                                    break;
                                default:
                                    break;
                            }
                        }

                    }
                }
                if(waitpay_num_count> 0){
                    waitpay_num.setText(String.valueOf(waitpay_num_count));
                    waitpay_num.setVisibility(View.VISIBLE);
                }else {
                    waitpay_num.setVisibility(View.GONE);
                }
                if(waitsend_num_count> 0){
                    waitsend_num.setText(String.valueOf(waitsend_num_count));
                    waitsend_num.setVisibility(View.VISIBLE);
                }else {
                    waitsend_num.setVisibility(View.GONE);
                }

                if(waitreceive_num_count> 0){
                    waitreceive_num.setText(String.valueOf(waitreceive_num_count));
                    waitreceive_num.setVisibility(View.VISIBLE);
                }else {
                    waitreceive_num.setVisibility(View.GONE);
                }
                if(waitcomment_num_count> 0){
                    waitcomment_num.setText(String.valueOf(waitcomment_num_count));
                    waitcomment_num.setVisibility(View.VISIBLE);
                }else {
                    waitcomment_num.setVisibility(View.GONE);
                }


            }
        }
    }
}