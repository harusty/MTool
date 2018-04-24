package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddContactTagView;

/**
 * Created by sshss on 2017/12/4.
 */

public class AddContactTagPresenter extends BasePresenter<IAddContactTagView, BaseBean> {

    public AddContactTagPresenter(IAddContactTagView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showAddContactTag(bean);
    }

    public void addContactTag(String tag) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.name = tag;
        getHttpModel().request(API.CREATE_CONTACT_TAG, requestBean);
    }
}
