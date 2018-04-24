package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.zzkx.mtool.R;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.ToastUtils;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/15.
 */

public class LocateShareMapActivity extends BaseActivity implements AMap.OnMapLoadedListener, View.OnClickListener {

    MapView mMapView;
    private AMap mMap;
    @BindView(R.id.tv_section)
    TextView tv_section;
    @BindView(R.id.tv_address)
    TextView tv_address;
    private AMapLocation mMapLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mMapView.getMap().setOnMapLoadedListener(this);
        mMap = mMapView.getMap();
    }

    @Override
    public void onMapLoaded() {
        showProgress(true);
        LocateUtil.getInstance(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                showProgress(false);
                if (aMapLocation.getErrorCode() == 0) {
                    mMapLocation = aMapLocation;
                    tv_section.setText(aMapLocation.getPoiName());
                    tv_address.setText(aMapLocation.getAddress());
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location)));
                } else {
                    ToastUtils.showToast("获取当前位置失败");
                }
            }
        }).locate();
    }


    @Override
    public int getContentRes() {
        return R.layout.activity_locate_share;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("分享位置");
        findViewById(R.id.tv_share).setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        if (mMapLocation != null) {
            Intent intent = new Intent();
            intent.putExtra("latitude", mMapLocation.getLatitude());
            intent.putExtra("longitude", mMapLocation.getLongitude());
            intent.putExtra("address", mMapLocation.getAddress());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
