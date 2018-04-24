package com.zzkx.mtool.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.zzkx.mtool.R;
import com.zzkx.mtool.view.activity.CartActivity;

/**
 * Created by sshss on 2017/9/13.
 */

public class PayFragment extends BaseFragment {
    private AlertDialog mDialog;
    private CartActivity mActivity;


    @Override
    public int getContentRes() {
        return R.layout.fragment_pay;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = (CartActivity) getActivity();
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage(getString(R.string.callingPay));
    }

    @Override
    public void showProgress(boolean toShow) {
        if(toShow)
            mDialog.show();
        else
            mDialog.dismiss();
    }

    @Override
    public void onPageSelected() {
        mDialog.show();
        mActivity.toSubmit();
    }
    public void dismissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void onReload() {

    }

}
