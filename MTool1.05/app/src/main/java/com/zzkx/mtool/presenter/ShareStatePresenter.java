package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IStateView;

/**
 * Created by sshss on 2017/9/21.
 */

public class ShareStatePresenter extends BasePresenter<IStateView, BaseBean> {

    public ShareStatePresenter(IStateView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showPublishSuccess();
        }
        ToastUtils.showToast(bean.msg);
    }

    public void publish(RequestBean requestBean) {

        getView().showProgress(true);
        getHttpModel().request(API.SHARE_STATE, requestBean);

    }
}
