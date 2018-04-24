package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserSearchBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddContactView;

/**
 * Created by sshss on 2017/12/5.
 */

public class AddContactPreseter extends BasePresenter<IAddContactView, UserSearchBean> {
    public AddContactPreseter(IAddContactView view) {
        super(view);
    }

    @Override
    public void onSuccessM(UserSearchBean bean) {
        getView().showProgress(false);
        getView().showSearchResult(bean);
    }

    public void search(String keyword) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.userMember = new RequestBean();
        requestBean.userMember.nickname = keyword;
        getHttpModel().request(API.SEARCH_USER,requestBean);
    }
}
