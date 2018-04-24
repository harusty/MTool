package com.zzkx.mtool.presenter;

import android.app.Activity;

import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.OrderDetailBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.imple.ISuccessListener;
import com.zzkx.mtool.view.iview.IOrderDetailView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sshss on 2017/9/14.
 */

public class OrderDetailPresenter extends BasePresenter<IOrderDetailView, OrderDetailBean> {
    private Activity mActivity;

    public OrderDetailPresenter(IOrderDetailView view, Activity activity) {
        super(view);
        mActivity = activity;
    }

    @Override
    public void onSuccessM(OrderDetailBean bean) {
        getView().showProgress(false);
        getView().showData(bean);
    }

    public void getOrderInfo(String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.orderDining = new RequestBean();
        requestBean.orderDining.id = id;
        getHttpModel().request(API.ORDER_DETAIL, requestBean);
    }

    public void submit(RequestBean requestBean, final ISuccessListener listener) {
        getView().showProgress(true);
        getHttpModel().request(API.SUBMIT_ORDER_ENVALUATE, requestBean, null, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            getView().showError(new ErrorBean(e.getMessage(), call.request().url().toString()));
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().showProgress(false);
                            BaseBean baseBean = Json_U.fromJson(string, BaseBean.class);
                            listener.onSuccess(baseBean);
                        }
                    });
                }
            }
        },null);
    }
}
