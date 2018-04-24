package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BillBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IBillView;

/**
 * Created by sshss on 2017/12/19.
 */

public class BillPresenter extends BasePresenter<IBillView, BillBean> {

    public BillPresenter(IBillView view) {
        super(view);
    }
    public void getBillList(){
        getView().showProgress(true);
        getHttpModel().request(API.BILL_LIST,null);
    }

    @Override
    public void onSuccessM(BillBean bean) {
        getView().showProgress(false);
        getView().shoBillList(bean.data);
    }
}
