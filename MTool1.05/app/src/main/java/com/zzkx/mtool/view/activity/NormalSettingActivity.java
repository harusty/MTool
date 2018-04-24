package com.zzkx.mtool.view.activity;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.view.customview.CustomSwitch;

import butterknife.BindView;

/**
 * Created by sshss on 2018/3/19.
 */

public class NormalSettingActivity extends BaseActivity {
    @BindView(R.id.sw1)
    CustomSwitch sw1;

    @Override
    public int getContentRes() {
        return R.layout.activity_normal_setting;
    }


    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("通用");
        sw1.setSwitch(SPUtil.getBoolean(Const.LEFT_MODE, false));
        sw1.setOnSwitchChangeListener(new CustomSwitch.OnSwitchChangeListener() {
            @Override
            public void onChange(CustomSwitch customSwitch, boolean change) {
                SPUtil.putBoolean(Const.LEFT_MODE, change);
                changeLeft();
            }
        });
    }

    @Override
    public void onReload() {

    }
}
