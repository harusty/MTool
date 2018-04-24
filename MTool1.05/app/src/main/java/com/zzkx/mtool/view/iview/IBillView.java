package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BillBean;

import java.util.List;

/**
 * Created by sshss on 2017/12/19.
 */

public interface IBillView extends IView {
    void shoBillList(List<BillBean.DataBean> data);
}
