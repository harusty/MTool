package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IStateList;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/9/30.
 */

public class StateDeletePresenter extends BasePresenter<IStateList,BaseBean> {
    public StateDeletePresenter(IStateList view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showDelete(bean);
    }

    public void delete(String id,int position) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.ids = new ArrayList<>();
        requestBean.ids.add(id);
        getHttpModel().request(API.DELETE_STATE,requestBean,position);
    }
}
