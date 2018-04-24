package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IStateCollectView;

/**
 * Created by sshss on 2017/10/19.
 */

public class StateCollectPresenter extends BasePresenter<IStateCollectView,BaseBean> {

    public StateCollectPresenter(IStateCollectView view) {
        super(view);
    }



    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showCollectResult(bean);
    }

    public void collectState(String id, int clickPosition){
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.postId = id;
        getHttpModel().request(API.ADD_STATE_COLLECTION,requestBean,clickPosition);
    }
}
