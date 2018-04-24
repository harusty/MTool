package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ServicePhoneBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IServicePhoneView;

/**
 * Created by sshss on 2018/2/24.
 */

public class ServicePhonePresenter extends BasePresenter<IServicePhoneView, ServicePhoneBean> {

    public ServicePhonePresenter(IServicePhoneView view) {
        super(view);
    }

    public void getServicePhone() {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.keyCustom = "custom_service_phone";
        getHttpModel().request(API.SERVIVCE_PHONE,requestBean);
    }

    @Override
    public void onSuccessM(ServicePhoneBean bean) {
        getView().showPhone(bean.data);
        getView().showProgress(false);
    }
}
