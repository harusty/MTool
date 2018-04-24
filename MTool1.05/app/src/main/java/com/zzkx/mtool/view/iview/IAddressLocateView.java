package com.zzkx.mtool.view.iview;

import android.app.Activity;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * Created by sshss on 2017/9/7.
 */

public interface IAddressLocateView  extends IView{
    void showLocatePb(boolean show);

    void showAddressInfo(List<PoiItem> aMapLocation);

    Activity getContext();
}
