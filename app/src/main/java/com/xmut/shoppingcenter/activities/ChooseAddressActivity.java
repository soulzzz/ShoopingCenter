package com.xmut.shoppingcenter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xmut.shoppingcenter.R;
import com.xmut.shoppingcenter.adapter.AddressAdapter;
import com.xmut.shoppingcenter.entity.Result;
import com.xmut.shoppingcenter.entity.UserAddress;
import com.xmut.shoppingcenter.okhttp.DatabaseUtil;
import com.xmut.shoppingcenter.okhttp.http.HttpAddress;
import com.xmut.shoppingcenter.utils.DialogUitl;
import com.xmut.shoppingcenter.utils.UserManage;

import java.util.List;

import static com.xmut.shoppingcenter.activities.PayOrderActivity.addressCode;

public class ChooseAddressActivity extends AppCompatActivity {
    Context context;
    AddressAdapter addressAdapter;
    List<UserAddress> userAddressList;
    boolean isCheckAddress = false;
    ImageView leave_bt,leave_bt2;
    RecyclerView addressrecycler;
    TextView addAddress, nullshow;
    Intent it;
    DialogUitl dialogUitl = DialogUitl.getInstance();
    private AMapLocationClient mClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    String mLocation = null;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption option = null;
    //编辑或新增地址
    Dialog dialog1= null;
    View contentView1 = null;
    TextView locate,saveaddress,title;
    EditText name,tel,address;
    boolean isManualOpen  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        isCheckAddress = getIntent().getBooleanExtra("checkaddress",false);
        setContentView(R.layout.activity_choose_address);
        initLocation();
        initData();
        init();

    }

    private void initLocation() {
        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        try {
            mClient = new AMapLocationClient(context);
            mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
                            //可在其中解析amapLocation获取相应内容。
//                            System.out.println(amapLocation.getAccuracy());
//                            System.out.println(amapLocation.getProvince());
//                            System.out.println(amapLocation.getCity());
//                            System.out.println(amapLocation.getDistrict());
//                            System.out.println(amapLocation.getStreet());
//                            System.out.println(amapLocation.getStreetNum());
//                            System.out.println(amapLocation.getDistrict());

                            //得到的数据仍是空的 继续获取数据
                            if(amapLocation.getAddress().isEmpty() ){
                                mClient.startLocation();
                            } // 界面开着 且数据不为空
                            else if(dialog1.isShowing() && !amapLocation.getAddress().isEmpty() && isManualOpen) {
                                address.setText(amapLocation.getAddress());
                                mClient.stopLocation();
                                Toast.makeText(context, "获取定位数据成功", Toast.LENGTH_SHORT).show();
                            } //界面没开时 获取到了数据 可以直接停止
                            else{
                                mClient.stopLocation();
                            }

//                            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                            amapLocation.getLatitude();//获取纬度
//                            amapLocation.getLongitude();//获取经度
//                            amapLocation.getAccuracy();//获取精度信息
//                            amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                            amapLocation.getCountry();//国家信息
//                            amapLocation.getProvince();//省信息
//                            amapLocation.getCity();//城市信息
//                            amapLocation.getDistrict();//城区信息
//                            amapLocation.getStreet();//街道信息
//                            amapLocation.getStreetNum();//街道门牌号信息
//                            amapLocation.getCityCode();//城市编码
//                            amapLocation.getAdCode();//地区编码
//                            amapLocation.getAoiName();//获取当前定位点的AOI信息
//                            amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                            amapLocation.getFloor();//获取当前室内定位的楼层
//                            amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
//获取定位时间
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            System.out.println(amapLocation.getErrorInfo());
                            System.out.println(amapLocation.getErrorCode());
                            mClient.startLocation();

                        }
                    }
                }
            };
            option = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//            option.setLocationMode(AMapLocationMode.Battery_Saving);
//关闭缓存机制
            option.setLocationCacheEnable(true);
            //获取一次定位结果：
            //该方法默认为false。
//            option.setOnceLocation(false);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            option.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            option.setNeedAddress(true);
            //设置是否允许模拟位置,默认为true，允许模拟位置
            option.setMockEnable(true);
            //关闭缓存机制
            option.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mClient.setLocationOption(option);
