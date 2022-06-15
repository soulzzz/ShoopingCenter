package com.xmut.shoppingcenter.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.GridImageAdapter;
import com.xmut.shoppingcenter.entity.Product;
import com.xmut.shoppingcenter.entity.ProductComment;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.utils.AvatarUtil;
import com.xmut.shoppingcenter.utils.FlowLayoutManager;
import com.xmut.shoppingcenter.utils.OrderUtil;
import com.xmut.shoppingcenter.utils.TimeUtil;
import com.xmut.shoppingcenter.utils.UserManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommentProductFragment extends Fragment {
    private View view;
    private ImageView product_icon;
    private TextView product_name,uploadImg,submit;
    private EditText comment;
    private RatingBar ratingbar;
    private RecyclerView rec_img;
    private Product product;
    private int curindex = -1;
    private List<Uri> myresult;
    private String imgPath ="";
    GridImageAdapter gridImageAdapter;
    private ProductComment productComment;
    public static int imgrequestCode = 10;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public CommentProductFragment(Product product,int curindex) {
        this.curindex = curindex;
        this.product = product;
    }

    public interface GoNextListener {
        void goNext(int curinde);

    }
    private GoNextListener gonextListener;

    public GoNextListener getGonextListener() {
        return gonextListener;
    }

    public void setGonextListener(GoNextListener gonextListener) {
        this.gonextListener = gonextListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_comment_product, container, false);
        super.onCreate(savedInstanceState);

        init();
        return view;
    }

    private void init() {

        myresult = new ArrayList<>();

        productComment = new ProductComment();
        product_icon = view.findViewById(R.id.product_icon);
        product_name = view.findViewById(R.id.product_name);
        uploadImg = view.findViewById(R.id.uploadImg);
        submit = view.findViewById(R.id.submit);
        comment = view.findViewById(R.id.comment);
        ratingbar = view.findViewById(R.id.ratingbar);
        rec_img = view.findViewById(R.id.rec_img);


        gridImageAdapter = new GridImageAdapter(getContext(), myresult);
        rec_img.setAdapter(gridImageAdapter);
        rec_img.setLayoutManager(new FlowLayoutManager(getContext(),true));
        Glide.with(getContext()).load(product.getProduct_img()).placeholder(R.drawable.ic_error).into(product_icon);
        product_name.setText(product.getProduct_name() );
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(myresult.size()<5){
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        //把所有照片显示出来
                        intent.setType("image/*");
                        startActivityForResult(intent, imgrequestCode);
                    }else{
                        Toast.makeText(getContext(), "图片已超过5张", Toast.LENGTH_SHORT).show();
                    }

                }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserManage.isEmpty( comment.getText().toString()) ){
                    Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i=0;i<myresult.size();i++){
                        Bitmap imgbitmap = null;
                        try {
                            imgbitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), myresult.get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Result result1 = DatabaseUtil.uploadFile(AvatarUtil.getInstance().getFile(imgbitmap),"file","upload");
                        if(result1.getCode()!=200){
                            Toast.makeText(getContext(), "图片提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            if(imgPath !=""){
                                imgPath +=('|'+result1.getResult() );
                            }else{
                                imgPath +=result1.getResult();
                            }
                        }
                    }
                    productComment.setUser_name(UserManage.getInstance().getUserInfo(getContext()).getUsername() );
                    productComment.setProduct_id(product.getProduct_id() );
                    productComment.setCreatetime(TimeUtil.getInstance().getFormatTime().get(1));
                    productComment.setOrder_id(product.getOrder_id());
                    productComment.setUser_id(UserManage.getInstance().getUserInfo(getContext()).getId());
                    productComment.setProduct_score( ratingbar.getRating() );
                    productComment.setProduct_comment(comment.getText().toString() );
                    productComment.setComment_img(imgPath);
                    Result result = DatabaseUtil.insert(productComment,"product","insertcomment");
                    if(result.getCode()!=200){
                        Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
                    }else{
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("order_id", product.getOrder_id());
                        paramsMap.put("product_id", product.getProduct_id());
                        paramsMap.put("order_state", String.valueOf(OrderUtil.successcomment()));
                        Result result1 = DatabaseUtil.putParamters(paramsMap,"order","updatestate");
                        if(result1.getCode()!=200){
                            Toast.makeText(getContext(), "更新订单状态失败", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            gonextListener.goNext(curindex);
                        }

                    }
                }

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imgrequestCode  && data!=null){
            myresult.add(data.getData() );
            gridImageAdapter.notifyDataSetChanged();
//            System.out.println(myresult.size() );
        } else {
            Toast.makeText(getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
        }
    }
}