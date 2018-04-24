package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.activity.AccountSafetyActivity;
import com.zzkx.mtool.view.activity.AddressListActivity;
import com.zzkx.mtool.view.activity.ArticleActivity;
import com.zzkx.mtool.view.activity.NormalSettingActivity;
import com.zzkx.mtool.view.activity.NotificationSettingActivity;
import com.zzkx.mtool.view.activity.PrivacySettingActivity;

/**
 * Created by sshss on 2017/9/14.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public int getContentRes() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mBaseView.findViewById(R.id.layout_addInfo).setOnClickListener(this);
        mBaseView.findViewById(R.id.acc_safety).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_notification).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_privacy).setOnClickListener(this);
        mBaseView.findViewById(R.id.layout_about).setOnClickListener(this);
        mBaseView.findViewById(R.id.setting_normal).setOnClickListener(this);
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_addInfo:
                startActivity(new Intent(getContext(), AddressListActivity.class));
                break;
            case R.id.acc_safety:
                startActivity(new Intent(getContext(), AccountSafetyActivity.class));
                break;
            case R.id.layout_privacy:
                startActivity(new Intent(getContext(), PrivacySettingActivity.class));
                break;
            case R.id.layout_notification:
                startActivity(new Intent(getContext(), NotificationSettingActivity.class));
                break;
            case R.id.layout_about:
//                startActivity(new Intent(getContext(), AboutActivity.class));
                startActivity(new Intent(getActivity(), ArticleActivity.class)
                        .putExtra(Const.TITLE, "关于MTool")
                        .putExtra(Const.ID, "115193562969412")
                );
                break;
            case R.id.setting_normal:
                startActivity(new Intent(getActivity(),NormalSettingActivity.class));
                break;
        }
    }
}
