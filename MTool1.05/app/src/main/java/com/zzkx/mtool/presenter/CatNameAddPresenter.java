package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICatNameAddView;

/**
 * Created by sshss on 2017/10/17.
 */

public class CatNameAddPresenter extends BasePresenter<ICatNameAddView, BaseBean> {

    public CatNameAddPresenter(ICatNameAddView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showAddResult(bean);
    }

    public void setName(String name, int type) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectCatalog = new RequestBean();
        requestBean.memberCollectCatalog.name = name;
        requestBean.memberCollectCatalog.type = type;
        getHttpModel().request(API.ADD_CAT, requestBean);
    }
}
