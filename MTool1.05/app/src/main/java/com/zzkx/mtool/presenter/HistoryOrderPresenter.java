package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.HistoryOrderBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IHistoryOrderListView;

import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public class HistoryOrderPresenter extends BasePresenter<IHistoryOrderListView, HistoryOrderBean> {

    private List<HistoryOrderBean.DataBean> mData;

    public HistoryOrderPresenter(IHistoryOrderListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(HistoryOrderBean bean) {
        getView().showProgress(false);
        mData = bean.data;
        if (mData != null && mData.size() > 0) {
            getView().showData(mData);
        }
    }

    public void getHistory() {

        getView().showProgress(true);
        getHttpModel().request(API.HISTORY_ORDER, new RequestBean());

    }
}
