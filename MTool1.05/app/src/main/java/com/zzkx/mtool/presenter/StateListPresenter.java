package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.fragment.BaseListFragment;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateListPresenter extends BaseListPresenter<BaseListFragment, StateListBean> {
    private int mFollow = 1;
    private String mLabelId;

    public void setFollow(int follow) {
        setFollow(follow, null);
    }

    public void setFollow(int follow, String labelId) {
        mFollow = follow;
        mLabelId = labelId;
    }

    @Override
    public void getListData(int pageNum) {
        RequestBean requestBean = new RequestBean();
        requestBean.pageNum = pageNum;
        requestBean.numPerPage = 10;
        requestBean.follow = mFollow;
        requestBean.labelId = mLabelId;
        getHttpModel().request(API.STATE_LIST, requestBean);
    }

    public StateListPresenter(BaseListFragment view) {
        super(view);
    }
}
