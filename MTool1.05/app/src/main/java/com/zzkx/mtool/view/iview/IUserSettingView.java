package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.UserSettingBean;

/**
 * Created by sshss on 2017/11/22.
 */

public interface IUserSettingView extends IView{
    void showSetting(UserSettingBean bean);

    void showSettingSuccess(UserSettingBean bean);
}
