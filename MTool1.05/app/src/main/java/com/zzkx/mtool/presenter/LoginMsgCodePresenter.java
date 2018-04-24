package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ILoginMsgView;

/**
 * Created by sshss on 2017/10/31.
 */

public class LoginMsgCodePresenter extends BasePresenter<ILoginMsgView, BaseBean> {

    public LoginMsgCodePresenter(ILoginMsgView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showMsgCodeSuccess();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    public void getLoginMsgCode(String phone) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        getHttpModel().request(API.LOGIN_MSG_CODE, requestBean);
    }
}
