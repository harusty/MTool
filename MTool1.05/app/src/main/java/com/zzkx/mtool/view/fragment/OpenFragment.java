package com.zzkx.mtool.view.fragment;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/8/1.
 */

public class OpenFragment extends BaseFragment {
    @Override
    public int getContentRes() {
        return R.layout.fragment_open;
    }

    @Override
    public void initNet() {

    }

    @Override
    public void initViews() {
        setTitleDisable();

    }

    @Override
    public void onReload() {

    }
}
