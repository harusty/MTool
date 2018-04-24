package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zzkx.mtool.R;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.chat.util.ChatPrepareUtil;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/1.
 */

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.welcomeLayout)
    ViewGroup mWelcomeLayout;
    @BindView(R.id.guidLayout)
    ViewGroup mGuidLayout;
    @BindView(R.id.guidPager)
    ViewPager mGuidPager;
    private static final int sleepTime = 2000;

    @Override
    public int getContentRes() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        if (SPUtil.getBoolean(Const.IS_FIRST, true)) {
            mWelcomeLayout.setVisibility(View.INVISIBLE);
            mGuidLayout.setVisibility(View.VISIBLE);
            mGuidPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return 4;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    TextView textView = new TextView(WelcomeActivity.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("欢迎" + position);
                    container.addView(textView);
                    if (position == 3)
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goMain();
                            }
                        });
                    return textView;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });
        } else {
            DemoHelper.getInstance().initHandler(this.getMainLooper());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtil.getBoolean(Const.IS_FIRST, true))
            return;
        ChatPrepareUtil.prepare(sleepTime, this);
    }

    /**
     * get sdk version
     */
    private String getVersion() {
        return EMClient.getInstance().VERSION;
    }

    private void goMain() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//        if (SPUtil.getBoolean(Const.IS_LOGIN, false)) {
//            finish();
//            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//        } else {
//            startActivity(new Intent(this, LoginActivity.class));
//        }
        finish();
    }

    @Override
    public void initNet() {

    }

    @Override
    public void onReload() {

    }
}
