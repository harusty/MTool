package com.zzkx.mtool.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.StatusBarUtil;
import com.zzkx.mtool.view.customview.MainMenu;
import com.zzkx.mtool.view.customview.StateView;

import butterknife.ButterKnife;


/**
 * Created by sshss on 2017/6/23.
 */

public abstract class BaseActivity extends FragmentActivity {
    public StateView mStateView;
    private ViewGroup mBaseView;
    public MainMenu mMainMenu;
    private View ic_title_check;
    private View ic_left2;
    private View ic_left;

    public abstract int getContentRes();

    public abstract void initViews();

    public void initNet() {

    }

    public abstract void onReload();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        mBaseView = (ViewGroup) View.inflate(this, R.layout.base_layout, null);
        ic_title_check = mBaseView.findViewById(R.id.ic_title_check);
        ic_left2 = mBaseView.findViewById(R.id.ic_left2);
        ic_left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishAct();
            }
        });
        ic_left = mBaseView.findViewById(R.id.ic_left);
        mBaseView.findViewById(R.id.ic_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishAct();
            }
        });
        ViewGroup fr_content = (ViewGroup) mBaseView.findViewById(R.id.fr_base_content);
        View contentView = View.inflate(this, getContentRes(), null);
        fr_content.addView(contentView);
        mStateView = new StateView(this) {
            @Override
            public void onReload() {
                BaseActivity.this.onReload();
            }
        };
        fr_content.addView(mStateView);
        setContentView(mBaseView);
        StatusBarUtil.setStatuBarColor(this, Color.BLACK, 0);
        ButterKnife.bind(this);
        initViews();
        initNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeLeft();
    }

    protected void changeLeft() {
        if (SPUtil.getBoolean(Const.LEFT_MODE, false)) {
            ic_left.setVisibility(View.GONE);
            ic_left2.setVisibility(View.VISIBLE);
        } else {
            ic_left.setVisibility(View.VISIBLE);
            ic_left2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mMainMenu != null && mMainMenu.isShow()) {
            mMainMenu.dismiss(true);
        } else {
            super.onBackPressed();
        }
    }

    public void setTitleCheckVisible(int visible) {
        ic_title_check.setVisibility(visible);
    }

    /**
     * 默认实现返回关闭activity
     */
    public void onFinishAct() {
        finish();
    }

    /**
     * 标题设置
     *
     * @param title
     */
    public void setMainTitle(String title) {
        ((TextView) mBaseView.findViewById(R.id.tv_main_title)).setText(title);
    }

    public void setMainTitleGone() {
        mBaseView.findViewById(R.id.tv_main_title).setVisibility(View.INVISIBLE);
    }

    /**
     * 是否添加主导航按钮
     */
    public void setMainMenuEnable() {
        mMainMenu = new MainMenu(this);
//        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        mBaseView.addView(mMainMenu);
    }

    public void setCustomMenuButton(View view) {
        if (mMainMenu != null)
            mMainMenu.setCustomMenuButton(view);
    }

    public void setMainMenuEnable(MainMenu menu) {
        mMainMenu = menu;
    }

    /**
     * 设置主导航菜单二级菜单
     *
     * @param imgRes
     * @param titles
     * @param listener
     */
    public void setSecMenu(int[] imgRes, String[] titles, View.OnClickListener listener) {
        if (mMainMenu != null)
            mMainMenu.setSecondMenu(imgRes, titles, listener);
    }

    public void setTitleDisable() {
//        mBaseView.findViewById(R.id.title_layout).setVisibility(View.GONE);
        mBaseView.removeView(mBaseView.findViewById(R.id.title_layout));
    }

    public StateView getStateView() {
        return mStateView;
    }

    public void showProgress(boolean toShow) {
        if (toShow)
            mStateView.setCurrentState(StateView.ResultState.LOADING);
        else
            mStateView.setCurrentState(StateView.ResultState.SUCESS);
    }

    public void showError(ErrorBean errorBean) {
        mStateView.setCurrentState(StateView.ResultState.ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeAvctivity(this);
    }
}
