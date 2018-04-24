package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.OrderPayResultBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IOrderConfrimView;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/9/5.
 */

public class OrderConfirmPresenter extends BasePresenter<IOrderConfrimView, OrderPayResultBean> {

    private RequestBean mRequestBean;
    public static final int ORDER_OUT = 0;
    public static final int ORDER_IN = 1;

    public OrderConfirmPresenter(IOrderConfrimView view) {
        super(view);
        initRequestBean();
    }

    public void initRequestBean() {
        mRequestBean = new RequestBean();
        mRequestBean.payInfo = new RequestBean.PayInfoBean();
        mRequestBean.orderDiningList = new ArrayList<>();
    }

    public void submint() {
        getView().showParentProgress(true);
        getHttpModel().request(API.SUBMIT_ORDER, mRequestBean);
    }

    @Override
    public void onSuccessM(OrderPayResultBean bean) {
        if (bean.status == 1) {
            getView().showPay(bean);
        } else {
            getView().showParentProgress(false);
            getView().showPayInfoError();
            ToastUtils.showToast(bean.msg);
        }
    }


    public RequestBean getRequestBean() {
        return mRequestBean;
    }


}
