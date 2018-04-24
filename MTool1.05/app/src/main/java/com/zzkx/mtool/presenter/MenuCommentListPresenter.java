package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.MenuCommentListBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IMenuCommentListView;

/**
 * Created by sshss on 2017/10/14.
 */

public class MenuCommentListPresenter extends BaseListPresenter<IMenuCommentListView, MenuCommentListBean> {

    private String mId;

    public MenuCommentListPresenter(IMenuCommentListView view, String id) {
        super(view);
        mId = id;
    }

    @Override
    public void getListData(int pageNum) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.numPerPage = 15;
        requestBean.pageNum = pageNum;
        requestBean.foodInfo = new RequestBean.FoodInfo();
        requestBean.foodInfo.id = mId;
        getHttpModel().request(API.MENU_COMMENT_LIST, requestBean);
    }
}
