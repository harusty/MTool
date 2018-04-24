package com.zzkx.mtool.view.iview;

import android.content.Context;

import com.zzkx.mtool.bean.SearchContactBean;

/**
 * Created by sshss on 2017/11/20.
 */

public interface IContactSearchView extends IView {
    void showData(SearchContactBean bean);

    void showEmpty();

    Context getContext();

}
