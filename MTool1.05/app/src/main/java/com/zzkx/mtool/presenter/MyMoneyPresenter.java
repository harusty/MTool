package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.MyMoneyBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMyMoneyView;

/**
 * Created by sshss on 2018/1/3.
 */

public class MyMoneyPresenter extends BasePresenter<IMyMoneyView, MyMoneyBean> {

    public MyMoneyPresenter(IMyMoneyView view) {
        super(view);
    }

    public void getWalleteInfo(){
        getView().showProgress(true);
        getHttpModel().request(API.WALLETE_INFO,null);
    }

    @Override
    public void onSuccessM(MyMoneyBean bean) {
        getView().showProgress(false);
        getView().showWalletyInfo(bean);
    }
}
