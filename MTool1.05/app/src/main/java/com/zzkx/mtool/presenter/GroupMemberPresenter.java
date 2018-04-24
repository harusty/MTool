package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.GroupMemberBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IGroupMemberView;

/**
 * Created by sshss on 2017/12/8.
 */

public class GroupMemberPresenter extends BasePresenter<IGroupMemberView, GroupMemberBean> {

    public GroupMemberPresenter(IGroupMemberView view) {
        super(view);
    }

    @Override
    public void onSuccessM(GroupMemberBean bean) {
        getView().showMembers(bean);
        getView().showProgress(false);
    }

    public void getMembers(String groupId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.groupId = groupId;
        getHttpModel().request(API.GROUP_MEMBERS, requestBean);
    }
}
