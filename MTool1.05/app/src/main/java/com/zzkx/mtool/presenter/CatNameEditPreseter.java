package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICatNameEditView;

/**
 * Created by sshss on 2017/10/18.
 */

public class CatNameEditPreseter extends BasePresenter<ICatNameEditView, BaseBean> {
    public CatNameEditPreseter(ICatNameEditView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showEditResult(bean);
    }

    public void editName(String id, String name) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectCatalog = new RequestBean();
        requestBean.memberCollectCatalog.name = name;
        requestBean.memberCollectCatalog.id = id;
        getHttpModel().request(API.EDIT_COLLECTION_CAT, requestBean);
    }

    public void editName(String id, String name, String url) {
        RequestBean requestBean = new RequestBean();
        requestBean.name = name;
        requestBean.id = id;
        getHttpModel().request(url, requestBean);
    }
}
