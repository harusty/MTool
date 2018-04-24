package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.OrderPayResultBean;

/**
 * Created by sshss on 2017/9/5.
 */

public interface IOrderConfrimView extends IAddListView {

    void showPay(OrderPayResultBean bean);

    void showParentProgress(boolean b);

    void showPayInfoError();
}
