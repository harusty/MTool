package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.AddressListBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IAddListView;

/**
 * Created by sshss on 2017/9/8.
 */

public class AddressListPresnter extends BasePresenter<IAddListView, AddressListBean> {

    public AddressListPresnter(IAddListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(AddressListBean bean) {
        getView().showProgress(false);
        if(bean.status == 1){
            getView().showAddList(bean.data);
        }else{
            ToastUtils.showToast("error");
        }
    }
    public void getAddress() {
        getView().showProgress(true);
        getHttpModel().request(API.ADD_LIST, null);
    }


}
