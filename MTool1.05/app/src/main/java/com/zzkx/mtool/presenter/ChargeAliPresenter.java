package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IChargeAliView;

/**
 * Created by sshss on 2018/1/3.
 */

public class ChargeAliPresenter extends BasePresenter<IChargeAliView, OrderPayResultBean> {
    public ChargeAliPresenter(IChargeAliView view) {
        super(view);
    }

    public void getPayInfo(RequestBean bean) {
        getView().showProgress(true);
        getHttpModel().request(API.WALLETE_CHARGE, bean);
    }

    @Override
    public void onSuccessM(OrderPayResultBean bean) {
        getView().showProgress(false);
        getView().showAliChargeInfo(bean);
    }
}
