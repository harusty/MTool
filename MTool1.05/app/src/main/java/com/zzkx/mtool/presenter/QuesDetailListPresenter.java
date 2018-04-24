package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.QuesDetailListBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IQuesDetailView;

/**
 * Created by sshss on 2018/2/24.
 */

public class QuesDetailListPresenter extends BasePresenter<IQuesDetailView, QuesDetailListBean> {

    public QuesDetailListPresenter(IQuesDetailView view) {
        super(view);
    }

    public void getQuesDetail(String catalogId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.catalogId = catalogId;
        getHttpModel().request(API.QUES_DETAIL_LIST, requestBean);
    }

    @Override
    public void onSuccessM(QuesDetailListBean bean) {
        getView().showQuesDetail(bean);
        getView().showProgress(false);
    }
}
