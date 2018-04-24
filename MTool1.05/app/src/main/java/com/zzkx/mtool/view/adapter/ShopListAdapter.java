package com.zzkx.mtool.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.aries.ui.view.radius.RadiusTextView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.adapter.base.BaseViewHolder;
import com.zzkx.mtool.view.adapter.base.MyBaseAdapter;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.MtoolRatingBar;

import java.util.List;

/**
 * Created by sshss on 2017/8/23.
 */

public class ShopListAdapter extends MyBaseAdapter<CloudItem, ShopListAdapter.ViewHolder> {


    public ShopListAdapter(Context context, List<CloudItem> data) {
        super(context, data);
    }

    @Override
    public int getItemRes() {
        return R.layout.item_list_shop;
    }

    @Override
    public void setView(ViewHolder holder, CloudItem cloudItem) {
        String s_deposit = cloudItem.getCustomfield().get(Const.CUS_BAOZHENG);//保证金
        String s_toStore_money = cloudItem.getCustomfield().get(Const.CUS_RENJUN);//人均消费
        String s_toHome_money = cloudItem.getCustomfield().get(Const.CUS_QISONG);//起送价格
        String s_toHome_tip = cloudItem.getCustomfield().get(Const.CUS_PEISONG);//配送费
        String s_type = cloudItem.getCustomfield().get(Const.CUS_TYPE);//类型
        String s_service = cloudItem.getCustomfield().get(Const.CUS_SERV_SCORE);//服务分
        if (!TextUtils.isEmpty(s_service)) {
            holder.rating.setCount(Integer.parseInt(s_service));
        }
        holder.typeLayout.removeAllViews();
        TextView tag = (TextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
        tag.setText("保" + s_deposit);
        holder.typeLayout.addView(tag);
        RadiusTextView typeTag = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
        if ("餐饮".equals(s_type)) {
            typeTag.setText("餐饮");
            typeTag.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.ligthBlue));
            holder.typeLayout.addView(typeTag);
        } else if ("酒店".equals(s_type)) {
            typeTag.setText("酒店");
            typeTag.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.darkBlue));
            holder.typeLayout.addView(typeTag);
        } else if ("娱乐".equals(s_type)) {
            typeTag.setText("娱乐");
            typeTag.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.dartYellow));
            holder.typeLayout.addView(typeTag);
        }

        holder.tv_info.setText("到店人均：" + s_toStore_money + "元\\外送：" + s_toHome_money + "元起\\配送费：" + s_toHome_tip + "元");

        GlideUtil.getInstance().display(holder.iv_image, cloudItem.getCustomfield().get(Const.CUS_SHOP_LOGO));
        holder.title.setText(cloudItem.getTitle());
        double dis = ((double) cloudItem.getDistance()) / 1000.0;
        holder.tv_dis.setText(dis + "km");
    }

    @Override
    public ViewHolder getViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    static class ViewHolder extends BaseViewHolder {
        TextView title;
        TextView tv_dis;
        TextView tv_info;
        ImageView iv_image;
        MtoolRatingBar rating;
        CategoryLayout typeLayout;

        public ViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            tv_dis = (TextView) convertView.findViewById(R.id.tv_dis);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            rating = (MtoolRatingBar) convertView.findViewById(R.id.rating);
            typeLayout = (CategoryLayout) convertView.findViewById(R.id.layout_type);
        }
    }
}
