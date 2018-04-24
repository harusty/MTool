package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.AttentionShopBean;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public interface IAttentionShopView extends IView {
    void showShopData(List<AttentionShopBean.DataBean> data);

    void showEmpty();
}
