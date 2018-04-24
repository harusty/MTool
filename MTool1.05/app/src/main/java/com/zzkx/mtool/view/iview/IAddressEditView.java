package com.zzkx.mtool.view.iview;

import android.app.Activity;

import com.zzkx.mtool.bean.BaseBean;

/**
 * Created by sshss on 2017/9/8.
 */

public interface IAddressEditView extends IView {
    void showAdd(boolean isSuccess);

    Activity getContext();

    void showDelete(BaseBean baseBean);
}
