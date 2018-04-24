package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.CityLatBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICityLatView;

/**
 * Created by sshss on 2017/11/28.
 */

public class CityLatPresenter extends BasePresenter<ICityLatView,CityLatBean> {
    public CityLatPresenter(ICityLatView view) {
        super(view);
    }

    @Override
    public void onSuccessM(CityLatBean bean) {
        getView().showCityLat(bean);
        getView().showProgress(false);
    }
    public void getCityLat(String cityId,String name){
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.id = cityId;
        getHttpModel().request(API.CITY_LAT,requestBean,name);
    }
}
