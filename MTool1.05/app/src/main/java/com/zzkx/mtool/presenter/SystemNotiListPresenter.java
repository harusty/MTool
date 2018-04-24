package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.SystemNotiBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IListView;

/**
 * Created by sshss on 2018/2/23.
 */

public class SystemNotiListPresenter extends BaseListPresenter<IListView,SystemNotiBean> {

    public SystemNotiListPresenter(IListView view) {
        super(view);
    }

    @Override
    public void getListData(int pageNum) {
        RequestBean requestBean = new RequestBean();
        requestBean.numPerPage = 15;
        requestBean.pageNum = pageNum;
        getHttpModel().request(API.SYSTEM_NOTI_LIST, requestBean);
    }
}
