package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.MysStateGallaryBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

/**
 * Created by sshss on 2017/10/21.
 */

public class MyStateGallaryAdapter extends BaseAdapter {

    private final List<Object> mData;
    private View.OnClickListener mOnResClickListener;

    public MyStateGallaryAdapter(MysStateGallaryBean bean) {
        mData = bean.cusData;
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
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof String)
            return 0;
        else return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            if (itemViewType == 0)
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_gallary_time_line, null);
            else
                convertView = View.inflate(MyApplication.getContext(), R.layout.item_my_state_gallary, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (itemViewType == 0) {
            holder.tv_title.setText(((String) mData.get(position)));
            if (position == 0)
                holder.tmpView.setVisibility(View.GONE);
            else
                holder.tmpView.setVisibility(View.VISIBLE);
        } else
            setImage(holder.image_container, (List<StateListBean.ResData>) mData.get(position));
        return convertView;
    }

    private void setImage(ViewGroup image_container, List<StateListBean.ResData> res) {
        for (int i = 0; i < image_container.getChildCount(); i++) {
            ViewGroup child = (ViewGroup) image_container.getChildAt(i);
            ImageView image = (ImageView) child.getChildAt(0);

            if (i < res.size()) {
                StateListBean.ResData res1 = res.get(i);
                StateListBean.ResData dataBean = res1;
                if (res1.type == 1) {
                    GlideUtil.getInstance().display(image, dataBean.coverUrl);
                    ((ViewGroup) image_container.getChildAt(i)).getChildAt(1).setVisibility(View.VISIBLE);
                } else {
                    GlideUtil.getInstance().display(image, dataBean.resourceUrl);
                    ((ViewGroup) image_container.getChildAt(i)).getChildAt(1).setVisibility(View.INVISIBLE);
                }
                if (mOnResClickListener != null) {
                    child.setOnClickListener(mOnResClickListener);
                    child.setTag(R.id.child_index, res1.cusResIndex);
                }
            } else {
                GlideUtil.getInstance().display(image, R.color.white);
            }
        }
    }

    public void setOnResClickListener(View.OnClickListener onResClickListener) {
        mOnResClickListener = onResClickListener;
    }

    private static class ViewHolder {
        public ViewGroup image_container;
        public TextView tv_title;
        public View tmpView;

        public ViewHolder(View convertView) {
            image_container = (ViewGroup) convertView.findViewById(R.id.image_container);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tmpView = convertView.findViewById(R.id.tmpView);
        }
    }
}
