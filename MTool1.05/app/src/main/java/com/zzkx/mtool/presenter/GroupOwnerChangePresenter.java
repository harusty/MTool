package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IOWnerChangeView;

/**
 * Created by sshss on 2018/2/1.
 */

public class GroupOwnerChangePresenter extends BasePresenter<IOWnerChangeView, BaseBean> {

    public GroupOwnerChangePresenter(IOWnerChangeView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showResult(bean);
    }

    public void change(String hxId, String groupId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.newGroupOwnId = hxId;
        requestBean.groupid = groupId;
        getHttpModel().request(API.CHANGE_GROUP_OWNER,requestBean);
    }
}
