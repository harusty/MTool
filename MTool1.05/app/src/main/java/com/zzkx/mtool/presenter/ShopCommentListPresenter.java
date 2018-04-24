package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopCommentListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IShopCommentListView;

/**
 * Created by sshss on 2017/10/14.
 */

public class ShopCommentListPresenter extends BaseListPresenter<IShopCommentListView, ShopCommentListBean> {

    private String mShopId;

    public ShopCommentListPresenter(IShopCommentListView view, String shopId) {
        super(view);
        mShopId = shopId;
    }

    @Override
    public void getListData(int pageNum) {
        RequestBean requestBean = new RequestBean();
        requestBean.pageNum = pageNum;
        requestBean.numPerPage = 10;
        requestBean.merchantRestaurants = new RequestBean.MerchantRestaurants();
        requestBean.merchantRestaurants.id = mShopId;
        getHttpModel().request(API.SHOP_COMMENT_LIST, requestBean);
    }
}
