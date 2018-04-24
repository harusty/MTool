package com.zzkx.mtool.util;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.easeui.EaseConstant;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.activity.MineActivity;
import com.zzkx.mtool.view.activity.UserDetailActivity;

/**
 * Created by sshss on 2017/12/20.
 */

public class HeadClickUtil {

    public static void handleClick(Context context, String userId, String hxId) {
        if (userId != null) {
            String uid = SPUtil.getString(Const.U_ID, "");
            if (userId.equals(uid)) {
                context.startActivity(new Intent(context, MineActivity.class));
            } else {
                context.startActivity(new Intent(context, UserDetailActivity.class).putExtra(Const.ID, userId));
            }
        } else if (hxId != null) {
            String myHxid = SPUtil.getString(Const.HX_ID, "");
            if (hxId.equals(myHxid)) {
                context.startActivity(new Intent(context, MineActivity.class));
            } else {
                context.startActivity(new Intent(context, UserDetailActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, hxId));
            }
        } else {
            ToastUtils.showToast("?");
        }
    }
}
