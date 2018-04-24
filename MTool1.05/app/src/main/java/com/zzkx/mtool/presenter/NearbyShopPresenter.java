package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.view.iview.INearByShopView;

/**
 * Created by sshss on 2017/9/18.
 */

public class NearbyShopPresenter extends BasePresenter<INearByShopView,BaseBean> {
    public NearbyShopPresenter(INearByShopView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {

    }
}
