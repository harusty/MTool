package com.zzkx.mtool.view.activity;

import android.view.View;

import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.DemoModel;
import com.zzkx.mtool.chat.util.ChatNotifySoundSettingUtil;
import com.zzkx.mtool.view.customview.CustomSwitch;

import butterknife.BindView;

/**
 * Created by sshss on 2017/10/30.
 */

public class NotificationSettingActivity extends BaseActivity implements CustomSwitch.OnSwitchChangeListener {
    @BindView(R.id.layout_voice_vivrate)
    View mLayoutVoiceVivrate;
    private DemoModel mDemoModel;
    private CustomSwitch sw_silent_mode;
    private CustomSwitch sw_rec_new_msg;
    private CustomSwitch sw_voice;
    private CustomSwitch sw_vibrate;
    private CustomSwitch sw_show_message;

    @Override
    public int getContentRes() {
        return R.layout.activity_notification_setting;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("新消息提醒");
        sw_silent_mode = ((CustomSwitch) findViewById(R.id.sw_silent_mode));
        sw_silent_mode.setOnSwitchChangeListener(this);
        sw_rec_new_msg = ((CustomSwitch) findViewById(R.id.sw_rec_new_msg));
        sw_rec_new_msg.setOnSwitchChangeListener(this);
        sw_voice = ((CustomSwitch) findViewById(R.id.sw_voice));
        sw_voice.setOnSwitchChangeListener(this);
        sw_vibrate = ((CustomSwitch) findViewById(R.id.sw_vibrate));
        sw_vibrate.setOnSwitchChangeListener(this);
        sw_show_message = ((CustomSwitch) findViewById(R.id.sw_show_message));
        sw_show_message.setOnSwitchChangeListener(this);


        mDemoModel = DemoHelper.getInstance().getModel();
        sw_silent_mode.setSwitch(ChatNotifySoundSettingUtil.getSilentMode());
        sw_rec_new_msg.setSwitch(mDemoModel.getSettingMsgNotification());
        sw_voice.setSwitch(mDemoModel.getSettingMsgSound());
        sw_vibrate.setSwitch(mDemoModel.getSettingMsgVibrate());
        sw_show_message.setSwitch(ChatNotifySoundSettingUtil.getShowMessageInfo());

        if (!mDemoModel.getSettingMsgNotification()) {
            mLayoutVoiceVivrate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onChange(CustomSwitch customSwitch, boolean change) {
        switch (customSwitch.getId()) {
            case R.id.sw_silent_mode:
               ChatNotifySoundSettingUtil.setSilentMode(change);
                break;
            case R.id.sw_rec_new_msg:
                mDemoModel.setSettingMsgNotification(change);
                if (change) {
                    mLayoutVoiceVivrate.setVisibility(View.VISIBLE);
                } else {
                    mLayoutVoiceVivrate.setVisibility(View.GONE);
                }
                break;
            case R.id.sw_show_message:
                ChatNotifySoundSettingUtil.setShowMessageInfo(change);
                break;
            case R.id.sw_voice:
                mDemoModel.setSettingMsgSound(change);
                break;
            case R.id.sw_vibrate:
                mDemoModel.setSettingMsgVibrate(change);
                break;
        }
    }
}
