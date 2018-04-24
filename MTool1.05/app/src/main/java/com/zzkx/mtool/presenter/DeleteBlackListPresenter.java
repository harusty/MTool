package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IDelBlackListView;

import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public class DeleteBlackListPresenter extends BasePresenter<IDelBlackListView, BaseBean> {

    public DeleteBlackListPresenter(IDelBlackListView view) {
        super(view);
    }

    public void deleteFromBlackList(List<String> ids) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.hxNames = ids;
        getHttpModel().request(API.REMOVE_FROM_BLACK_LIST,requestBean);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showDeleteResult(bean);
    }
}
