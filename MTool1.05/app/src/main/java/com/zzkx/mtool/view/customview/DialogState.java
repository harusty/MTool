package com.zzkx.mtool.view.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.ScreenUtils;
import com.zzkx.mtool.util.ShareMethodUtils;
import com.zzkx.mtool.view.activity.ShareStateActivity;


/**
 * Created by sshss on 2017/9/30.
 */

public class DialogState {
    private final View mCollectView;
    private Context mContext;
    private View mLoCenter;
    private View mDelete;
    private ImageView mCollectImage;
    private TextView mTvCollect;
    private View mDialogView;
    private int mSize;
    private View.OnClickListener mListener;
    private AlertDialog alertDialog;

    public DialogState(Context context, View.OnClickListener listener) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        mDialogView = View.inflate(context, R.layout.dialog_state_more, null);
        mSize = getSize(context);
        mListener = listener;
        resetView(R.id.layout_share_mtool);
        resetView(R.id.layout_share_mtool_friend);
        resetView(R.id.layout_share_wechat);
        resetView(R.id.layout_share_wechat_circle);
        resetView(R.id.layout_share_qq);
        resetView(R.id.layout_share_qq_space);
        resetView(R.id.layout_share_weibo);
        mDialogView.findViewById(R.id.jubao).setOnClickListener(mListener);
        mCollectView = mDialogView.findViewById(R.id.collect);
        mCollectView.setOnClickListener(mListener);
        mCollectImage = (ImageView) mDialogView.findViewById(R.id.iv_collect);
        mTvCollect = (TextView) mDialogView.findViewById(R.id.tv_collect);
        mLoCenter = mDialogView.findViewById(R.id.lo_center);
        mDelete = mDialogView.findViewById(R.id.delete);
        mDialogView.findViewById(R.id.tv_cacle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mDelete.setOnClickListener(mListener);
    }

    public void hideActionMenu() {
        mDialogView.findViewById(R.id.layout_action_other).setVisibility(View.GONE);
    }

    public void onShare(int id, ChatShareBean shareBean) {
        switch (id) {
            case R.id.layout_share_mtool:
                if (shareBean.type == 1)
                    mContext.startActivity(new Intent(mContext, ShareStateActivity.class).putExtra(Const.ID, shareBean.id));
                break;
            case R.id.layout_share_wechat:
                ShareMethodUtils.showWeiXin_url(shareBean.picUrl, shareBean.title, shareBean.content, "https://www.baidu.com/");
                break;
            case R.id.layout_share_wechat_circle:
                ShareMethodUtils.showWeiXinZone_url(shareBean.picUrl, shareBean.title, "https://www.baidu.com/");
                break;
            case R.id.layout_share_qq:
                ShareMethodUtils.showQQ_url(shareBean.picUrl, shareBean.title, shareBean.content, "https://www.baidu.com/");
                break;
            case R.id.layout_share_qq_space:
                ShareMethodUtils.showQQZone_Url(shareBean.picUrl, shareBean.title, shareBean.content, "https://www.baidu.com/");
                break;
            case R.id.layout_share_weibo:
                ShareMethodUtils.showSina_url(shareBean.picUrl, shareBean.title, shareBean.content, "https://www.baidu.com/");
                break;
        }
    }

    private void resetView(int res) {
        ViewGroup view = (ViewGroup) mDialogView.findViewById(res);
        view.setOnClickListener(mListener);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mSize;
    }

    private int getSize(Context context) {
        int screenWidth = ScreenUtils.getScreenWidth(context);
        return (int) ((screenWidth - context.getResources().getDimension(R.dimen.marginBorder)) / 5);
    }

    public void show(String id, int collectType) {
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.setContentView(mDialogView);

        setCollected(collectType);

        if (id.equals(SPUtil.getString(Const.U_ID, ""))) {
            mLoCenter.setVisibility(View.GONE);
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mLoCenter.setVisibility(View.VISIBLE);
            mDelete.setVisibility(View.GONE);
        }

        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//有edittext必须加这一条
        window.setWindowAnimations(R.style.up_down_anim);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//布局背景透明
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;//在这定义宽度才可以居中
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        alertDialog.onWindowAttributesChanged(attributes);
    }

    public void setCollected(int collectType) {
        boolean isCollected = collectType == 1;
        if (collectType == 1) {
            mTvCollect.setText("取消收藏");
            mCollectImage.setImageResource(R.mipmap.ic_star_empty_red);
        } else {
            mTvCollect.setText("收藏");
            mCollectImage.setImageResource(R.mipmap.ic_star_gray_empty);
        }
        mCollectView.setTag(isCollected);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
}
