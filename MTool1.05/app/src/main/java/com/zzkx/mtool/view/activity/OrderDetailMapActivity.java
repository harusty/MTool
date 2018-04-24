package com.zzkx.mtool.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.iview.IOderMapDetailMapView;
import com.zzkx.mtool.view.iview.OrderDetailMapPresender;

/**
 * Created by sshss on 2017/9/15.
 */

public class OrderDetailMapActivity extends BaseActivity implements IOderMapDetailMapView, AMap.OnMapLoadedListener {
    MapView mMapView;
    private OrderDetailMapPresender mPresenter;
    private AMap mMap;
    private Marker mMarker;

    private LatLng mShopLatLng;
    private LatLng mMyLatLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mMapView.getMap().setOnMapLoadedListener(this);
        mMap = mMapView.getMap();
        mPresenter = new OrderDetailMapPresender(this, mMapView.getMap(), this);
        String mLocInfo = getIntent().getStringExtra(Const.LOC_INFO);
        if (!TextUtils.isEmpty(mLocInfo)) {
            String[] split = mLocInfo.split(",");
            mShopLatLng = new LatLng(Double.parseDouble(split[1]), Double.parseDouble(split[0]));
        }
    }

    @Override
    public void onMapLoaded() {
//        mPresenter.locate();
        mPresenter.addMarkers(getIntent().getStringExtra(Const.SHOP_ID), null);
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_order_detail_map;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        ((TextView) findViewById(R.id.tv_section)).setText(getIntent().getStringExtra(Const.TITLE));
        ((TextView) findViewById(R.id.tv_address)).setText(getIntent().getStringExtra(Const.CUS_ADDRESS));
        setMainTitle("店铺位置");
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
        mPresenter.onDestroy();
    }
}
