package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.ShopComentReplyBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ShopCommentReplyView;

/**
 * Created by sshss on 2018/1/23.
 */

public class ShopCommentReplyListPresenter extends BaseListPresenter<ShopCommentReplyView, ShopComentReplyBean> {
    private String mCommentId;

    public ShopCommentReplyListPresenter(ShopCommentReplyView view, String commentId) {
        super(view);
        mCommentId = commentId;
    }

    @Override
    public void getListData(int pageNum) {
        RequestBean requestBean = new RequestBean();
        requestBean.merchantRestaurantsCommentReply = new RequestBean();
        requestBean.merchantRestaurantsCommentReply.commentId = mCommentId;
        requestBean.numPerPage = 10;
        requestBean.pageNum = pageNum;
        getHttpModel().request(API.SHOP_COMMENT_REPLY_LIST,requestBean);
    }
}
