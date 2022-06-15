package com.xmut.shoppingcenter.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.ProductGridLayoutAdapter;
import com.xmut.shoppingcenter.adapter.ProductLinearLayoutAdapter;
import com.xmut.shoppingcenter.entity.PageInfo;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.utils.RecordsUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfterSearchActivity extends AppCompatActivity {
    boolean isSlidingToLast =false;
    boolean fbt =true;
    int pageSize = 20;
    int choice = 1;
    PageInfo<Product> pageInfo;
    int requestcount = 0 ;
    ImageView filter_1_icon,sort_icon,leave_bt;
    TextView filter_1_text,filter_2_text;
    List<Product> productList;
    String searchcontent;
    EditText editText;
    Button button;
    RecordsUtil recordsUtil =RecordsUtil.getInstance();
    RecyclerView rec_product;
    Context context;
    private ListView listView;
    private final String[] titles = new String[]{"综合","价格升序","价格降序"};
    RelativeLayout filterdiv1;
    Boolean isGridLayout =true;
    private RelativeLayout nullshow;
    private PopupWindow popupWindow;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.OnScrollListener gridLayoutListener,linearLayoutListener;
    ProductGridLayoutAdapter productGridLayoutAdapter;
    ProductLinearLayoutAdapter productLinearLayoutAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context =this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_search);
        init();
    }
    public void init(){
        nullshow = findViewById(R.id.nullshow);
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchcontent = (String) getIntent().getSerializableExtra("searchcontent");
        productList = getProductLists(choice,++requestcount,pageSize,UserManage.trim(searchcontent) );
        pageInfo = getPageInfo(requestcount,pageSize,UserManage.trim(searchcontent));
        if(productList.isEmpty()){
            nullshow.setVisibility(View.VISIBLE);
        }else{
            nullshow.setVisibility(View.GONE);
        }
        productGridLayoutAdapter = new ProductGridLayoutAdapter(context,productList);
        productLinearLayoutAdapter = new ProductLinearLayoutAdapter(context,productList);
        rec_product = findViewById(R.id.rec_product);
        filter_1_text = findViewById(R.id.filter_1_text);
        filter_2_text = findViewById(R.id.filter_2_text);

        filter_2_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resort(4);
                fbt =false;
            }
        });

        filter_1_icon = findViewById(R.id.filter_1_icon);
