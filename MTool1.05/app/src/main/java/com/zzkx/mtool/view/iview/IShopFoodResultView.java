package com.zzkx.mtool.view.iview;

import com.amap.api.services.core.PoiItem;
import com.zzkx.mtool.bean.CusMenuListBean;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/9/16.
 */

public interface IShopFoodResultView extends IView{
    void showFoodData(CusMenuListBean bean);

    void showPoiData(ArrayList<PoiItem> poiItems);

    void refreshLocalHistory(String keyword);

    void showEmpty();
}
