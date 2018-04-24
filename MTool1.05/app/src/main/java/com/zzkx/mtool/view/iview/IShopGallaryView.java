package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.ShopGallaryBean;
import com.zzkx.mtool.bean.StateListBean;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/10/16.
 */

public interface IShopGallaryView extends IView {
    void showData(ArrayList<StateListBean.ResData> bean);
}
