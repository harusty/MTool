package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ISetTagView;

import java.util.List;

/**
 * Created by sshss on 2017/12/14.
 */

public class SetMultiTagPresenter extends BasePresenter<ISetTagView,BaseBean> {

    public SetMultiTagPresenter(ISetTagView view) {
        super(view);
    }

    public void setTag(String userId,List<String> labelids){
        getView().showProgress(false);
        RequestBean requestBean = new RequestBean();
        requestBean.labelIds = labelids;
        requestBean.memberId = userId;
        getHttpModel().request(API.SET_MULTI_TAG,requestBean);
    }
    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showTagSetResult(bean);
    }
}
