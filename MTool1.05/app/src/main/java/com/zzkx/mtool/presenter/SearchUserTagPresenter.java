package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserTagBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ISearchUserTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/12/14.
 */

public class SearchUserTagPresenter extends BasePresenter<ISearchUserTagView, UserTagBean> {
    private List<String> mIds;

    public SearchUserTagPresenter(ISearchUserTagView view) {
        super(view);
    }

    public void searchUserTag(String userId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberId = userId;
        getHttpModel().request(API.SEARCH_USER_TAG, requestBean);
    }

    @Override
    public void onSuccessM(UserTagBean bean) {
        getView().showProgress(false);
        getView().showUserTag(mIds);
    }

    @Override
    public void onSuccessWorkThread(UserTagBean bean) {
        List<UserTagBean.DataBean> data = bean.data;
        if(data!= null && data.size() > 0){
           mIds= new ArrayList<>();
            for (UserTagBean.DataBean tagBean : data) {
                mIds.add(tagBean.id);
            }
        }
    }
}
