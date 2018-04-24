package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IAddTagMemberView;

import java.util.List;

/**
 * Created by sshss on 2017/12/4.
 */

public class AddTagMemberPresenter extends BasePresenter<IAddTagMemberView, BaseBean> {
    public AddTagMemberPresenter(IAddTagMemberView view) {
        super(view);
    }

    @Override
    public void onSuccessM(BaseBean bean) {
        getView().showProgress(false);
        getView().showAddResult(bean);
    }

    public void addMember(String tagId, List<String> ids) {
        getView().showProgress(false);
        RequestBean requestBean = new RequestBean();
        requestBean.labelId = tagId;
        requestBean.hxUsernames = ids;
        getHttpModel().request(API.ADD_TAG_MEMBER, requestBean);
    }
}
