package com.zzkx.mtool.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by sshss on 2017/10/10.
 */

public class HeaderUtil {
    public static void addHeader(Context context, ListView listView, int height) {
        View view = new View(context);
        if (height == 0)
            height = Dip2PxUtils.dip2px(context, 20);
        else
            height =  Dip2PxUtils.dip2px(context, height);
        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        listView.addHeaderView(view);
    }
}
