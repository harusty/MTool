package com.zzkx.mtool.presenter;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ICollectView;

/**
 * Created by sshss on 2017/9/28.
 */

public class CollectPresenter extends BasePresenter<ICollectView, BaseBean> {

    public CollectPresenter(ICollectView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().collectSuccess(bean);
    }

    public void collectShop(String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberShopCollect = new RequestBean();
        requestBean.memberShopCollect.shopId = id;
        getHttpModel().request(API.SHOP_COLLECT, requestBean);
    }

    public void collectGood(String id, String goodImage, double price) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberGoodsCollect = new RequestBean();
        requestBean.memberGoodsCollect.goodsId = id;
        requestBean.memberGoodsCollect.goodsImage = goodImage;
        requestBean.memberGoodsCollect.goodsPrice = price;
        getHttpModel().request(API.COLLECT_GOOD, requestBean);
    }

    @Override
    public void onConnectFaild(ErrorBean bean) {
        ToastUtils.showToast(MyApplication.getContext().getString(R.string.netErroRetry));
    }
}
