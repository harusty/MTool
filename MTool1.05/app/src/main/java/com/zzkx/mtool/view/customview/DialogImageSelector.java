package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2018/1/31.
 */

public class DialogImageSelector extends SimpleDialog {
    public DialogImageSelector(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.dialog_img_select);
        findViewById(R.id.tv_camera).setOnClickListener(clickListener);
        findViewById(R.id.tv_gallery).setOnClickListener(clickListener);
        findViewById(R.id.tv_video).setOnClickListener(clickListener);
        findViewById(R.id.tv_back).setOnClickListener(clickListener);
        findViewById(R.id.tv_confirm).setOnClickListener(clickListener);
        findViewById(R.id.title).setVisibility(View.GONE);
    }
    public void setVideoOptionVisible() {
        findViewById(R.id.tv_video).setVisibility(View.VISIBLE);
    }
}
