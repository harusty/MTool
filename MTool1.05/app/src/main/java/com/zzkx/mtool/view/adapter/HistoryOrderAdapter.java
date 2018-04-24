package com.zzkx.mtool.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.HistoryOrderBean;
import com.zzkx.mtool.util.CartCacheUtil;
import com.zzkx.mtool.view.adapter.base.BaseViewHolder;
import com.zzkx.mtool.view.adapter.base.MyBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sshss on 2017/9/15.
 */

public class HistoryOrderAdapter extends MyBaseAdapter<HistoryOrderBean.DataBean, HistoryOrderAdapter.ViewHolder> {
    private SimpleDateFormat mFormat;

    public HistoryOrderAdapter(Context context, List<HistoryOrderBean.DataBean> data) {
        super(context, data);
        mFormat = new SimpleDateFormat("yyyy.MM.dd");
    }

    @Override
    public int getItemRes() {
        return R.layout.item_history_order;
    }

    @Override
    public void setView(ViewHolder holder, HistoryOrderBean.DataBean dataBean) {
        HistoryOrderBean.DiningType diningType = dataBean.orderDining;
        switch (diningType.diningType) {
            case CartCacheUtil.TYPE_OUT:
                holder.iv_logo.setBackgroundResource(R.mipmap.ic_order_type_deliver);
                holder.tv_info.setText("外送：" + dataBean.shopName);
                holder.tv_time.setText(mFormat.format(dataBean.createTime));
                break;
            case CartCacheUtil.TYPE_IN:
                holder.iv_logo.setBackgroundResource(R.mipmap.ic_order_type_toshop);
                holder.tv_info.setText("到店：" + dataBean.shopName);
                holder.tv_time.setText(mFormat.format(dataBean.createTime));
                break;
        }
    }

    @Override
    public ViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    class ViewHolder extends BaseViewHolder {
        public ImageView iv_logo;
        public TextView tv_info;
        public TextView tv_time;

        public ViewHolder(View convertView) {
            super(convertView);
            iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        }
    }
}
