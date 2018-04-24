package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;

/**
 * Created by sshss on 2018/1/8.
 */

public class DialogQr extends SimpleDialog {
    private ImageView mIVQr;
    private boolean mNoCode;

    public DialogQr(Context context) {
        super(context, R.layout.dialog_qr);
        mIVQr = (ImageView) findViewById(R.id.iv_qr);
        ((TextView)findViewById(R.id.tv_num)).setText("识别码："+SPUtil.getString(Const.USER_PHONE,""));
    }

    public void setImgRes(String path){
        GlideUtil.getInstance().display(mIVQr,path);
    }

    public void setCode(String code) {
        ((TextView)findViewById(R.id.tv_num)).setText(code);
    }
}
