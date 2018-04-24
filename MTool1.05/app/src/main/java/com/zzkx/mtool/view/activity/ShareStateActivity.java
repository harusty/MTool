package com.zzkx.mtool.view.activity;

import android.view.View;

import com.hyphenate.easeui.domain.EaseUser;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ShareStatePresenter;

/**
 * Created by sshss on 2017/9/25.
 */

public class ShareStateActivity extends PublishStateActivity{
    private ShareStatePresenter mShareStatePresenter;

    @Override
    public void initViews() {
        super.initViews();
        setMainTitle("分享到动态");
        mImageContainer.setVisibility(View.GONE);
        mShareStatePresenter = new ShareStatePresenter(this);
        iv_collect.setVisibility(View.GONE);
    }

    @Override
    public void handleSend() {
        RequestBean requestBean = new RequestBean();
        requestBean.share = mComment.getText().toString();
        requestBean.id = getIntent().getStringExtra(Const.ID);
        if (mAtList != null) {
            String atIds = "";
            for (EaseUser user : mAtList) {
                atIds += user.getMtoolId() + ",";
            }
            if (atIds.endsWith(",")) {
                atIds = atIds.substring(0, atIds.length() - 1);
            }
            requestBean.parentId = atIds;
        }

        mShareStatePresenter.publish(requestBean);
    }
}
