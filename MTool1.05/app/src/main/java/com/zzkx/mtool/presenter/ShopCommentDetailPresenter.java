package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopCommentDetailBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IShopCommentDetailView;

/**
 * Created by sshss on 2018/1/26.
 */

public class ShopCommentDetailPresenter extends BasePresenter<IShopCommentDetailView,ShopCommentDetailBean> {
    public ShopCommentDetailPresenter(IShopCommentDetailView view) {
        super(view);
    }

    @Override
    public void onSuccessM(ShopCommentDetailBean bean) {
        getView().showDetailInfo(bean);
    }

    public void getDetailInfo(String id) {
        RequestBean requestBean = new RequestBean();
        requestBean.id = id;
        getHttpModel().request(API.SHOP_COMMENT_DETAIL,requestBean);
    }
}
