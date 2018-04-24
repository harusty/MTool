package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.fragment.SearchFragment;
import com.zzkx.mtool.view.fragment.StateSearchFragment;

/**
 * Created by sshss on 2017/10/16.
 */

public class StateSearchActivity extends SearchActivity {


    @Override
    protected String getHintText() {
        return "输入信息";
    }

    @Override
    protected String getMainTitle() {
        if (getIntent().getBooleanExtra(Const.FLAG, false))
            return "搜索赞过";
        else
            return "搜索动态";
    }

    @Override
    protected SearchFragment getSecFragment() {
        return null;
    }

    @Override
    protected SearchFragment getFirsFragment() {
        StateSearchFragment stateSearchFragment = new StateSearchFragment();
        stateSearchFragment.setArguments(getIntent().getExtras());
        return stateSearchFragment;
    }

    @Override
    public int getSearchType() {
        if (getIntent().getBooleanExtra(Const.FLAG, false))
            return Const.SEARCH_TYPE_SUPPORTED;
        else
            return Const.SEARCH_TYPE_STATE;
    }
}
