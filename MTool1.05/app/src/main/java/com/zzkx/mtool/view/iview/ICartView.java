package com.zzkx.mtool.view.iview;

import android.view.View;

import com.zzkx.mtool.bean.CusMenuListBean;
import com.zzkx.mtool.bean.MenuListBean;

import java.util.List;

/**
 * Created by sshss on 2017/9/2.
 */

public interface ICartView extends IView {
    void showCartInfo(CusMenuListBean bean, View.OnClickListener optionCtrlListener);

    void notifyDataSetChanged();

    void showOption(MenuListBean.FoodInfoListBean bean, MenuListBean.DataBean dataBean, boolean b);

    void showTopOrderInfo(double cusTotalPrice, int size, int type);

    void recordData(List<MenuListBean.DataBean> headerList,int type);
    void setDataLoded();
}