//            通过按钮点击启动定位获取地址信息

            mClient.startLocation();
        } catch (Exception e) {

        }
        if (mClient != null) {
//设置定位回调监听
            mClient.setLocationListener(mLocationListener);
        }
    }

    private void initData() {
        Result result = DatabaseUtil.selectById(HttpAddress.get(HttpAddress.useraddress(), "list", UserManage.getInstance().getUserInfo(this).getId()));
        userAddressList = DatabaseUtil.getObjectList(result, UserAddress.class);
    }

    @Override
    public void finish() {
        super.finish();
        if(mClient!=null)
            mClient.onDestroy();
    }

    private void init() {
        dialog1 = new Dialog(context);

        contentView1 = LayoutInflater.from(context).inflate(
                R.layout.dialog_addressutil, null);
        title = contentView1.findViewById(R.id.change_title);
        address = contentView1.findViewById(R.id.address);
        name = contentView1.findViewById(R.id.name);
         tel = contentView1.findViewById(R.id.tel);
        locate = contentView1.findViewById(R.id.locate);
        leave_bt2 = contentView1.findViewById(R.id.leave_bt);
        leave_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isManualOpen =true;
                Toast.makeText(context, "获取定位数据中，请稍后", Toast.LENGTH_SHORT).show();
                mClient.startLocation();
            }
        });
        dialog1.setContentView(contentView1);
        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                name.setText("");
                tel.setText("");
                address.setText("");
                isManualOpen = false;
            }
        });
        dialogUitl.setDialogTransparentandCircleFromRight(dialog1);
        dialogUitl.setDialogMatchParent(context, dialog1);
        saveaddress = contentView1.findViewById(R.id.saveaddress);

        addressrecycler = findViewById(R.id.addressrecycler);

        it = getIntent();
        nullshow = findViewById(R.id.nullshow);
        addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title.setText("新增收货地址");
                saveaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserManage.isEmpty(name.getText().toString()) || UserManage.isEmpty(tel.getText().toString()) || tel.getText().toString().length() != 11 || UserManage.isEmpty(address.getText().toString())) {
                            Toast.makeText(context, "输入格式错误", Toast.LENGTH_SHORT).show();
                        } else {
                            UserAddress userAddress = new UserAddress(UserManage.getInstance().getUserInfo(context).getId(), UserManage.trim(address.getText().toString())
                                    , UserManage.trim(name.getText().toString()), UserManage.trim(tel.getText().toString()));

                            Result result = DatabaseUtil.insert(userAddress, HttpAddress.get(HttpAddress.useraddress(), "insert"));
                            if (result.getCode() != 200) {
                                Toast.makeText(context, "新增地址失败", Toast.LENGTH_SHORT).show();
                            } else {
                                userAddressList = DatabaseUtil.getObjectList(result, UserAddress.class);
                                addressAdapter.setUserAddresses(userAddressList);
                                Toast.makeText(context, "新增地址成功", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();

                            }

                        }
                    }
                });
                dialog1.show();
            }
        });
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addressAdapter = new AddressAdapter(context, userAddressList);
        addressrecycler.setAdapter(addressAdapter);
        addressrecycler.setLayoutManager(new LinearLayoutManager(context));
        addressAdapter.setFirstListener(new AddressAdapter.FirstListener() {
            @Override
            public void onSelect(int pos) {
                if(isCheckAddress){
                    onEdit(pos);
                }else{
                    Intent it = getIntent();
                    it.putExtra("useraddress", userAddressList.get(pos));
                    setResult(addressCode, it);
                    finish();
                }

            }

            @Override
            public void onDelete(int pos) {
                Result result = DatabaseUtil.deleteById(HttpAddress.get(HttpAddress.useraddress(), "delete", userAddressList.get(pos).getAddress_id()));
                if (result.getCode() != 200) {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                } else {
                    userAddressList.remove(pos);
                    addressAdapter.notifyItemRemoved(pos);
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEdit(int pos) {

                title.setText("编辑收货地址");

                name.setText(userAddressList.get(pos).getUser_name());
                tel.setText(userAddressList.get(pos).getUser_tel());
                address.setText(userAddressList.get(pos).getUser_address());
                saveaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserManage.isEmpty(name.getText().toString()) || UserManage.isEmpty(tel.getText().toString()) || tel.getText().toString().length() != 11 || UserManage.isEmpty(address.getText().toString())) {
                            Toast.makeText(context, "输入格式错误", Toast.LENGTH_SHORT).show();
                        } else {
                            UserAddress userAddress = new UserAddress(UserManage.getInstance().getUserInfo(context).getId(), UserManage.trim(address.getText().toString())
                                    , UserManage.trim(name.getText().toString()), UserManage.trim(tel.getText().toString()));
                            userAddress.setAddress_id(userAddressList.get(pos).getAddress_id());
                            Result result = DatabaseUtil.updateById(userAddress, HttpAddress.get(HttpAddress.useraddress(), "update"));
                            if (result.getCode() != 200) {
                                Toast.makeText(context, "更新地址失败", Toast.LENGTH_SHORT).show();
                            } else {
                                userAddressList = DatabaseUtil.getObjectList(result, UserAddress.class);
                                addressAdapter.setUserAddresses(userAddressList);
                                Toast.makeText(context, "更新地址成功", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();

                            }

                        }
                    }
                });
                dialog1.show();
            }
        });

    }
}