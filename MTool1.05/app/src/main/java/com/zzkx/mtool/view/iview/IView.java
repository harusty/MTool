package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.ErrorBean;

/**
 * Created by sshss on 2017/6/23.
 * 大神对多异常通过具体的code去区分展示哪一种错误界面，但是以以往公司项目来看，没必要
 */

public interface IView {
    void showProgress(boolean toShow);
    void showError(ErrorBean errorBean);
}
