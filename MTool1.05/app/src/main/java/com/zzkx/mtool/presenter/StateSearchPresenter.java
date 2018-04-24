package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IStateSearcView;

/**
 * Created by sshss on 2017/10/24.
 */

public class StateSearchPresenter extends BasePresenter<IStateSearcView, StateListBean> {
    public StateSearchPresenter(IStateSearcView view) {
        super(view);
    }

    @Override
    public void onSuccessM(StateListBean bean) {
        getView().showProgress(false);
        if (bean.status == 1) {
            getView().showData(bean.data);
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    public void search(String key, boolean isSupported) {
        getView().showProgress(true);
        getHttpModel().cancleAll();
        RequestBean requestBean = new RequestBean();
        if(isSupported){
            requestBean.keyWord = key;
            getHttpModel().request(API.MY_SUPPOSED_ACT, requestBean);
        }else{
            requestBean.forumPost = new RequestBean();
            requestBean.forumPost.content = key;
            getHttpModel().request(API.SEARCH_STATE, requestBean);

        }

    }
}
