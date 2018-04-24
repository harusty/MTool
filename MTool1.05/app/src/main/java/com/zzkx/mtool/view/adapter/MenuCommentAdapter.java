package com.zzkx.mtool.view.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MenuCommentListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2017/10/14.
 */

public class MenuCommentAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<MenuCommentListBean.DataBean> mData;

    public MenuCommentAdapter(Activity activity, List<MenuCommentListBean.DataBean> totalData) {
        mActivity = activity;
        mData = totalData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StateListAdapter.ViewHolder holder;
        if (convertView == null) {
            View child = null;

            switch (getItemViewType(position)) {
                case 0:
                    child = View.inflate(mActivity, R.layout.item_state_txt, null);
                    break;
                case 1:
                    child = View.inflate(mActivity, R.layout.item_state_single_image, null);
                    break;
                case 2:
                    child = View.inflate(mActivity, R.layout.item_state_multi_image_1, null);
                    break;
            }
            convertView = View.inflate(mActivity, R.layout.item_state, null);
            ((ViewGroup) convertView).addView(child, 1);
            holder = new StateListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StateListAdapter.ViewHolder) convertView.getTag();
        }
        MenuCommentListBean.DataBean dataBean = mData.get(position);

        GlideUtil.getInstance().display(holder.iv_user_header, dataBean.memPic);
        holder.tv_user_name.setText(dataBean.memNickname);
        holder.tv_content.setText(dataBean.content);
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(dataBean.createTime)));

        return convertView;
    }


}
