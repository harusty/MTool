package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.DistrictsBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IDistrictsView;

/**
 * Created by sshss on 2017/11/28.
 */

public class DistrictSearchPresenter extends BasePresenter<IDistrictsView, DistrictsBean> {

    public DistrictSearchPresenter(IDistrictsView view) {
        super(view);
    }

    @Override
    public void onSuccessM(DistrictsBean bean) {
        getView().showDistrict(bean);
    }

    public void getDistrict(String citycode) {
        RequestBean requestBean = new RequestBean();
        requestBean.citycode = citycode;
        getHttpModel().request(API.CITY_DISTRICT_DATA,requestBean);
    }
}
