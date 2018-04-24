package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.AddressListBean;

import java.util.List;

/**
 * Created by sshss on 2017/9/7.
 */

public interface IAddListView extends IView  {
    void showAddList(List<AddressListBean.AddressBean> data);

}
