package com.zzkx.mtool.presenter;

import com.google.gson.Gson;
import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IAddressEditView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sshss on 2017/9/8.
 */

public class AddEditPresenter extends BasePresenter<IAddressEditView, BaseBean> {
    private RequestBean mRequestBean;

    public AddEditPresenter(IAddressEditView view) {
        super(view);
        mRequestBean = new RequestBean();
        mRequestBean.memberReceiveAddr = new AddressListBean.AddressBean();
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showAdd(true);
        } else {
            ToastUtils.showToast("添加失败，请重试");
            getView().showAdd(false);
        }
    }

    public void setLocationInfo(double latitude, double longitude, String address) {
        mRequestBean.memberReceiveAddr.longitude = longitude;
        mRequestBean.memberReceiveAddr.latitude = latitude;
        mRequestBean.memberReceiveAddr.addrDetail = address;

    }

    public void submit(String name, String phone, int sex, String addDetail,
                       int isDeafault, AddressListBean.AddressBean editBean) {
        getView().showProgress(true);
        if (mRequestBean.memberReceiveAddr.latitude == 0 || mRequestBean.memberReceiveAddr.longitude == 0) {
            ToastUtils.showToast("locate info null");
            return;
        }
        getView().showProgress(true);
        mRequestBean.memberReceiveAddr.name = name;
        mRequestBean.memberReceiveAddr.phone = phone;
        mRequestBean.memberReceiveAddr.sex = sex;
        mRequestBean.memberReceiveAddr.addrDetail += addDetail;
        mRequestBean.memberReceiveAddr.defaultValue += isDeafault;
        String url;
        if (editBean == null) {
            url = API.ADD_ADDRESS;
        } else {
            url = API.UPDATE_ADDRESS;
            mRequestBean.memberReceiveAddr.id = editBean.id;
        }
        getHttpModel().request(url, mRequestBean);
    }

    public void delete(AddressListBean.AddressBean updateBean) {
        getView().showProgress(true);
        mRequestBean.memberReceiveAddr.id = updateBean.id;
        mRequestBean.memberReceiveAddr.defaultValue = updateBean.defaultValue;
        getHttpModel().request(API.DELETE_ADDRESS, mRequestBean, null, new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                getView().getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getView().showError(new ErrorBean(-1, call.request().url().toString()));
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String json = response.body().string();
                final BaseBean baseBean = new Gson().fromJson(json, BaseBean.class);
                getView().getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getView().showProgress(false);
                        getView().showDelete(baseBean);
                    }
                });

            }
        },null);
    }


}
