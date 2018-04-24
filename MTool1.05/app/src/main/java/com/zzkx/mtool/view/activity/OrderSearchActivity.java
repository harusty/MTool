package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.fragment.OrderSearchFragment;
import com.zzkx.mtool.view.fragment.SearchFragment;

/**
 * Created by sshss on 2017/10/25.
 */

public class OrderSearchActivity extends SearchActivity {
    @Override
    protected String getHintText() {
        return "订单号/店铺";
    }

    @Override
    protected String getMainTitle() {
        return "搜索订单";
    }

    @Override
    protected SearchFragment getSecFragment() {
        return null;
    }

    @Override
    protected SearchFragment getFirsFragment() {
        return new OrderSearchFragment();
    }

    @Override
    public int getSearchType() {
        return Const.SEARCH_TYPE_ORDER;
    }
}
