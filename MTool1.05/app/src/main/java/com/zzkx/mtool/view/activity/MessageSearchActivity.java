package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.fragment.ContactSearchFragment;
import com.zzkx.mtool.view.fragment.MessageSearchFragment;
import com.zzkx.mtool.view.fragment.SearchFragment;

/**
 * Created by sshss on 2017/11/15.
 */

public class MessageSearchActivity extends SearchActivity {

    @Override
    protected String getHintText() {
        return "请输入消息/联系人";
    }

    @Override
    protected String getMainTitle() {
        return "搜    索";
    }

    @Override
    protected SearchFragment getSecFragment() {
        return new ContactSearchFragment();
    }

    @Override
    protected SearchFragment getFirsFragment() {
        return new MessageSearchFragment();
    }

    @Override
    public int getSearchType() {
        return Const.SEARCH_TYPE_MSGFRIEND;
    }

    @Override
    public String getCat1Title() {
        return "消  息";
    }

    @Override
    public String getCat2Title() {
        return "联系人";
    }
}
