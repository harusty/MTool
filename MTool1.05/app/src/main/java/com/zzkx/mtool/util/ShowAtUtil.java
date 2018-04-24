package com.zzkx.mtool.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zzkx.mtool.MToolConfig;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateListBean;

import java.util.List;

/**
 * Created by sshss on 2018/1/25.
 */

public class ShowAtUtil {
    public static void handleAtUsers(TextView tv_content, String content, List<StateListBean.UserMemberBean> userMemberList) {
        if (!TextUtils.isEmpty(content)) {
            String[] split = content.split(" ");
            if (split.length > 1) {
                SpannableString ss = new SpannableString(content);
                int fromIndex = 0;
                for (String str : split) {
                    if (str.startsWith("@") && str.length() - 1 < MToolConfig.MAX_NICK_LEN) {
                        int i = content.indexOf(str, fromIndex);
                        ss.setSpan(new ForegroundColorSpan(MyApplication.getContext().getResources().getColor(R.color.blue))
                                , i, i + str.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        fromIndex = i + str.length() - 1;
                    }
                }
                tv_content.setText(ss);
                return;
            }
        }
        tv_content.setText(content);
    }
}
