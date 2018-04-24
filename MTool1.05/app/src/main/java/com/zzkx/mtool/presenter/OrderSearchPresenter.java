package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.HistoryOrderBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IOrderSearchView;

/**
 * Created by sshss on 2017/10/25.
 */

public class OrderSearchPresenter extends BasePresenter<IOrderSearchView, HistoryOrderBean> {
    public OrderSearchPresenter(IOrderSearchView view) {
        super(view);
    }

    @Override
    public void onSuccessM(HistoryOrderBean bean) {
        getView().showProgress(false);
        getView().showData(bean);
    }

    public void search(String key) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.keyWord = key;
        getHttpModel().request(API.HISTORY_ORDER, requestBean);
    }
}
