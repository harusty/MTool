package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.StateDetailBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IStateDetailView;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateDetailPresenter extends BasePresenter<IStateDetailView, StateDetailBean> {
    public StateDetailPresenter(IStateDetailView view) {
        super(view);
    }

    @Override
    public void onSuccessM(StateDetailBean bean) {
        getView().showProgress(false);
        getView().showDate(bean);
    }

    public void getDetailInfo(String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.forumPost = new RequestBean();
        requestBean.forumPost.id = id;
        getHttpModel().request(API.STATE_DETAIL,requestBean);
    }
}
