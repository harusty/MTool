package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddAttentionView;

/**
 * Created by sshss on 2017/11/22.
 */

public class DeleteAttenionPresenter extends BasePresenter<IAddAttentionView, BaseBean> {

    public DeleteAttenionPresenter(IAddAttentionView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showDelAttentionFaild(bean);
    }

    public void del(String userId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.idolId = userId;
        getHttpModel().request(API.DEL_ATTENTION, requestBean);
    }
}
