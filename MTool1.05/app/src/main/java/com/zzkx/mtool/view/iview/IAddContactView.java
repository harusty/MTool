package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.UserSearchBean;
import com.zzkx.mtool.view.iview.IView;

/**
 * Created by sshss on 2017/12/5.
 */

public interface IAddContactView extends IView {
    void showSearchResult(UserSearchBean bean);
}
