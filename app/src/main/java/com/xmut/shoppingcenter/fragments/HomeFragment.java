package com.xmut.shoppingcenter.fragments;

import android.content.Context;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.activities.SearchActivity;
import com.xmut.shoppingcenter.adapter.ImageAdatper;
import com.xmut.shoppingcenter.adapter.ProductGridLayoutAdapter;
import com.xmut.shoppingcenter.adapter.SearchrecordsAdatper;
import com.xmut.shoppingcenter.entity.Ad;
import com.xmut.shoppingcenter.entity.PageInfo;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.RecordsUtil;
import com.xmut.shoppingcenter.utils.UserManage;
import com.xmut.shoppingcenter.utils.fragmentUtil.LazyLoadFragment;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class HomeFragment extends LazyLoadFragment {
    boolean isLast = false,isLastSwitch =false;
    int requestcount=0;
    int pageSize = 2;
    int choice = 1;
    PageInfo<Product> pageInfo;
    int curRecordPos = 0;
    List<Product> productList = new ArrayList<>();;
    String searchcontent;
    RecyclerView.OnScrollListener gridLayoutListener;
    ProductGridLayoutAdapter productGridLayoutAdapter;
    private View view;
    private Context context;
    EditText editText;
    Button button;
    RecyclerView recyclerView;
    RefreshLayout refreshLayout;
    Banner ad_show;
    List<Ad> ads;
    List<String> ad_list;
    List<String> product_id_list;
    List<String> records;
    List<Integer> eachRecordSearchIndexState ;
    List<PageInfo> pageInfoList;
    int alreadyMaxSearch = 0;
    RecordsUtil recordsUtil =RecordsUtil.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        view = inflater.inflate(R.layout.fragment_home,container, false);
        init();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){}
    }
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    @Override
    public void onLazyLoad() {
        alreadyMaxSearch=  0;
        records = RecordsUtil.getInstance().getRecords(context);
        eachRecordSearchIndexState =new ArrayList<>();
        pageInfoList = new ArrayList<>();
        //记录不为空 获取各pageInfo
        if(!records.isEmpty()){
            for(String tempname :records){
                eachRecordSearchIndexState.add(1);
                pageInfoList.add(getPageInfo(1,pageSize,tempname));
            }
        }else{
            //记录为空 添加
            productList.addAll(getProductLists2() );
            productList = removeDuplicateObject(productList);
            productGridLayoutAdapter.setProductList(productList);
            isLast = true;
        }
       for(; curRecordPos<eachRecordSearchIndexState.size()&& !isLast; curRecordPos++){
           if(  eachRecordSearchIndexState.get(curRecordPos)<=pageInfoList.get(curRecordPos).getPages()){
               searchcontent= records.get(curRecordPos);
               productList.addAll(getProductLists(choice,eachRecordSearchIndexState.get(curRecordPos),pageSize,searchcontent) );
               eachRecordSearchIndexState.set(curRecordPos,eachRecordSearchIndexState.get(curRecordPos)+1);
               productList = removeDuplicateObject(productList);
               productGridLayoutAdapter.setProductList(productList);

               if(eachRecordSearchIndexState.get(curRecordPos)> pageInfoList.get(curRecordPos).getPages()){

                   alreadyMaxSearch++;
                   System.out.println(alreadyMaxSearch+"个条件满足了");
               }



           }

           if(curRecordPos ==eachRecordSearchIndexState.size()-1 ){
               System.out.println("编里一遍后");
               curRecordPos =-1;
               //循环一遍
               if(productList.size()<4){
                   System.out.println("不够继续便利");
               }
               else{
                   System.out.println("够了退出");
                   break;
               }

           }
           if(alreadyMaxSearch ==records.size()){
               System.out.println("以结束");
               isLast =true;
               break;
           }
       }
//        for(String tempname :records){
//            curRecordPos++;
//            if(!UserManage.isEmpty(tempname)){
//                pageInfo = getPageInfo(requestcount,pageSize,tempname);
//                do {
//
//                    searchcontent= tempname;
//                    productList.addAll(getProductLists(choice,++requestcount,pageSize,tempname) );
//                    productList = removeDuplicateObject(productList);
//
//                    productGridLayoutAdapter.setProductList(productList);
//                    if(requestcount>pageInfo.getPages())
//                }while (productList.size()<4);
//                System.out.println("当前位置"+curRecordPos +"搜索内容"+tempname);
//
//                if(productList.size()>6)
//                break;
//            }
//          }
//        //没有任何搜索记录或者 全部搜索了搜索记录且产品信息<6
//        if(records.isEmpty() ||productList.size()<4 ){
//            productList.addAll(getProductLists2() );
//            productList = removeDuplicateObject(productList);
//            productGridLayoutAdapter.setProductList(productList);
//        }

    }

    private void init() {

         refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLast =false;
                isLastSwitch =false;
                alreadyMaxSearch=  0;
                curRecordPos =0;
                records = RecordsUtil.getInstance().getRecords(context);
                eachRecordSearchIndexState =new ArrayList<>();
                pageInfoList = new ArrayList<>();
                productList.clear();
                //记录不为空 获取各pageInfo
                if(!records.isEmpty()){
                    for(String tempname :records){
                        eachRecordSearchIndexState.add(1);
                        pageInfoList.add(getPageInfo(1,pageSize,tempname));
                    }
                }else{
                    //记录为空 添加
                    System.out.println("我进了这里 ");
                    productList.addAll(getProductLists2() );
                    productList = removeDuplicateObject(productList);
                    productGridLayoutAdapter.setProductList(productList);
                    isLast = true;
                }
                for(; curRecordPos<eachRecordSearchIndexState.size()&& !isLast; curRecordPos++){
                    System.out.println("我进了这里A "+curRecordPos);
                     if(  eachRecordSearchIndexState.get(curRecordPos)<=pageInfoList.get(curRecordPos).getPages()){
                        System.out.println("我进了这里B "+curRecordPos);
                        searchcontent= records.get(curRecordPos);
                        productList.addAll(getProductLists(choice,eachRecordSearchIndexState.get(curRecordPos),pageSize,searchcontent) );
                        eachRecordSearchIndexState.set(curRecordPos,eachRecordSearchIndexState.get(curRecordPos)+1);
                        productList = removeDuplicateObject(productList);
                        productGridLayoutAdapter.setProductList(productList);

                        if(eachRecordSearchIndexState.get(curRecordPos)> pageInfoList.get(curRecordPos).getPages()){

                            alreadyMaxSearch++;
                            System.out.println(alreadyMaxSearch+"个条件满足了");
                        }



                    }

                    if(curRecordPos ==eachRecordSearchIndexState.size()-1 ){
                        System.out.println("我进了这里C "+curRecordPos);
                        curRecordPos =-1;
                        //循环一遍
                        if(productList.size()<4){
                            System.out.println("我进了这里D "+curRecordPos);
                        }
                        else{
                            System.out.println("我进了这里E "+curRecordPos);
                            break;
                        }

                    }
                    if(alreadyMaxSearch ==records.size()){
                        System.out.println("我进了这里F "+curRecordPos);
                        isLast =true;
                        break;
                    }
                }
                System.out.println("推出了 "+curRecordPos);

                refreshlayout.finishRefresh(1500/*,false*/);//传入false表示刷新失败


            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        product_id_list =new ArrayList<>();
        ad_list = new ArrayList<>();
        Result result = DatabaseUtil.selectList("ad","list");
        if(result.getCode()!=200){
            ad_list.add("http://soulzzz.top:8082/img/test1.jpg");
            ad_list.add("http://soulzzz.top:8082/img/test2.jpg");
            ad_list.add("http://soulzzz.top:8082/img/test3.jpg");
        }else{
            ads = DatabaseUtil.getObjectList(result,Ad.class);
            for(Ad ad:ads){
                ad_list.add(ad.getAd_img());
                product_id_list.add(ad.getProduct_id());
            }

        }

        ad_show = view.findViewById(R.id.ad_show);
        editText = view.findViewById(R.id.edit_box);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManage.getInstance().Jump(getActivity(), SearchActivity.class);
            }
        });
        ImageAdatper adatper = new ImageAdatper(ad_list,product_id_list,context);
        button = view.findViewById(R.id.search_bt);
        recyclerView = view.findViewById(R.id.recyclerview);
        ad_show.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(adatper)
                .setIndicator(new CircleIndicator(context));
        productGridLayoutAdapter = new ProductGridLayoutAdapter(context);
        productGridLayoutAdapter.setProductList(productList);

        gridLayoutListener = new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
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
                    int lastVisibleItem = manager.findLastVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向下滚动 或者初始就没有PageInfo信息
                    if(isLast && !isLastSwitch ){
                        if(productList.size()< getProductLists2().size() ){
                            productList.addAll(getProductLists2() );
                            productList = removeDuplicateObject(productList);
                            productGridLayoutAdapter.setProductList(productList);

                        }
                        isLastSwitch=true;
                        Toast.makeText(context, "已完成数据加载", Toast.LENGTH_SHORT).show();
                    }
                    if(isLastSwitch)
                        return;

                    if (   (lastVisibleItem == (totalItemCount - 1) || lastVisibleItem == totalItemCount  ) ) {
                        curRecordPos = curRecordPos ==-1?0:curRecordPos;
                        //并且是向下滚动 或者初始就没有PageInfo信息 没有则自动加东西
                        for(; curRecordPos<eachRecordSearchIndexState.size()&& !isLast; ++curRecordPos){
                            System.out.println("个数B"+curRecordPos);
                            if(  eachRecordSearchIndexState.get(curRecordPos)<=pageInfoList.get(curRecordPos).getPages()){
                                searchcontent= records.get(curRecordPos);
                                productList.addAll(getProductLists(choice,eachRecordSearchIndexState.get(curRecordPos),pageSize,searchcontent) );
                                eachRecordSearchIndexState.set(curRecordPos,eachRecordSearchIndexState.get(curRecordPos)+1);
                                productList = removeDuplicateObject(productList);
                                productGridLayoutAdapter.setProductList(productList);

                                if(eachRecordSearchIndexState.get(curRecordPos)> pageInfoList.get(curRecordPos).getPages()){

                                    alreadyMaxSearch++;
                                    System.out.println(alreadyMaxSearch+"个条件满足了");
                                }



                            }

                            if(curRecordPos == eachRecordSearchIndexState.size()-1 ){
                                System.out.println("编里一遍后");
                                curRecordPos =-1;
                                //循环一遍
                                System.out.println("个数A"+curRecordPos);

                            }
                            if(alreadyMaxSearch ==records.size()){
                                System.out.println("以结束");
                                isLast =true;
                                break;
                            }
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
        recyclerView.setAdapter(productGridLayoutAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.addOnScrollListener(gridLayoutListener);

    }
    private List<Product> getProductLists(int choice,int pageNum,int pageSize,String searchname){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("pageNum",String.valueOf(pageNum));
        paramsMap.put("pageSize",String.valueOf(pageSize));
        paramsMap.put("name",searchname);
        Result result = null;
        switch (choice){
            case 1: //权重+名字
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
            case 5: //only weight
                result= DatabaseUtil.postParamters(paramsMap,"product","queryonlybyweight");
                break;
        }
        if(result.getCode()!=200)
            return new ArrayList<Product>() {
            };
        return DatabaseUtil.getObjectList(result,Product.class);
    }

    private List<Product> getProductLists2(){
       Result result =DatabaseUtil.selectList(HttpAddress.product(),"list","bystate","1");
        if(result.getCode()!=200)
            return new ArrayList<Product>() {
            };
        return DatabaseUtil.getObjectList(result,Product.class);
    }

    private PageInfo<Product> getPageInfo(int pageNum,int pageSize,String searchname){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("pageNum",String.valueOf(pageNum));
        paramsMap.put("pageSize",String.valueOf(pageSize));
        paramsMap.put("name",searchname);
        Result result= DatabaseUtil.postParamters(paramsMap,"product","querypage");
        return DatabaseUtil.getEntity(result,PageInfo.class);
    }
    private List<Product> removeDuplicateObject(List<Product> products){
        return products.stream().filter(distinctByKey(Product::getProduct_id)).collect(Collectors.toList());
//        return  products.stream().collect(
//                Collectors.collectingAndThen(Collectors.toCollection(
//                        () -> new TreeSet<>(Comparator.comparing(Product::getProduct_id))), ArrayList::new)
//        );
//        return  new ArrayList<>(products.stream()
//                .collect(Collectors.toMap(Product::getProduct_id,
//                        Function.identity(), (oldValue, newValue) -> oldValue))
//                .values());
    }


}