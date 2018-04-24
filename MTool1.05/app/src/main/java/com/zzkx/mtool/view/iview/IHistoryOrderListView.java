package com.zzkx.mtool.view.iview;

import android.content.Context;

import com.zzkx.mtool.bean.HistoryOrderBean;

import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public interface IHistoryOrderListView extends IView {
    void showData(List<HistoryOrderBean.DataBean> data);

    Context getContext();
}
