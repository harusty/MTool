package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.bean.DistrictsBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.DistrictSearchPresenter;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.adapter.DistrictsAdapter;
import com.zzkx.mtool.view.customview.DialogInitialNab;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableHelper;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;
import com.zzkx.mtool.view.fragment.CityListFragment;
import com.zzkx.mtool.view.fragment.PoiResultListFragment;
import com.zzkx.mtool.view.iview.IDistrictsView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/23.
 */

public class CityChangeActivity extends BaseActivity implements View.OnClickListener, IDistrictsView {
    @BindView(R.id.icon_red_arrow)
    View mRedArrow;
    @BindView(R.id.scrolable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.et_search)
    EditText mInput;
    private TextView mLocateHint;
    private TextView mCurCity;
    private DialogInitialNab mDialog;
    private GridView mDistrickGrid;
    private boolean isLocating;
    private ScrollableHelper mHelper;
    private DistrictSearchPresenter mDistrictSearchPresenter;
    private DistrictsAdapter mDistrictsAdapter;
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation.getErrorCode() == 0) {
                CurrentCityInfo currentCity = new CurrentCityInfo();
                currentCity.latitude = amapLocation.getLatitude();
                currentCity.longitude = amapLocation.getLongitude();
                currentCity.city = amapLocation.getCity();
                currentCity.cityCode = amapLocation.getCityCode();
                MyApplication.getInstance().setCurrentCityInfo(currentCity);
                setLocInfo(currentCity);
            } else {
                mCurCity.setText("");
                mLocateHint.setText("点此重新定位");
                mLocateHint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        locate();
                    }
                });
            }
            isLocating = false;
        }
    };

    public void setLocInfo(CurrentCityInfo aMapLocation) {
        String city = aMapLocation.city;
        if (city.endsWith("市"))
            city = city.substring(0, city.length() - 1);
        mCurCity.setText(city);
        mLocateHint.setText("当前定位城市");
        if (!TextUtils.isEmpty(aMapLocation.cityCode))
            mDistrictSearchPresenter.getDistrict(aMapLocation.cityCode);
    }

    private CityListFragment mCityListFragment;
    private PoiResultListFragment mPoiResultListFragment;
    private TextWatcher mInputWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                getSupportFragmentManager().beginTransaction().show(mCityListFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mPoiResultListFragment).commit();
                mHelper.setCurrentScrollableContainer(mCityListFragment);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextView.OnEditorActionListener mInputKeyListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String keyword = mInput.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    InputUtils.hideInput(CityChangeActivity.this, mInput);
                    search(keyword);
                    getSupportFragmentManager().beginTransaction().show(mPoiResultListFragment).commit();
                    getSupportFragmentManager().beginTransaction().hide(mCityListFragment).commit();
                    mHelper.setCurrentScrollableContainer(mPoiResultListFragment);
                }
                return true;
            }
            return false;
        }
    };


    private void search(String keyword) {
        if (isLocating) {
            ToastUtils.showToast("正在获取当前位置");
        } else if (MyApplication.getInstance().getCurrentCity() == null) {
            ToastUtils.showToast("获取当前位置失败，请重新定位");
        } else {
            mPoiResultListFragment.search(keyword);
        }
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_city_change;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        findViewById(R.id.ic_left).setOnClickListener(this);
        mLocateHint = (TextView) findViewById(R.id.tv_locate_hint);
        mCurCity = (TextView) findViewById(R.id.tv_cur_city);
        mDistrickGrid = (GridView) findViewById(R.id.grid);
        mRedArrow.setOnClickListener(this);
        mInput.addTextChangedListener(mInputWatcher);
        mInput.setOnEditorActionListener(mInputKeyListener);
        ((TextView) findViewById(R.id.tv_main_title)).setText(getResources().getString(R.string.swip_city));

        mDistrictSearchPresenter = new DistrictSearchPresenter(this);
        initFragment();
        initInitialDialog();

        if (MyApplication.getInstance().getCurrentCity() == null) {
            locate();
        } else {
            isLocating = false;
            setLocInfo(MyApplication.getInstance().getCurrentCity());
        }
    }

    private void initFragment() {
        mCityListFragment = new CityListFragment();
        mPoiResultListFragment = new PoiResultListFragment();
        mPoiResultListFragment.setOnPoiClickListener(new PoiResultListFragment.OnPoiClickListener() {
            @Override
            public void onPoiClick(PoiItem poiItem) {
                startActivity(new Intent(CityChangeActivity.this, MainActivity.class)
                        .putExtra(Const.LOC_INFO, poiItem)
                );
                finish();
            }
        });
        mHelper = mScrollableLayout.getHelper();
        mHelper.setCurrentScrollableContainer(mCityListFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mPoiResultListFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mCityListFragment).show(mCityListFragment).commit();
    }

    private void locate() {
        isLocating = true;
        mLocateHint.setText("正在获取当前位置");
        LocateUtil.getInstance(mLocationListener).locate();
    }

    private void initInitialDialog() {
        mDialog = new DialogInitialNab(this);
        mDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String alpah = (String) view.getTag();
                mScrollableLayout.scrollTo(0, mScrollableLayout.getHeadHeight());
                mCityListFragment.onInitialClick(alpah);
                mDialog.dismiss();

            }
        });
    }

    @Override
    public void initNet() {

    }

    @Override
    public void onReload() {
        setLocInfo(MyApplication.getInstance().getCurrentCity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left:
                finish();
                break;
            case R.id.icon_red_arrow:
                getSupportFragmentManager().beginTransaction().show(mCityListFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mPoiResultListFragment).commit();
                mHelper.setCurrentScrollableContainer(mCityListFragment);
                showPop();
                break;
        }
    }

    private void showPop() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        } else {
            mDialog.show(mRedArrow);
        }
    }

    @Override
    public void showDistrict(DistrictsBean bean) {
        if (bean.data != null && bean.data.size() > 0) {
            mDistrictsAdapter = new DistrictsAdapter(bean.data);
            mDistrickGrid.setAdapter(mDistrictsAdapter);

            mDistrickGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DistrictsBean.DataBean item = mDistrictsAdapter.getItem(position);
                    showCityLat(item);
                }
            });
        }
    }

    public void showCityLat(DistrictsBean.DataBean item) {
        MyApplication.getInstance().getCurrentCity().longitude = item.longitude;
        MyApplication.getInstance().getCurrentCity().latitude = item.latitude;
        MyApplication.getInstance().getCurrentCity().city = item.name;

        startActivity(new Intent(CityChangeActivity.this, MainActivity.class));
        finish();

    }
}
