package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BaseListBean;

/**
 * Created by sshss on 2017/6/29.
 */

public interface IListView extends IView {
    void showRefreshComplete();

    void showList(BaseListBean baseListBean);

    void showEmpty();

    void showReload();
}
