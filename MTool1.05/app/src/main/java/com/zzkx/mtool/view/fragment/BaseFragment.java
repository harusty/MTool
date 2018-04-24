package com.zzkx.mtool.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.inter.IPageSelect;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableHelper;
import com.zzkx.mtool.view.iview.IBaseFragment;

import butterknife.ButterKnife;

/**
 * Created by sshss on 2017/6/28.
 */

public abstract class BaseFragment extends Fragment implements ScrollableHelper.ScrollableContainer, IBaseFragment, IPageSelect {
    public StateView mStateView;
    public ViewGroup mBaseView;
    private FragmentActivity mHostActivity;

    public abstract int getContentRes();

    public void initNet() {

    }
    public abstract void initViews();

    public abstract void onReload();

    public View getScrollableView() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHostActivity = getActivity();
    }

    public Activity getContext() {
        return mHostActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseView = (ViewGroup) View.inflate(mHostActivity, R.layout.base_layout, null);
        ViewGroup fr_content = (ViewGroup) mBaseView.findViewById(R.id.fr_base_content);
        View contentView = View.inflate(mHostActivity, getContentRes(), null);
        fr_content.addView(contentView);
        mStateView = new StateView(mHostActivity) {
            @Override
            public void onReload() {
                initNet();
            }
        };
        fr_content.addView(mStateView);
        ButterKnife.bind(this, mBaseView);
    }

    /**
     * 标题设置
     *
     * @param title
     */
    public void setMainTitle(String title) {
        ((TextView) mBaseView.findViewById(R.id.tv_main_title)).setText(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViews();
        initNet();
        return mBaseView;
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


    public void setTitleDisable() {
        mBaseView.findViewById(R.id.title_layout).setVisibility(View.GONE);
    }

    //viewpager滚动选择回调

    @Override
    public void onPageSelected() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

