package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IChargeWxView;

/**
 * Created by sshss on 2018/1/3.
 */

public class ChargeWxPresenter extends BasePresenter<IChargeWxView, OrderPayResultBean> {
    public ChargeWxPresenter(IChargeWxView view) {
        super(view);
    }

    public void getPayInfo(RequestBean bean) {
        getView().showProgress(true);
        getHttpModel().request(API.WALLETE_CHARGE, bean);
    }

    @Override
    public void onSuccessM(OrderPayResultBean bean) {
        getView().showProgress(false);
        getView().showWxChargeInfo(bean);
    }
}
