package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2018/1/15.
 */

public class DialogResend extends SimpleDialog implements View.OnClickListener {
    private  OnConfrimListener mListener;

    public DialogResend(Context context, OnConfrimListener onConfrimListener) {
        super(context, R.layout.dialog_resend);
        findViewById(R.id.tv_cacel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        mListener = onConfrimListener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_confirm){
            mListener.onConfirm();
        }
        dismiss();
    }
    public interface OnConfrimListener{
        void onConfirm();
    }
}
