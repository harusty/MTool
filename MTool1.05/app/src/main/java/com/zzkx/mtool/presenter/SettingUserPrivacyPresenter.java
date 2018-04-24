package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserPrivacyView;

/**
 * Created by sshss on 2017/12/14.
 */

public class SettingUserPrivacyPresenter extends BasePresenter<IUserPrivacyView, BaseBean> {

    public SettingUserPrivacyPresenter(IUserPrivacyView view) {
        super(view);
    }

    public void setPrivacy(RequestBean requestBean) {
        getView().showProgress(true);
        getHttpModel().request(API.SET_USER_PRIVACY, requestBean);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showSetResult(bean);
    }
}
