package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.MenuDetailBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMenuDetailVew;

/**
 * Created by sshss on 2017/10/19.
 */

public class MenuDetailPresenter extends BasePresenter<IMenuDetailVew, MenuDetailBean> {

    public MenuDetailPresenter(IMenuDetailVew view) {
        super(view);
    }

    @Override
    public void onSuccessM(MenuDetailBean bean) {
        getView().showProgress(false);
        getView().showMenuDetail(bean);
    }

    public void getMenuDetailInfo(String id) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.foodInfo = new RequestBean.FoodInfo();
        requestBean.foodInfo.id = id;
        getHttpModel().request(API.MENU_DETAIL, requestBean);
    }
}
