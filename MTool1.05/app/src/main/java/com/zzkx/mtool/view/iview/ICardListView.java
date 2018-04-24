package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BankCardBean;

/**
 * Created by sshss on 2018/1/4.
 */

public interface ICardListView extends IView {
    void showCardList(BankCardBean bean);

    void showDefCardInfo(BankCardBean.Data dataBean);
}
