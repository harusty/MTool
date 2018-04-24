package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BlackListBean;

import java.util.List;

/**
 * Created by sshss on 2017/12/11.
 */

public interface IBlackListView extends IView {
    void showBlackList(List<BlackListBean.DataBean> bean);

    void showEmpty();
}
