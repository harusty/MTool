package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.UserPrivacyBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserPrivacyView;

/**
 * Created by sshss on 2017/12/14.
 */

public class SearchUserPrivacyPresenter extends BasePresenter<IUserPrivacyView, UserPrivacyBean> {

    public SearchUserPrivacyPresenter(IUserPrivacyView view) {
        super(view);
    }

    public void searchUserPrivacy(){
        getView().showProgress(true);
        getHttpModel().request(API.SEARCH_USER_PRIVACY,null);
    }
    @Override
    public void onSuccessM(UserPrivacyBean bean) {
        getView().showProgress(false);
        getView().showUserPrivacy(bean);
    }
}
