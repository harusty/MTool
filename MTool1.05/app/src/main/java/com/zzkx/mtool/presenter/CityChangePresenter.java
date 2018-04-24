package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.view.iview.ICityListView;

/**
 * Created by sshss on 2017/8/23.
 */

public class CityChangePresenter extends BasePresenter<ICityListView,BaseBean> {


    public CityChangePresenter(ICityListView view) {
        super(view);
    }


    @Override
    public void onSuccessM(BaseBean bean) {

    }
}
