package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMySupposedStateView;

/**
 * Created by sshss on 2017/10/10.
 */

public class SupposedStatePresenter extends BasePresenter<IMySupposedStateView, StateListBean> {
    public SupposedStatePresenter(IMySupposedStateView view) {
        super(view);
    }

    @Override
    public void onSuccessM(StateListBean bean) {
        getView().showProgress(false);
        getView().showData(bean);
    }
    public void getData(){
        getView().showProgress(true);
        getHttpModel().request(API.MY_SUPPOSED_ACT,new RequestBean());
    }
}
