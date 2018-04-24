package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.CollectionCatBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICollectCatListView;

/**
 * Created by sshss on 2017/10/17.
 */

public class CollectCatListPreseter extends BasePresenter<ICollectCatListView, CollectionCatBean> {

    public CollectCatListPreseter(ICollectCatListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(CollectionCatBean bean) {
        getView().showProgress(false);
        getView().showCatNameList(bean.data);
    }

    public void getCatList(int type) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectCatalog = new RequestBean();
        requestBean.memberCollectCatalog.type = type;
        getHttpModel().request(API.COLLECT_CATNAME_LIST, requestBean);
    }
}
