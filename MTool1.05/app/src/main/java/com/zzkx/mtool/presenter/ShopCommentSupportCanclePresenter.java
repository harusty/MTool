package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ISupportCancleView;

/**
 * Created by sshss on 2017/10/20.
 */

public class ShopCommentSupportCanclePresenter extends BasePresenter<ISupportCancleView, BaseBean> {

    public ShopCommentSupportCanclePresenter(ISupportCancleView view) {
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
        requestBean.commentId = id;
        getHttpModel().request(API.SHOP_COMMENT_CANCLE_SUPPORT, requestBean, position);
    }
}
