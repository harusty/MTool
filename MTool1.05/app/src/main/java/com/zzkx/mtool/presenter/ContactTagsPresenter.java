package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.ContactTagBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IContactTagsView;

/**
 * Created by sshss on 2017/12/4.
 */

public class ContactTagsPresenter extends BasePresenter<IContactTagsView, ContactTagBean> {
    public ContactTagsPresenter(IContactTagsView view) {
        super(view);
    }

    @Override
    public void onSuccessM(ContactTagBean bean) {
        getView().showTags(bean);
        getView().showProgress(false);
    }

    public void getTags() {
        getView().showProgress(true);
        getHttpModel().request(API.CONTACT_TAGS, null);
    }
}
