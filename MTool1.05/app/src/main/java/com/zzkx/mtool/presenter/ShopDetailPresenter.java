package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopDetailBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IShopDetailView;

/**
 * Created by sshss on 2017/9/28.
 */

public class ShopDetailPresenter extends BaseListPresenter<IShopDetailView, ShopDetailBean> {
    private String mShopId;


    public ShopDetailPresenter(IShopDetailView view, String id) {
        super(view);
        mShopId = id;
    }

    @Override
    public void onSuccessM(ShopDetailBean bean) {
        getView().showProgress(false);
        if (getPageNum() == -1) {
            getView().showData(bean);
        }
        super.onSuccessM(bean);

    }

    @Override
    public void getListData(int pageNum) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberShopCollect = new RequestBean();
        requestBean.memberShopCollect.shopId = mShopId;
        requestBean.pageNum = pageNum;
        requestBean.numPerPage = 15;
        getHttpModel().request(API.SHOP_DETAIL_INFO, requestBean);
    }
}
