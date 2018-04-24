package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.core.LatLonPoint;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.MainMapPresenter;
import com.zzkx.mtool.util.RightMenuHelper;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.DialogFilter;
import com.zzkx.mtool.view.iview.IMainView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/18.
 */

public class NearByShopActivity extends BaseActivity implements IMainView, View.OnClickListener,
        AMap.OnCameraChangeListener, AMap.OnMapLoadedListener {
    @BindView(R.id.swip_city_name)
    TextView mTvCityName;
    @BindView(R.id.icon_locate)
    View mLocateView;
    @BindView(R.id.locate_pb)
    ProgressBar mPbLocate;
    @BindView(R.id.shop_pager)
    ViewPager mShopPager;
    @BindView(R.id.right_container)
    View mRightContainer;
    @BindView(R.id.icon_eye)
    View mIcEye;
    private MainMapPresenter mMainMapPresenter;
    private MapView mapView;
    private RightMenuHelper mRightMenuHelper;
    private DialogFilter mDialogFilter;
    private LatLonPoint mLatPoint;


    @Override
    public int getContentRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        setMainTitle(getString(R.string.locate));
        findViewById(R.id.layout_swip_city).setVisibility(View.GONE);
        findViewById(R.id.layout_locate).setVisibility(View.GONE);
        findViewById(R.id.layout_search).setVisibility(View.GONE);

        mIcEye.setOnClickListener(this);
        findViewById(R.id.icon_list).setOnClickListener(this);
        setMainMenuEnable();
        mRightMenuHelper = new RightMenuHelper(mRightContainer);
        initRightFilterView();

        mLatPoint = getIntent().getParcelableExtra(Const.LOC_INFO);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mMainMapPresenter = new MainMapPresenter(this, mapView);
        mapView.getMap().setOnCameraChangeListener(this);
        mapView.getMap().setOnMapLoadedListener(this);
    }

    @Override
    public void onMapLoaded() {
        if(mLatPoint == null){
            ToastUtils.showToast("latpoint null");
            return;
        }
        mMainMapPresenter.searchByBound("", mLatPoint.getLatitude(),mLatPoint.getLongitude(),null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_eye:
                mDialogFilter.toggleRightFilter(mIcEye);
                break;
            case R.id.icon_list:
                startActivity(new Intent(this, ShopListActivity.class)
                        .putExtra(Const.LAT, mLatPoint)
                        .putExtra(Const.KEY_WORD, mMainMapPresenter.getKeyWord())
                );
                break;
            case R.id.icon_locate:
                mMainMapPresenter.locate();
                break;
        }
    }


    private void initRightFilterView() {
        mDialogFilter = new DialogFilter(this, new DialogFilter.OnMenuClick() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:
                        mMainMapPresenter.searchByBound("", mLatPoint.getLatitude(),mLatPoint.getLongitude(),null);
                        break;
                    case 1:
                        mMainMapPresenter.searchByBound("", mLatPoint.getLatitude(),mLatPoint.getLongitude(),new Pair<String, String>("s_takeOut","1"));
                        break;
                    case 2:
                        mMainMapPresenter.searchByBound("", mLatPoint.getLatitude(),mLatPoint.getLongitude(),new Pair<String, String>("s_takeIn","1"));
                        break;
                }
                mDialogFilter.dismiss();
            }
        });
    }

    @Override
    public void showLocatePb(boolean show) {
        if (show)
            mPbLocate.setVisibility(View.VISIBLE);
        else
            mPbLocate.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getKeyWord() {
        return null;
    }

    @Override
    public ViewPager getViewPager() {
        return mShopPager;
    }

    @Override
    public void setCurCity(String city) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMainMapPresenter != null)
            mMainMapPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMainMapPresenter != null)
            mMainMapPresenter.onDestroy();
    }

    @Override
    public void showProgress(boolean toShow) {
        super.showProgress(toShow);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mMainMapPresenter.onCameraChangeFinish(cameraPosition);
    }

    @Override
    public void onReload() {

    }
}
