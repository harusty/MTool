package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.StateListBean;

import java.util.List;

/**
 * Created by sshss on 2017/10/24.
 */

public interface IStateSearcView extends IView {
    void showData(List<StateListBean.DataBean> data);
}
