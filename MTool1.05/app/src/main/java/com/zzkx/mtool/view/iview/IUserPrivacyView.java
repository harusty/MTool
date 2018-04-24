package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.UserPrivacyBean;

/**
 * Created by sshss on 2017/12/14.
 */

public interface IUserPrivacyView extends IView {
    void showUserPrivacy(UserPrivacyBean bean);

    void showSetResult(BaseBean bean);
}
