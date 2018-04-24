package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.OrderPayResultBean;

/**
 * Created by sshss on 2018/1/3.
 */

public interface IChargeWxView extends IView {
    void showWxChargeInfo(OrderPayResultBean bean);
}
