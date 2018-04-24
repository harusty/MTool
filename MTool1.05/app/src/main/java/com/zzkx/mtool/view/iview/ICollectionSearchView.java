package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.CollectionSearchBean;

/**
 * Created by sshss on 2017/10/26.
 */

public interface ICollectionSearchView extends IView {
    void showEmpty();

    void showData(CollectionSearchBean bean);
}
