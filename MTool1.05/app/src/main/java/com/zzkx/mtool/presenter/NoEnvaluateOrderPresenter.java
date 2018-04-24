package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.OrderListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.INoEnvaluateOrderView;

import java.util.List;

/**
 * Created by sshss on 2017/9/24.
 */

public class NoEnvaluateOrderPresenter extends BasePresenter<INoEnvaluateOrderView,OrderListBean> {

    public NoEnvaluateOrderPresenter(INoEnvaluateOrderView view) {
        super(view);
    }

    @Override
    public void onSuccessM(OrderListBean bean) {
        getView().showProgress(false);
        List<OrderListBean.DataBean> data = bean.data;
        if(data != null && data.size() > 0){
            getView().showData(data);
        }
    }

    public void getData() {
        getView().showProgress(true);
        getHttpModel().request(API.NO_ENVALUATE_ORDER_LIST,null);
    }
}
