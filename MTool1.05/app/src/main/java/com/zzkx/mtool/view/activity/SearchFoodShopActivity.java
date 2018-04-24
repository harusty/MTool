package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.fragment.PoiResultListFragment;
import com.zzkx.mtool.view.fragment.SearchFragment;
import com.zzkx.mtool.view.fragment.ShopFoodResultListFragment;

/**
 * Created by sshss on 2017/9/15.
 */

public class SearchFoodShopActivity extends SearchActivity {

    @Override
    protected String getHintText() {
        return getString(R.string.searchHint);
    }

    @Override
    protected String getMainTitle() {
        return getString(R.string.search);
    }

    @Override
    protected SearchFragment getSecFragment() {
        return new ShopFoodResultListFragment();
    }

    @Override
    protected SearchFragment getFirsFragment() {
        return new PoiResultListFragment();
    }

    @Override
    public int getSearchType() {
        return Const.SEARCH_TYPE_FOODSHOP;
    }

    @Override
    public String getCat1Title() {
        return "商圈/地址";
    }

    @Override
    public String getCat2Title() {
        return "商品";
    }

}
