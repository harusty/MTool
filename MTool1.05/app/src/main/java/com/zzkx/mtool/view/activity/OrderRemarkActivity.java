package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.ToastUtils;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/18.
 */

public class OrderRemarkActivity extends BaseActivity {
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.tv_save)
    TextView mTvSave;

    @Override
    public int getContentRes() {
        return R.layout.activity_order_remark;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("订单备注");
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtInput.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showToast("请输入备注内容");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("remark", s);
                intent.putExtra(Const.INDEX, getIntent().getIntExtra(Const.INDEX, 0));
                setResult(Const.RESULT_SUCESS_CODE,intent);
                finish();
            }
        });
    }

    @Override
    public void onReload() {

    }
}
