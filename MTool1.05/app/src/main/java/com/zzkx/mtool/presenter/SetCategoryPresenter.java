package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CollectionCatBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICatSetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/18.
 */

public class SetCategoryPresenter extends BasePresenter<ICatSetView, BaseBean> {
    public SetCategoryPresenter(ICatSetView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showSetCategoryResult(bean);
    }

    public void setCatIds(String collectionId, List<CollectionCatBean.DataBean> selectList) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.memberCollectDetail = new RequestBean();
        requestBean.memberCollectDetail.collectId = collectionId;
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            ids.add(selectList.get(i).id);
        }
        requestBean.catalogIds = ids;
        getHttpModel().request(API.SET_COLLECTION_CAT, requestBean);
    }
}
