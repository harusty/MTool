package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserSettingBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserSettingView;

/**
 * Created by sshss on 2017/11/22.
 */

public class UserSettingPresenter extends BasePresenter<IUserSettingView, UserSettingBean> {

    public UserSettingPresenter(IUserSettingView view) {
        super(view);
    }

    @Override
    public void onSuccessM(UserSettingBean bean) {
        getView().showProgress(false);
        getView().showSettingSuccess(bean);
    }

    public void setUser(RequestBean requestBean) {
        getView().showProgress(true);
        getHttpModel().request(API.SET_USER, requestBean);
    }
}
