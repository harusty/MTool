package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;

import com.zzkx.mtool.R;

/**
 * Created by sshss on 2017/10/18.
 */

public class DialogCollectionMore extends SimpleDialog {

    public DialogCollectionMore(Context context, View.OnClickListener clickListener) {
        super(context, R.layout.dialog_collect_more);
        getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        getView().findViewById(R.id.layout_1).setOnClickListener(clickListener);
        getView().findViewById(R.id.layout_2).setOnClickListener(clickListener);
        getView().findViewById(R.id.layout_3).setOnClickListener(clickListener);
    }


    public void show(Object item) {
        super.show();
        getView().findViewById(R.id.layout_1).setTag(item);
        getView().findViewById(R.id.layout_2).setTag(item);
        getView().findViewById(R.id.layout_3).setTag(item);
    }
}
