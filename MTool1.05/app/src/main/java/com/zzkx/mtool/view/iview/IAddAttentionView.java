package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BaseBean;

/**
 * Created by sshss on 2017/11/22.
 */

public interface IAddAttentionView extends IView {
    void showAttentionAddSuccess(BaseBean bean);
    void showDelAttentionFaild(BaseBean bean);
}
