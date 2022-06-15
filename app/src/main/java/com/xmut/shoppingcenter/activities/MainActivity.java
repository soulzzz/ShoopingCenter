package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingending.popuplayout.PopupLayout;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.entity.Notice;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.okhttp.http.OKHttpUtil;
import com.xmut.shoppingcenter.utils.ImageUtil.RadiuImageView;
import com.xmut.shoppingcenter.utils.UserManage;
import com.xmut.shoppingcenter.utils.fragmentUtil.MyFragmentPageAdatper;
import com.xmut.shoppingcenter.utils.fragmentUtil.MyViewPager;
import com.xmut.shoppingcenter.fragments.HomeFragment;
import com.xmut.shoppingcenter.fragments.NoticeFragment;
import com.xmut.shoppingcenter.fragments.PersonalityFragment;
import com.xmut.shoppingcenter.fragments.ShoppingCarFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private LinearLayout tab_menu_1;
    private TextView tab_menu_text1;
    private TextView tab_menu_num1;

    private LinearLayout tab_menu_2;
    private TextView tab_menu_text2;
    private TextView tab_menu_num2;

    private LinearLayout tab_menu_3;
    private TextView tab_menu_text3;
    private TextView tab_menu_num3;

    private LinearLayout tab_menu_4;
    private TextView tab_menu_text4;
    private TextView tab_menu_num4;
    public static MainActivity instance;
    private MyViewPager vp;
    private Handler handler = null;
    Timer timer;
    int c = 0;
    Runnable runnable;
    MyFragmentPageAdatper adapter;
    FragmentManager fragmentManager;

    public void redrawNoticeIcon() {
        c = 0;
        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.notice(), "list", UserManage.getInstance().getUserInfo(MainActivity.this).getId()));
        if (result.getCode() != 200) {
            Toast.makeText(instance, result.getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            TextView text = findViewById(R.id.tab_menu_2_num);
            List<Notice> temp = DatabaseUtil.getObjectList(result, Notice.class);
            if (temp.isEmpty()) {

                return;
            } else {

                for (Notice notice : temp)
                    if (notice.getRead_state() == 0)
                        c++;
                handler.post(runnable);
            }


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("我被销毁了");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 仿返回键退出界面,但不销毁，程序仍在后台运行
            moveTaskToBack(false); // 关键的一行代码
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    public void getNoticesByMinute(int minute) {

        new Thread(() -> {
            try {
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                redrawNoticeIcon();
                            }
                        }, 0,minute * 60 * 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        //修改UI的
         runnable = new Runnable() {
            @Override
            public void run() {
                TextView temp = findViewById(R.id.tab_menu_2_num);
                if (c == 0) {
                    temp.setVisibility(View.GONE);
                } else {
                    temp.setText(String.valueOf(c));
                    temp.setVisibility(View.VISIBLE);
                    if(NoticeFragment.instance!=null)
                    NoticeFragment.instance.shouldrefresh();
                }
            }
        };
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        instance = this;

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new ShoppingCarFragment());
        fragments.add(new PersonalityFragment());
        fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentPageAdatper(fragmentManager, fragments);

        //设定适配器
        vp = (MyViewPager) findViewById(R.id.viewpager);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(this);
        init();

//        Map<String,String> paramsMap=new HashMap<>();
//        paramsMap.put("subject","1");
//        paramsMap.put("model","c1");
//        paramsMap.put("testType","order");
//        paramsMap.put("key","fe4307c2e450cfd12f05a1d5fb62af76");
//        System.out.println( OKHttpUtil.postparamter("http://v.juhe.cn/jztk",paramsMap,"query"));
    }

    private void init() {
        tab_menu_1 = findViewById(R.id.tab_menu_1);
        tab_menu_text1 = findViewById(R.id.tab_menu_1_text);
        tab_menu_num1 = findViewById(R.id.tab_menu_1_num);

        tab_menu_2 = findViewById(R.id.tab_menu_2);
        tab_menu_text2 = findViewById(R.id.tab_menu_2_text);
        tab_menu_num2 = findViewById(R.id.tab_menu_2_num);

        tab_menu_3 = findViewById(R.id.tab_menu_3);
        tab_menu_text3 = findViewById(R.id.tab_menu_3_text);
        tab_menu_num3 = findViewById(R.id.tab_menu_3_num);

        tab_menu_4 = findViewById(R.id.tab_menu_4);
        tab_menu_text4 = findViewById(R.id.tab_menu_4_text);
        tab_menu_num4 = findViewById(R.id.tab_menu_4_num);


        tab_menu_1.setOnClickListener(this);
        tab_menu_2.setOnClickListener(this);
        tab_menu_3.setOnClickListener(this);
        tab_menu_4.setOnClickListener(this);

        setSelected(1);
        getNoticesByMinute(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Toast.makeText(this, "NOW:"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageSelected(int position) {
//        Toast.makeText(this, "NOW1:"+position+1, Toast.LENGTH_SHORT).show();
        setSelected(position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_menu_1:
                //topb_bar_text.setText(tab_menu_text1.getText().toString());
                vp.setCurrentItem(0);
                break;
            case R.id.tab_menu_2:
                //topb_bar_text.setText(tab_menu_text2.getText().toString());
                vp.setCurrentItem(1);
                break;
            case R.id.tab_menu_3:
                //topb_bar_text.setText(tab_menu_text3.getText().toString());
                vp.setCurrentItem(2);
                break;
            case R.id.tab_menu_4:
                //topb_bar_text.setText(tab_menu_text3.getText().toString());
                vp.setCurrentItem(3);
                break;
        }
    }

    private void setSelected(int cur) {
        tab_menu_text1.setSelected(false);
        tab_menu_text2.setSelected(false);
        tab_menu_text3.setSelected(false);
        tab_menu_text4.setSelected(false);
        tab_menu_1.setSelected(false);
        tab_menu_2.setSelected(false);
        tab_menu_3.setSelected(false);
        tab_menu_4.setSelected(false);
        tab_menu_num1.setVisibility(View.INVISIBLE);
//        tab_menu_num2.setVisibility(View.INVISIBLE);
        tab_menu_num3.setVisibility(View.INVISIBLE);
        tab_menu_num4.setVisibility(View.INVISIBLE);

        switch (cur) {
            case 1:
                tab_menu_text1.setSelected(true);
                break;
            case 2:
                tab_menu_text2.setSelected(true);
                break;
            case 3:
                tab_menu_text3.setSelected(true);
                break;
            case 4:
                tab_menu_text4.setSelected(true);
                break;
        }


    }
}