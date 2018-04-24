package com.zzkx.mtool.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.zzkx.mtool.R;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/12.
 */

public class NewSearchFriendFragment extends BaseFragment {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        
    }

    @Override
    public void onReload() {

    }
}
