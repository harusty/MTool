package com.zzkx.mtool.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.activity.AttentionListActivity;
import com.zzkx.mtool.view.customview.RectChekBox;

import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionUserAdapter extends BaseAdapter {
    private int mAction;
    private List<AttentionUserBean.DataBean> mData;

    public AttentionUserAdapter(List<AttentionUserBean.DataBean> data, int action) {
        mData = data;
        mAction = action;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public AttentionUserBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_attention, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            if (mAction == AttentionListActivity.ACTION_SELECT) {
                holder.chekcbox.setVisibility(View.VISIBLE);
                holder.chekcbox.setTouchable(false);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AttentionUserBean.DataBean dataBean = mData.get(position);
        holder.chekcbox.setChecked(dataBean.cusSelected);
        String header = dataBean.cusInitial;
        if ((position == 0 || header != null && !header.equals(getItem(position - 1).cusInitial))) {
            if (TextUtils.isEmpty(header)) {
                holder.tv_initial.setVisibility(View.GONE);
            } else {
                holder.tv_initial.setVisibility(View.VISIBLE);
                holder.tv_initial.setText(header);
                holder.tv_initial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            holder.tv_initial.setVisibility(View.GONE);
        }
        AttentionUserBean.UserMemberBean userMember = dataBean.userMember;
        if (userMember != null) {
            holder.tv_name.setText(userMember.nickname);
            GlideUtil.getInstance().display(holder.iv_header, userMember.picUrl);
        }
        return convertView;
    }

    public static class ViewHolder {

        public TextView tv_initial;
        public ImageView iv_header;
        public TextView tv_name;
        public RectChekBox chekcbox;

        public ViewHolder(View convertView) {
            tv_initial = (TextView) convertView.findViewById(R.id.tv_initial);
            iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            chekcbox = (RectChekBox) convertView.findViewById(R.id.chekcbox);
        }
    }
}
