package com.zzkx.mtool.view.activity;

import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.UserPrivacyBean;
import com.zzkx.mtool.presenter.SearchUserPrivacyPresenter;
import com.zzkx.mtool.presenter.SettingUserPrivacyPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.customview.CustomSwitch;
import com.zzkx.mtool.view.iview.IUserPrivacyView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/30.
 */

public class PrivacySettingActivity extends BaseActivity implements IUserPrivacyView, CustomSwitch.OnSwitchChangeListener {
    private SearchUserPrivacyPresenter mSearchUserPrivacyPresenter;
    @BindView(R.id.sw_attention_fan)
    CustomSwitch mAutoAttention;
    @BindView(R.id.sw_fan_talk)
    CustomSwitch mEnableTalk;
    @BindView(R.id.sw_disable_show_to_stranger)
    CustomSwitch mDisShowToStr;
    @BindView(R.id.sw_enable_show_state_10)
    CustomSwitch mEnShowToStr10;
    @BindView(R.id.sw_disable_show_to_fan)
    CustomSwitch mDisShowToFan;
    @BindView(R.id.sw_acynk_order_comment)
    CustomSwitch mAsyncOrderComment;
    private SettingUserPrivacyPresenter mSettingUserPrivacyPresenter;


    @Override
    public int getContentRes() {
        return R.layout.activity_privacy_setting;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("隐私设置");
        mSearchUserPrivacyPresenter = new SearchUserPrivacyPresenter(this);
        mSettingUserPrivacyPresenter = new SettingUserPrivacyPresenter(this);
        mAutoAttention.setOnSwitchChangeListener(this);
        mEnableTalk.setOnSwitchChangeListener(this);
        mDisShowToStr.setOnSwitchChangeListener(this);
        mEnShowToStr10.setOnSwitchChangeListener(this);
        mDisShowToFan.setOnSwitchChangeListener(this);
        mAsyncOrderComment.setOnSwitchChangeListener(this);
    }

    @Override
    public void initNet() {
        mSearchUserPrivacyPresenter.searchUserPrivacy();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showUserPrivacy(UserPrivacyBean bean) {
        UserPrivacyBean.DataBean data = bean.data;
        if (data != null) {

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomSwitch customSwitch = (CustomSwitch) v;
                    onChange(customSwitch, !customSwitch.isSwitchOn());
                }
            };
            mAutoAttention.setOnClickListener(clickListener);
            mEnableTalk.setOnClickListener(clickListener);
            mDisShowToStr.setOnClickListener(clickListener);
            mEnShowToStr10.setOnClickListener(clickListener);
            mDisShowToFan.setOnClickListener(clickListener);
            mAsyncOrderComment.setOnClickListener(clickListener);

            mAutoAttention.setSwitch(data.autoAddFans == 1, true);
            mEnableTalk.setSwitch(data.passivityChat == 1, true);
            mDisShowToStr.setSwitch(data.strangerSeeStatus == 0, true);
            mEnShowToStr10.setSwitch(false, true);
            mDisShowToFan.setSwitch(data.fansSeeStatus == 0, true);
            mAsyncOrderComment.setSwitch(data.togetherStatus == 1, true);
        }
    }

    @Override
    public void showSetResult(BaseBean bean) {
        if (bean.status == 1) {
            if (mLastRequest.autoAddFans != null) {
                mAutoAttention.setSwitch(mLastRequest.autoAddFans == 1, true);
            } else if (mLastRequest.passivityChat != null) {
                mEnableTalk.setSwitch(mLastRequest.passivityChat == 1, true);
            } else if (mLastRequest.strangerSeeStatus != null) {
                mDisShowToStr.setSwitch(mLastRequest.strangerSeeStatus == 0, true);
            } else if (mLastRequest.fansSeeStatus != null) {
                mDisShowToFan.setSwitch(mLastRequest.fansSeeStatus == 0, true);
            } else if (mLastRequest.togetherStatus != null) {
                mAsyncOrderComment.setSwitch(mLastRequest.togetherStatus == 1, true);
            } else if (mLastRequest.tmp10 != null) {
                mEnShowToStr10.setSwitch(mLastRequest.tmp10 == 1, true);
            }
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    private RequestBean mLastRequest;

    @Override
    public void onChange(CustomSwitch customSwitch, boolean change) {
        RequestBean requestBean = new RequestBean();
        requestBean.userMemberSeting = new RequestBean();
        mLastRequest = requestBean.userMemberSeting;
        switch (customSwitch.getId()) {
            case R.id.sw_attention_fan:
                mLastRequest.autoAddFans = change ? 1 : 0;
                break;
            case R.id.sw_fan_talk:
                mLastRequest.passivityChat = change ? 1 : 0;
                break;
            case R.id.sw_disable_show_to_stranger:
                mLastRequest.strangerSeeStatus = change ? 0 : 1;
                break;
            case R.id.sw_enable_show_state_10:
                mLastRequest.tmp10 = change ? 1 : 0;
                break;
            case R.id.sw_disable_show_to_fan:
                mLastRequest.fansSeeStatus = change ? 0 : 1;
                break;
            case R.id.sw_acynk_order_comment:
                mLastRequest.togetherStatus = change ? 1 : 0;
                break;
        }
        mSettingUserPrivacyPresenter.setPrivacy(requestBean);
    }
}
