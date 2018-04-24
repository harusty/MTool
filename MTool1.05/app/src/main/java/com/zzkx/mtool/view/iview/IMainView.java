package com.zzkx.mtool.view.iview;

import android.support.v4.view.ViewPager;

/**
 * Created by sshss on 2017/8/22.
 */

public interface IMainView extends IView {
    void showLocatePb(boolean show);

    String getKeyWord();

    ViewPager getViewPager();

    void setCurCity(String city);
}
