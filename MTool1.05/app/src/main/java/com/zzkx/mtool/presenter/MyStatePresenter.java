package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMyStateView;

/**
 * Created by sshss on 2017/10/20.
 */

public class MyStatePresenter extends BasePresenter<IMyStateView, StateListBean> {
    public MyStatePresenter(IMyStateView view) {
        super(view);
    }

    @Override
    public void onSuccessM(StateListBean bean) {
        getView().showProgress(false);
        getView().showMyState(bean);
    }

    public void getMyState() {
        getView().showProgress(true);
        getHttpModel().request(API.MY_STATE_LIST, null);
    }
}
