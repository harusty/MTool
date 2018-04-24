package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.TagMemberBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.ITagMemberListView;

/**
 * Created by sshss on 2017/12/4.
 */

public class TagMemberListPresetner extends BasePresenter<ITagMemberListView, TagMemberBean> {

    public TagMemberListPresetner(ITagMemberListView view) {
        super(view);
    }

    @Override
    public void onSuccessM(TagMemberBean bean) {
        getView().showProgress(false);
        getView().showMemberList(bean);
    }

    public void getMemberList(String tagId) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.labelId = tagId;
        getHttpModel().request(API.TAG_MEMBER_LIST, requestBean);
    }
}
