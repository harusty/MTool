package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.SystemRecommendListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2018/2/1.
 */

public class SystemRecommendAdapter extends BaseAdapter {

    private View.OnClickListener mListener;
    private List<SystemRecommendListBean.DataBean> mData;

    public SystemRecommendAdapter(List<SystemRecommendListBean.DataBean> data, View.OnClickListener listener) {
        mListener = listener;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SystemRecommendListBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_new_fri_attention, null);
        }
        SystemRecommendListBean.DataBean dataBean = mData.get(position);

        ImageView head = (ImageView) convertView.findViewById(R.id.iv_head);
        TextView name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView confirm = (TextView) convertView.findViewById(R.id.tv_confirm);
        confirm.setTag(position);
        confirm.setOnClickListener(mListener);
        if (dataBean.idolType == 1) {
            confirm.setText("已关注");
            confirm.setClickable(false);
        } else {
            confirm.setText("关  注");
            confirm.setClickable(true);
        }

        GlideUtil.getInstance().display(head, dataBean.picUrl);
        name.setText(dataBean.nickname);
        return convertView;
    }
}
