package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IDeleteCatView;

/**
 * Created by sshss on 2017/10/18.
 */

public class CatDeletePresenter extends BasePresenter<IDeleteCatView, BaseBean> {
    public CatDeletePresenter(IDeleteCatView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showDeleteResult(bean);
    }

    public void deleteCate(String catId) {

        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectCatalog = new RequestBean();
        requestBean.memberCollectCatalog.id = catId;
        getHttpModel().request(API.DELETE_COLLECTION_CAT, requestBean);
    }

    public void deleteCate(String id, String url) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.id = id;
        getHttpModel().request(url, requestBean);
    }
}
