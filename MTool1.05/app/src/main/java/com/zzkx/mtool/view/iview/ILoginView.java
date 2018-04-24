package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.LoginBean;

/**
 * Created by sshss on 2017/6/23.
 */

public interface ILoginView extends IView {
    void showLoginSuccess(LoginBean bean);
    void showLoginFaild(String msg);
}