//        filter_3_icon = findViewById(R.id.filter_3_icon);
        sort_icon = findViewById(R.id.sort_icon);
        sort_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGridLayout)
                {
                    sort_icon.setImageResource(R.drawable.ic_grid);
                    rec_product.setLayoutManager(gridLayoutManager);
                    rec_product.setAdapter(productGridLayoutAdapter);
                    rec_product.removeOnScrollListener(linearLayoutListener);
                    rec_product.addOnScrollListener(gridLayoutListener);



                }else
                {
                    sort_icon.setImageResource(R.drawable.ic_list);
                    rec_product.setAdapter(productLinearLayoutAdapter);
                    rec_product.setLayoutManager(linearLayoutManager);
                    rec_product.removeOnScrollListener(gridLayoutListener);
                    rec_product.addOnScrollListener(linearLayoutListener);

                }
                isGridLayout=!isGridLayout;
            }
        });
        setColor(1);
        editText = findViewById(R.id.edit_box);
        editText.setText(searchcontent);
        editText.setSelection(searchcontent.length());

        button =findViewById(R.id.search_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempname = UserManage.trim(editText.getText().toString());
                if(!UserManage.isEmpty(tempname)){
                    requestcount=0;
                    System.out.println(tempname );
                    productList =getProductLists(choice,++requestcount,pageSize,tempname);
                    pageInfo = getPageInfo(requestcount,pageSize,tempname);
                    productGridLayoutAdapter.setProductList(productList);
                    productLinearLayoutAdapter.setProductList(productList);
                    if(productList.isEmpty()){
                        nullshow.setVisibility(View.VISIBLE);
                    }else{
                        nullshow.setVisibility(View.GONE);
                    }
                }else
                {
                    Toast.makeText(context, "搜索内容为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        filterdiv1 = findViewById(R.id.filter_1);
        filterdiv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow1();
                filter_1_icon.setImageResource(R.drawable.ic_arrow_up);
                if(fbt){
                    setColor(1);
                }else{
                    setColor(2);
                }
            }


        });

        gridLayoutManager = new GridLayoutManager(context,2);
        linearLayoutManager =new LinearLayoutManager(context,RecyclerView.VERTICAL,false);

        //用于GridLayout
         gridLayoutListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                /*
                SCROLL_STATE_FLING，这个参数表示你手离开后ListView还在“飞”中
                SCROLL_STATE_IDLE，这个参数表示ListView停下不动了
                SCROLL_STATE_TOUCH_SCROLL，这个参数表示你手还在ListView上
                 */
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向下滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        if(pageInfo.getPages()>requestcount){
                            productList.addAll(getProductLists(choice,++requestcount,pageSize,UserManage.trim(searchcontent) )) ;
                            productGridLayoutAdapter.notifyDataSetChanged();
                            productLinearLayoutAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, "暂无更多内容", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
             @Override
             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                 if (dy > 0) {
                     //大于0表示正在向下滚动
                     isSlidingToLast = true;
                 } else {
                     //小于等于0表示停止或向上滚动
                     isSlidingToLast = false;
                 }
             }
         };
         linearLayoutListener =new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);

                 LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                /*
                SCROLL_STATE_FLING，这个参数表示你手离开后ListView还在“飞”中
                SCROLL_STATE_IDLE，这个参数表示ListView停下不动了
                SCROLL_STATE_TOUCH_SCROLL，这个参数表示你手还在ListView上
                 */
                 if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                     //获取最后一个完全显示的ItemPosition
                     int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                     int totalItemCount = manager.getItemCount();

                     // 判断是否滚动到底部，并且是向下滚动
                     if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast ) {
                         if(pageInfo.getPages()>requestcount){
                             productList.addAll(getProductLists(choice,++requestcount,pageSize,UserManage.trim(searchcontent) )) ;
                             productGridLayoutAdapter.notifyDataSetChanged();
                             productLinearLayoutAdapter.notifyDataSetChanged();
                         }else{
                             Toast.makeText(context, "暂无更多内容", Toast.LENGTH_SHORT).show();
                         }

                     }
                 }
             }
             @Override
             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                 if (dy > 0) {
                     //大于0表示正在向下滚动
                     isSlidingToLast = true;
                 } else {
                     //小于等于0表示停止或向上滚动
                     isSlidingToLast = false;
                 }
             }
         };

        rec_product.setAdapter(productGridLayoutAdapter);
        rec_product.setLayoutManager(gridLayoutManager);
    }

    private List<Product> getProductLists(int choice,int pageNum,int pageSize,String searchname){
        recordsUtil.saveRecords(context,searchname);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("pageNum",String.valueOf(pageNum));
        paramsMap.put("pageSize",String.valueOf(pageSize));
        paramsMap.put("name",searchname);
        Result result = null;
        switch (choice){
            case 1: //权重
                result= DatabaseUtil.postParamters(paramsMap,"product","querybyweight");
                break;
            case 2: //价格升序
                result= DatabaseUtil.postParamters(paramsMap,"product","querybypriceasc");
                break;
            case 3: //价格降序
                result= DatabaseUtil.postParamters(paramsMap,"product","querybypricedesc");
                break;
            case 4: //销量降序
                result= DatabaseUtil.postParamters(paramsMap,"product","querybysales");
                break;
        }
        if(result.getCode()!=200)
            return null;
        return DatabaseUtil.getObjectList2(result,Product[].class);
    }

    private PageInfo<Product> getPageInfo(int pageNum,int pageSize,String searchname){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("pageNum",String.valueOf(pageNum));
        paramsMap.put("pageSize",String.valueOf(pageSize));
        paramsMap.put("name",searchname);
        Result result= DatabaseUtil.postParamters(paramsMap,"product","querypage");
        return DatabaseUtil.getEntity(result,PageInfo.class);
    }

    private void showPopupWindow1() {
        View contentview = LayoutInflater.from(this).inflate(R.layout.popupwindow,null);
        listView = contentview.findViewById(R.id.popwindowlistview);
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, titles[position], Toast.LENGTH_SHORT).show();
                filter_1_text.setText(titles[position]);
                switch (position+1){
                    case 1:
                        //综合 按权重排
                        resort(1);
                        fbt =true;
                        break;
                    case 2:
                        //价格升序
                        fbt =true;
                        resort(2);
                        break;
                    case 3://价格降序
                        fbt =true;
                        resort(3);
                        break;

                }


                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                filter_1_icon.setImageResource(R.drawable.ic_arrow_down);
                if(fbt)
                    setColor(1);
            }
        });
        popupWindow.showAsDropDown(filterdiv1);
    }
    public void setColor(int num){ //综合和销量  选中后的按钮颜色变化
        switch (num){
            case 1:
                filter_1_text.setTextColor(Color.parseColor("#F1824E"));
                filter_2_text.setTextColor(Color.parseColor("#B0000000"));
                filter_1_icon.getDrawable().setTint(Color.parseColor("#F1824E"));
                break;
            case 2:
                filter_1_text.setTextColor(Color.parseColor("#B0000000"));
                filter_2_text.setTextColor(Color.parseColor("#F1824E"));
                filter_1_icon.getDrawable().setTint(Color.parseColor("#C0C0C0"));
                break;

        }
    }
    public void resort(int num){
        requestcount =0;
        switch (num){
            case 1: //综合 按权重排
                setColor(1);
                break;
            case 2:  //价格升序
                setColor(1);
                break;
            case 3://价格降序
                setColor(1);
                break;
            case 4://销量降序
                setColor(2);
                break;

        }

        productList = getProductLists(num,++requestcount,pageSize,UserManage.trim(searchcontent) ) ;
        productGridLayoutAdapter.setProductList(productList);
        productLinearLayoutAdapter.setProductList(productList);

    }

}