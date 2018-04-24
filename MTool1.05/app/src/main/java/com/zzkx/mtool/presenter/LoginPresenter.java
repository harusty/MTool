package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.LoginBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;
import com.zzkx.mtool.util.HXLoginUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.UserInfoCachUtil;
import com.zzkx.mtool.view.iview.ILoginView;

/**
 * Created by sshss on 2017/6/23.
 * 根据具体情况复写onResponseErrorM，onConnectFaildM方法
 */

public class LoginPresenter extends BasePresenter<ILoginView, LoginBean> implements IPresenter {


    private String mPhone;
    private String mPwd;

    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(String phone, String pwd) {
        mPhone = phone;
        mPwd = pwd;
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.username = phone;
        requestBean.password = pwd;
        getHttpModel().request(API.LOGIN, requestBean);
    }

    public void login(RequestBean requestBean) {
        getView().showProgress(true);
        getHttpModel().request(API.OTHER_LOGIN, requestBean);
    }

    public void msgLogin(String phone, String code) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        requestBean.code = code;
        getHttpModel().request(API.MSG_LOGIN, requestBean);
    }

    /**
     * 此成功回调是执行在非UI线程，如果需要对数据二次初始化整理，可以在这执行
     *
     * @param bean
     */

    @Override
    public void onSuccessM(final LoginBean bean) {
        if (bean.status == 1) {
            LoginBean.Data data = bean.data;
            if (data.type != 0) {
                getView().showLoginFaild("非用户端账号！");
                return;
            }
            SPUtil.putBoolean(Const.IS_LOGIN, true);
            SPUtil.putString(Const.PHONE, mPhone);
            SPUtil.putString(Const.PWD, mPwd);
            UserInfoCachUtil.cachInfo(bean);
//            SPUtil.putString(Const.HX_ID, data.hxUsername);
//            SPUtil.putString(Const.HX_PWD, data.hxPassword);

            HXLoginUtil.login(null, new HXLoginUtil.LoginResultListener() {
                @Override
                public void onSuccess() {
                    getView().showLoginSuccess(bean);
                }

                @Override
                public void onFaild() {
                    getView().showLoginSuccess(bean);
                }
            });
        } else {
            getView().showLoginFaild(bean.msg);
        }
    }


    public void loginNoPwd(String phone) {
        mPhone = phone;
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.phone = phone;
        getHttpModel().request(API.NO_PWD_LOGIN, requestBean);
    }
}
