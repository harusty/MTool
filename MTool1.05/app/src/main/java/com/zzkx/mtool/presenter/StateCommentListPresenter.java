package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.CommentListBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ICommentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/9/27.
 */

public class StateCommentListPresenter extends BasePresenter<ICommentListView, CommentListBean> {

    private List<Object> mData = new ArrayList<>();
    private String mId;
    private boolean isGettingTop;

    public StateCommentListPresenter(ICommentListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(CommentListBean bean) {
        if (isGettingTop) {
            isGettingTop = false;
            getNormalCommentData(mId);
        } else {
            getView().showProgress(false);
            ArrayList<Object> objects = new ArrayList<>();
            objects.addAll(mData);
            getView().showData(objects);
        }
    }

    public void getCommentData(String id) {
        getView().showProgress(true);
        isGettingTop = true;
        mId = id;
        mData.clear();
        RequestBean requestBean = new RequestBean();
        requestBean.postId = id;
        getHttpModel().request(API.TOP_STATE_COMMENTS, requestBean);
    }

    private void getNormalCommentData(String id) {
        RequestBean requestBean = new RequestBean();
        requestBean.forumPostComment = new RequestBean();
        requestBean.forumPostComment.id = id;
        getHttpModel().request(API.STATE_COMMENT_LIST, requestBean);
    }

    @Override
    public void onSuccessWorkThread(CommentListBean bean) {
        List<CommentListBean.DataBean> data = bean.data;
        if (data != null && data.size() > 0) {
            mData.remove("评论");
            if (isGettingTop) {
                mData.add("精选评论");
            } else {
                mData.add("评论");
            }
            mData.addAll(data);
        }
    }
}
