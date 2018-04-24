package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IComplainView;

/**
 * Created by sshss on 2018/2/1.
 */

public class ComplainPresenter extends BasePresenter<IComplainView, BaseBean> {
    public ComplainPresenter(IComplainView view) {
        super(view);
    }

    //0动态 1 餐饮(商家) 2群 9订单
    public void complain(String content, int type, String pics,String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.contentInfo = content;
        requestBean.type = type;
        requestBean.typeId = id;
        requestBean.pics = pics;
        getHttpModel().request(API.COMPLAIN,requestBean);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showComplainResult(bean);
        getView().showProgress(false);
    }
}
