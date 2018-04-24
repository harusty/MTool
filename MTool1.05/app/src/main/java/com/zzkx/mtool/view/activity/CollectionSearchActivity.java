package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.fragment.CollectionSearchFragment;
import com.zzkx.mtool.view.fragment.SearchFragment;

/**
 * Created by sshss on 2017/10/26.
 */

public class CollectionSearchActivity extends SearchActivity {
    @Override
    protected String getHintText() {
        return "输入信息";
    }

    @Override
    protected String getMainTitle() {
        return "搜索收藏";
    }

    @Override
    protected SearchFragment getSecFragment() {
        return null;
    }

    @Override
    protected SearchFragment getFirsFragment() {
        return new CollectionSearchFragment();
    }

    @Override
    public int getSearchType() {
        return Const.SEARCH_TYPE_COLLECTION;
    }
}
