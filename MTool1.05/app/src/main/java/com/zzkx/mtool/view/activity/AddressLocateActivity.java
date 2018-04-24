package com.zzkx.mtool.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddressLocatePresenter;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.view.adapter.LocateNeayByAdapter;
import com.zzkx.mtool.view.iview.IAddressLocateView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/7.
 */

public class AddressLocateActivity extends BaseActivity implements IAddressLocateView,
        View.OnClickListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, GeocodeSearch.OnGeocodeSearchListener {

    MapView mMapView;
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.icon_locate)
    View mIconLocate;
    @BindView(R.id.locate_pb)
    ProgressBar mLocatePb;
    @BindView(R.id.layout_pb)
    View mAddPb;
    @BindView(R.id.search_main)
    EditText mEtSearch;
    private AddressLocatePresenter mPresenter;
    private List<PoiItem> mDataList;
    private LocateNeayByAdapter mAdapter;
    private boolean mNoBound;
    private GeocodeSearch geocodeSearch;

    @Override
    public int getContentRes() {
        return R.layout.activity_address_locate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mPresenter = new AddressLocatePresenter(this, mMapView.getMap());
        mMapView.getMap().setOnCameraChangeListener(this);
        mMapView.getMap().setOnMapLoadedListener(this);
    }


    @Override
    public void initViews() {
        setMainMenuEnable();
        findViewById(R.id.layout_locate).setOnClickListener(this);
        setMainTitle(getString(R.string.locateArea));
        mNoBound = getIntent().getBooleanExtra(Const.NO_BOUND, false);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(mEtSearch.getText().toString().trim())) {
                        InputUtils.hideInput(AddressLocateActivity.this, mEtSearch);
                        mPresenter.search(mEtSearch.getText().toString().trim(), mNoBound);
                    }
                    return true;
                }
                return false;
            }
        });
        geocodeSearch = new GeocodeSearch(MyApplication.getContext());
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void showLocatePb(boolean show) {
        if (show)
            mLocatePb.setVisibility(View.VISIBLE);
        else
            mLocatePb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress(boolean toShow) {
        int visble = toShow ? View.VISIBLE : View.INVISIBLE;
        mAddPb.setVisibility(visble);
    }

    @Override
    public void showAddressInfo(List<PoiItem> poiItems) {
        if (mAdapter == null) {
            mDataList = poiItems;
            mAdapter = new LocateNeayByAdapter(this, mDataList);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PoiItem poiItem = mDataList.get(position);
                    if (mNoBound) {
                        showProgress(true);
                        LatLonPoint latLonPoint = new LatLonPoint(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
                        geocodeSearch.getFromLocationAsyn(query);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(Const.LOC_INFO, poiItem);
                        setResult(Const.RESULT_SUCESS_CODE, intent);
                        finish();
                    }
                }
            });
        } else {
            mDataList.clear();
            mDataList.addAll(poiItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        showProgress(false);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            Intent intent = new Intent();
            intent.putExtra(Const.LOC_INFO, regeocodeAddress);
            setResult(Const.RESULT_SUCESS_CODE, intent);
            finish();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onMapLoaded() {
        mPresenter.locate();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_locate:
                mPresenter.locate();
                break;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mPresenter.onCameraChangeFinish(cameraPosition);
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


}
