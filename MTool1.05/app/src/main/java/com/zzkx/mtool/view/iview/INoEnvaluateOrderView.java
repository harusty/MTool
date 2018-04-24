package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.OrderListBean;

import java.util.List;

/**
 * Created by sshss on 2017/9/8.
 */

public interface INoEnvaluateOrderView extends IView{
    void showData(List<OrderListBean.DataBean> data);
}
