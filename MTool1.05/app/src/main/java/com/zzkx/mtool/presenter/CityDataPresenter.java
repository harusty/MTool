package com.zzkx.mtool.presenter;

import android.text.TextUtils;

import com.zzkx.mtool.bean.CityDataBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICityListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/11/23.
 */

public class CityDataPresenter extends BasePresenter<ICityListView, CityDataBean> {

    private HashMap<String, Integer> mSections = new HashMap<>();

    public CityDataPresenter(ICityListView view) {
        super(view);
    }

    public void getCityData() {
        getView().showProgress(true);
        getHttpModel().request(API.CITY_DATA, null);
    }

    @Override
    public void onSuccessM(CityDataBean bean) {
        getView().showCityData(bean);
        getView().showProgress(false);
    }

    public Integer getSectionIndex(String alpah) {
        return mSections.get(alpah);
    }

    @Override
    public void onSuccessWorkThread(CityDataBean bean) {
        super.onSuccessWorkThread(bean);
        List<CityDataBean.Data> data = bean.data;
        bean.cusData = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                CityDataBean.Data data1 = data.get(i);
                if (i == 0 || !TextUtils.equals(data1.charAlpha, data.get(i - 1).charAlpha)) {
                    mSections.put(data1.charAlpha, bean.cusData.size());
                    bean.cusData.add(data1.charAlpha);
                }
                bean.cusData.add(data1);
            }
            bean.data.clear();
        }
    }
}
