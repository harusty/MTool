package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddAttentionShopView;

/**
 * Created by sshss on 2017/12/13.
 */

public class CancleAttentionShopPresenter extends BasePresenter<IAddAttentionShopView, BaseBean> {

    public CancleAttentionShopPresenter(IAddAttentionShopView view) {
        super(view);
    }

    public void cancleShopAttention(String shopId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.id = shopId;
        getHttpModel().request(API.CANCLE_SHOP_ATTENTION, requestBean);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showCancleShopAttentionResult(bean);
    }
}
