package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddCardVew;

/**
 * Created by sshss on 2018/1/4.
 */

public class AddCardPresenter extends BasePresenter<IAddCardVew, BaseBean> {
    public AddCardPresenter(IAddCardVew view) {
        super(view);
    }


    public void addCard(RequestBean requestBean) {
        getView().showProgress(true);
        getHttpModel().request(API.ADD_BANK_CARD, requestBean);

    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showAddResult(bean);
    }
}
