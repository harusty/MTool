package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddAttentionView;

/**
 * Created by sshss on 2017/11/22.
 */

public class AddAttenionPresenter extends BasePresenter<IAddAttentionView, BaseBean> {

    public AddAttenionPresenter(IAddAttentionView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showAttentionAddSuccess(bean);
    }

    public void add(String userId, Object tag) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.idolId = userId;
        getHttpModel().request(API.ADD_ATTENTION, requestBean, tag);
    }

    public void add(String userId) {
        add(userId, "");
    }
}
