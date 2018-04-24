package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.CollectionCatBean;

import java.util.List;

/**
 * Created by sshss on 2017/10/17.
 */

public interface ICollectCatListView extends IView {

    void showCatNameList(List<CollectionCatBean.DataBean> data);
}
