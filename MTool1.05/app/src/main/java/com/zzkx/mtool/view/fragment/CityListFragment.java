package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CityDataBean;
import com.zzkx.mtool.bean.CurrentCityInfo;
import com.zzkx.mtool.presenter.CityDataPresenter;
import com.zzkx.mtool.view.activity.MainActivity;
import com.zzkx.mtool.view.adapter.CityDataAdapter;
import com.zzkx.mtool.view.iview.ICityListView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/11/23.
 */

public class CityListFragment extends BaseFragment implements ICityListView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CityDataPresenter mCityDataPresenter;
    private CityDataAdapter mCityDataAdapter;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mRefreshLayout.setEnabled(false);
        mCityDataPresenter = new CityDataPresenter(this);
    }

    @Override
    public void initNet() {
        mCityDataPresenter.getCityData();
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showCityData(CityDataBean bean) {
        mCityDataAdapter = new CityDataAdapter(bean.cusData);
        mListView.setAdapter(mCityDataAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = mCityDataAdapter.getItem(position);
                if (item instanceof String)
                    return;
                showCityLat((CityDataBean.Data) item);
            }
        });
    }

    public void onInitialClick(String alpah) {
        Integer sectionIndex = mCityDataPresenter.getSectionIndex(alpah);
        if (sectionIndex != null) {
            mListView.setSelection(sectionIndex + mListView.getHeaderViewsCount());
        }
    }

    public void showCityLat(CityDataBean.Data bean) {
        CurrentCityInfo currentCityInfo = new CurrentCityInfo();
        currentCityInfo.latitude = bean.latitude;
        currentCityInfo.longitude = bean.longitude;
        currentCityInfo.city = bean.name;
        currentCityInfo.cityCode = bean.citycode;
        MyApplication.getInstance().setCurrentCityInfo(currentCityInfo);
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
