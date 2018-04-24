package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserDetailBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IUserDetailView;

/**
 * Created by sshss on 2017/9/22.
 */

public class UserDetailPresenter extends BaseListPresenter<IUserDetailView, UserDetailBean> {
    private String mHxUsername;
    private String mUserId;

    public UserDetailPresenter(IUserDetailView view, String userId, String hxUserName) {
        super(view);
        mUserId = userId;
        mHxUsername = hxUserName;
    }

    @Override
    public void getListData(int pageNum) {
        RequestBean requestBean = new RequestBean();
        requestBean.userMember = new RequestBean();
        if (mUserId != null)
            requestBean.userMember.id = mUserId;
        else
            requestBean.userMember.hxUsername = mHxUsername;

        requestBean.pageNum = pageNum;
        requestBean.numPerPage = 15;
        getHttpModel().request(API.USER_DETAIL, requestBean);
    }

    @Override
    public void onSuccessM(UserDetailBean bean) {

        if (getPageNum() == -1) {
            getView().showData(bean);
        }

        super.onSuccessM(bean);

    }
}
