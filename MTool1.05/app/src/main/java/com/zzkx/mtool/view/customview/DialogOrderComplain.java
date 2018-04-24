package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2018/1/17.
 */

public class DialogOrderComplain extends SimpleDialog implements View.OnClickListener {

    private EditText mEtInput;
    private DialogResend.OnConfrimListener mListener;

    public DialogOrderComplain(Context context, DialogResend.OnConfrimListener listener) {
        super(context, R.layout.dialog_order_complain);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mListener = listener;
    }

    public String getContent(){
        return mEtInput.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                mListener.onConfirm();
                break;
        }
        dismiss();
    }
}
