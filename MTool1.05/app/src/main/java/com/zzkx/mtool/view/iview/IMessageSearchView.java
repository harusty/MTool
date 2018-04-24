package com.zzkx.mtool.view.iview;

import android.app.Activity;

import java.util.List;

/**
 * Created by sshss on 2017/11/15.
 */

public interface IMessageSearchView extends IView {
    void showSearchResult(List<Object> searchResult);

    Activity getActivity();

    void showEmpty();
}
