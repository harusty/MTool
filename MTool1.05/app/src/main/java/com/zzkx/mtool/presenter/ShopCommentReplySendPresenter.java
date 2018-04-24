package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ISendCommentView;

/**
 * Created by sshss on 2017/9/27.
 */

public class ShopCommentReplySendPresenter extends BasePresenter<ISendCommentView, BaseBean> {

    private RequestBean mLastRequest;

    public ShopCommentReplySendPresenter(ISendCommentView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showSendSuccess(mLastRequest);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    public void send(RequestBean requestBean) {
        getView().showProgress(true);
        mLastRequest = requestBean;
        getHttpModel().request(API.SHOP_COMMENT_REPLY, mLastRequest);
    }
}
