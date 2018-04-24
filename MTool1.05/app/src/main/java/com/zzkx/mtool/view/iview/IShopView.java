package com.zzkx.mtool.view.iview;

import android.app.Activity;
import android.view.View;

import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;
import com.zzkx.mtool.imple.BottomCartCtrlListener;

/**
 * Created by sshss on 2017/8/24.
 */

public interface IShopView extends IView {
    void shoMenulist(CusMenuListBean bean, View.OnClickListener onClickListener);
    Activity getContext();

    void notifyDataSetChanged();

    void showOption(MenuListBean.FoodInfoListBean bean, MenuListBean.DataBean dataBean);

    void showCartInfo(int count, double price,int type);
    void setBottomCtrlListener(BottomCartCtrlListener listener,int type);
}
