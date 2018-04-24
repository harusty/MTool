package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.MysStateGallaryBean;
import com.zzkx.mtool.view.iview.IView;

/**
 * Created by sshss on 2017/10/21.
 */

public interface IMyStateGallaryView extends IView {
    void showMyGallary(MysStateGallaryBean bean);

    void showEmpty();
}
