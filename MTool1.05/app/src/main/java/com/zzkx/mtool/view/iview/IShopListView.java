package com.zzkx.mtool.view.iview;

import com.amap.api.services.cloud.CloudItem;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/8/22.
 */

public interface IShopListView extends IView {
    void showData(ArrayList<CloudItem> cloudItems);

    void setLoadMore(boolean b);

    void setEmpty();
}
