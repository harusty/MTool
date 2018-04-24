package com.zzkx.mtool.view.activity;

import android.widget.GridView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateDetailBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.adapter.SupportAdapter;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/27.
 */

public class AllSupportedUserActivity extends BaseActivity {
    @BindView(R.id.grid)
    GridView mGridView;

    @Override
    public int getContentRes() {
        return R.layout.activity_all_supported_user;
    }

    @Override
    public void initViews() {
        setMainTitle("攒过的人");
        setMainMenuEnable();
        StateDetailBean.DataBean serializableExtra = (StateDetailBean.DataBean) getIntent().getSerializableExtra(Const.OBJ);
        mGridView.setAdapter(new SupportAdapter(serializableExtra.userMemberDos));
    }

    @Override
    public void onReload() {

    }
}
