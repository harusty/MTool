package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserInfoBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserInfoView;

/**
 * Created by sshss on 2017/11/2.
 */

public class UserInfoPresenter extends BasePresenter<IUserInfoView,UserInfoBean> {
    public UserInfoPresenter(IUserInfoView view) {
        super(view);
    }

    @Override
    public void onSuccessM(UserInfoBean bean) {
        getView().showProgress(false);
        getView().showUserInfo(bean);
    }

    public void getUserInfo(){
        getView().showProgress(true);
        getHttpModel().request(API.USER_INFO,new RequestBean());
    }
}
