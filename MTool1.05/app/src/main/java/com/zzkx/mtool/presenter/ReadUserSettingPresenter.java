package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserSettingBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserSettingView;

/**
 * Created by sshss on 2017/11/22.
 */

public class ReadUserSettingPresenter extends BasePresenter<IUserSettingView, UserSettingBean> {

    public ReadUserSettingPresenter(IUserSettingView view) {
        super(view);
    }

    @Override
    public void onSuccessM(UserSettingBean bean) {
        getView().showProgress(false);
        getView().showSetting(bean);
    }

    public void getSetting(String hxUserName) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.friend2Id = hxUserName;
        getHttpModel().request(API.GET_USER_SETTING, requestBean);
    }
}
