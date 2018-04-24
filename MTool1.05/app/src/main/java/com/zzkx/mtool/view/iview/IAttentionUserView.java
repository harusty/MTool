package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.AttentionUserBean;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public interface IAttentionUserView extends IView {
    void showUserData(List<AttentionUserBean.DataBean> data);

    void showEmpty();
}
