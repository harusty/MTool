package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IWithdrawView;

/**
 * Created by sshss on 2018/1/5.
 */

public class WithdrawPresenter extends BasePresenter<IWithdrawView,BaseBean> {
    public WithdrawPresenter(IWithdrawView view) {
        super(view);
    }

    public void withDraw(RequestBean requestBean){
        getView().showProgress(true);
        getHttpModel().request(API.WITHDRAW,requestBean);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showWithDrawResult(bean);
    }
}
