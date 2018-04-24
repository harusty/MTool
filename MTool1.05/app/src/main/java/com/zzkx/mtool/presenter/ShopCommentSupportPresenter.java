package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ISupportView;

/**
 * Created by sshss on 2017/9/26.
 */

public class ShopCommentSupportPresenter extends BasePresenter<ISupportView, BaseBean> {

    public ShopCommentSupportPresenter(ISupportView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().onSuppotedSuccess(bean);
    }


    public void support(String id, int position) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.commentId = id;
        getHttpModel().request(API.SHOP_COMMENT_TO_SUPPORT, requestBean, null, null, position + "");
    }

}
