package com.zzkx.mtool.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.config.OrderStatus;
import com.zzkx.mtool.presenter.ComplainPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogOrderComplain;
import com.zzkx.mtool.view.customview.DialogResend;
import com.zzkx.mtool.view.iview.IComplainView;
import com.zzkx.mtool.view.iview.IView;
import com.zzkx.mtool.view.iview.OrderDetailMapPresender;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/15.
 */

public class OrderDetailMapDelivActivity extends BaseActivity implements AMap.OnMapLoadedListener, IView, View.OnClickListener, IComplainView {
    MapView mMapView;
    private AMap mMap;
    private Marker mMarker;
    private LatLng mMyLatLng;
    private LatLng mDlivLat;

    private Marker mDelivMarker;
    private String mDelivId;
    private OrderDetailMapPresender mShopLatPresenter;
    private String mStoreId;
    private int mOrderStatus;
    @BindView(R.id.tv_time)
    TextView mTime;
    private DialogOrderComplain mDialogOrderComplain;
    private ComplainPresenter mComplainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mMapView.getMap().setOnMapLoadedListener(this);
        mMap = mMapView.getMap();
        mShopLatPresenter = new OrderDetailMapPresender(this, mMap, this);
        mDelivId = getIntent().getStringExtra(Const.ID);
        mStoreId = getIntent().getStringExtra(Const.SHOP_ID);


        if (mDelivId == null)
            mDelivId = "1234";
        mShopLatPresenter.addMarkers(mStoreId, mDelivId);
        mShopLatPresenter.setTimeView(mTime, mOrderStatus);
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public int getContentRes() {
        return R.layout.activity_order_detail_deli_map;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("配送员位置");

        TextView tv_status = (TextView) findViewById(R.id.tv_order_status);
        TextView tv_status_status = (TextView) findViewById(R.id.tv_order_time_status);
        mOrderStatus = getIntent().getIntExtra("order_status", OrderStatus.DAIQUCAN);
        if (mOrderStatus == OrderStatus.DAIQUCAN) {
            tv_status.setText("正在赶往商家");
            tv_status_status.setText("预计到店");
        } else if (mOrderStatus == OrderStatus.PEISONGZHONG) {
            tv_status.setText("配送中");
            tv_status_status.setText("预计送达");
        }
        findViewById(R.id.tv_complain).setOnClickListener(this);
        mComplainPresenter = new ComplainPresenter(this);
        mDialogOrderComplain = new DialogOrderComplain(this, new DialogResend.OnConfrimListener() {
            @Override
            public void onConfirm() {
                String content = mDialogOrderComplain.getContent();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast("请输入投诉内容");
                    return;
                }
                String orderId = getIntent().getStringExtra(Const.ORDER_ID);
                mComplainPresenter.complain(content, 9, null, orderId);
                mDialogOrderComplain.dismiss();
            }
        });
    }

    @Override
    public void initNet() {

    }

    @Override
    public void onReload() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mShopLatPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complain:
                mDialogOrderComplain.show();
                break;
        }
    }

    @Override
    public void showComplainResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("投诉成功");
        }
    }
}
