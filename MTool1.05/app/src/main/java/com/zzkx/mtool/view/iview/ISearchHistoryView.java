package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.HistoryBean;

import java.util.List;

/**
 * Created by sshss on 2017/9/16.
 */

public interface ISearchHistoryView extends IView {
    void showData(HistoryBean bean);

    void showLoacalCache(List<String> history);
}
