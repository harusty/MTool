package com.zzkx.mtool.chat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.Json_U;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.view.activity.MenuDetailActivity;
import com.zzkx.mtool.view.activity.ShopActivity;
import com.zzkx.mtool.view.activity.StateDetailActivity;

/**
 * Created by sshss on 2018/1/10.
 */

public class ChatRowShare extends EaseChatRow {

    private ImageView iv_share;
    private TextView tv_top;
    private TextView tv_bottom;
    private TextView tv_share_type;

    public ChatRowShare(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.row_receive_share : R.layout.row_sent_share, this);
    }

    @Override
    protected void onFindViewById() {
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_share_type = (TextView) findViewById(R.id.tv_share_type);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        String stringAttribute = message.getStringAttribute(Const.SHARE_INFO, null);
        if (!TextUtils.isEmpty(stringAttribute)) {
            ChatShareBean shareBean = Json_U.fromJson(stringAttribute, ChatShareBean.class);
            if (TextUtils.isEmpty(shareBean.picUrl))
                iv_share.setImageResource(R.mipmap.ic_logo);
            else
                GlideUtil.getInstance().display(iv_share, shareBean.picUrl);
            tv_top.setText(shareBean.title);
            tv_bottom.setText(shareBean.content);
            switch (shareBean.type) {
                case 0:
                    tv_share_type.setText("个人名片");
                    break;
                case 1:
                    tv_share_type.setText("分享动态");
                    break;
                case 2:
                    tv_share_type.setText("分享店铺");
                    break;
                case 3:
                    tv_share_type.setText("分享商品");
                    break;
            }
        }
    }

    @Override
    protected void onBubbleClick() {
        String stringAttribute = message.getStringAttribute(Const.SHARE_INFO, null);
        if (stringAttribute != null) {
            ChatShareBean shareBean = Json_U.fromJson(stringAttribute, ChatShareBean.class);
            switch (shareBean.type) {
                case 0:
                    HeadClickUtil.handleClick(getContext(), null, shareBean.id);
                    break;
                case 1:
                    getContext().startActivity(new Intent(getContext(), StateDetailActivity.class)
                            .putExtra(Const.ID, shareBean.id));
                    break;
                case 2:
                    getContext().startActivity(new Intent(getContext(), ShopActivity.class)
                            .putExtra(Const.ID, shareBean.id));
                    break;
                case 3:
                    getContext().startActivity(new Intent(getContext(), MenuDetailActivity.class)
                            .putExtra(Const.ID, shareBean.id));
                    break;

            }
        }
    }
}
