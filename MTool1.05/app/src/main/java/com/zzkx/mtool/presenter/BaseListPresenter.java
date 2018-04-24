package com.zzkx.mtool.presenter;

import android.widget.Toast;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.BaseListBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.view.iview.IListView;

/**
 * Created by sshss on 2017/6/28.
 */

public abstract class BaseListPresenter<E extends IListView, T extends BaseListBean> extends BasePresenter<E, T> {
    private int pageNum = -1;

    public abstract void getListData(int pageNum);

    public BaseListPresenter(E view) {
        super(view);
    }

    public void refreshData() {
        pageNum = 1;
        getListData(pageNum);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void loadMoreData() {
        getListData(pageNum);
    }

    @Override
    public void onSuccessM(T bean) {
        getView().showRefreshComplete();
        if (bean.status == 1) {
            pageNum = bean.page.plainPageNum + 1;
            getView().showList(bean);
        } else {
            handleError(new ErrorBean(bean.msg, null));
        }
    }

    @Override
    public void onConnectFaildM(ErrorBean errorBean) {
        handleError(errorBean);
    }

    @Override
    public void onResponseErrorM(ErrorBean errorBean) {
        handleError(errorBean);
    }

    private void handleError(ErrorBean errorBean) {
        if (pageNum == 1) {
            getView().showError(errorBean);
        } else {
            Toast.makeText(MyApplication.getContext(), errorBean.msg, Toast.LENGTH_SHORT).show();
            getView().showReload();
        }
    }
}