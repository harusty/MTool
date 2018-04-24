package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.SystemRecommendListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IRecommentListView;

/**
 * Created by sshss on 2018/2/1.
 */

public class SystemRecommendPresenter extends BasePresenter<IRecommentListView,SystemRecommendListBean> {

    public SystemRecommendPresenter(IRecommentListView view) {
        super(view);
    }



    public void getRecommendUser(){
        getView().showProgress(true);
        getHttpModel().request(API.SYSTEM_RECOMMEND_USER,null);
    }
    @Override
    public void onSuccessM(SystemRecommendListBean bean) {
        getView().showRecommendList(bean);
        getView().showProgress(false);
    }
}
