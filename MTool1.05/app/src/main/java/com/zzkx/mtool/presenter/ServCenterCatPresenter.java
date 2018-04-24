package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ServCenterCatBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IServCenterView;

/**
 * Created by sshss on 2018/2/24.
 */

public class ServCenterCatPresenter extends BasePresenter<IServCenterView, ServCenterCatBean> {
    public ServCenterCatPresenter(IServCenterView view) {
        super(view);
    }

    @Override
    public void onSuccessM(ServCenterCatBean bean) {
        getView().showCat(bean);
        getView().showProgress(false);
    }

    public void getCats(String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.catalogId = id;
        getHttpModel().request(API.SERV_CENTER_QUES_CAT, requestBean,id);
    }
}
