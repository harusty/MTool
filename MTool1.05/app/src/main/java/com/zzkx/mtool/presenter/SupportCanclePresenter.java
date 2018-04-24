package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ISupportCancleView;

/**
 * Created by sshss on 2017/10/20.
 */

public class SupportCanclePresenter extends BasePresenter<ISupportCancleView, BaseBean> {

    public SupportCanclePresenter(ISupportCancleView view) {
        super(view);
    }


    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showCancleSupportResult(bean);
    }

    public void cancleSupport(String id, int position) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.forumPostSuppopp = new RequestBean();
        requestBean.forumPostSuppopp.postId = id;
        getHttpModel().request(API.CANCLE_SUPPORT, requestBean,position);
    }
}
