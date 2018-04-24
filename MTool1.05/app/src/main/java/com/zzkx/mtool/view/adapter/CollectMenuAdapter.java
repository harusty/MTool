package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/17.
 */

public class CollectMenuAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private View.OnClickListener mClickListener;
    private List<Integer> mHeaderIndices;
    private List<BaseCollectBean> mData;

    public CollectMenuAdapter(CusCollectBean cusCollectBean, View.OnClickListener moreClickListener) {
        mClickListener = moreClickListener;
        mData = cusCollectBean.mCollectedShops;
        mHeaderIndices = cusCollectBean.headerIndices;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        CollectionBean.MemberGoodsCollectDoBean merchantRestaurantsListBean = (CollectionBean.MemberGoodsCollectDoBean) mData.get(position);
        CollectionBean.DataBean cusParentBean = merchantRestaurantsListBean.cusParentBean;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_collection_cat, null);
        }
        TextView tvname = (TextView) convertView.findViewById(R.id.tv_section);
        tvname.setText(cusParentBean.name);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mData.get(position).cusGroupPosition;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BaseCollectBean getItem(int position) {
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_collect_menu, null);
            holder = new ViewHolder(convertView);
            holder.iv_more.setVisibility(View.VISIBLE);
            holder.iv_more.setOnClickListener(mClickListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectionBean.MemberGoodsCollectDoBean item = (CollectionBean.MemberGoodsCollectDoBean) mData.get(position);
        holder.iv_more.setTag(item);
        GlideUtil.getInstance().display(holder.iv_good, item.goodsImg);
        holder.tv_name.setText(item.goodsName);
        holder.tv_money.setText(item.goodsPriceOut);
        CollectionBean.FoodInfoBean foodInfo = item.foodInfo;
        String info = "好评：";
        if (foodInfo != null) {
            holder.tv_shop_name.setText(foodInfo.shopName);
            info += foodInfo.praises + "%/月销量：" + foodInfo.monthlySales;
            holder.tv_info.setText(info);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Integer[]{1, 2};
    }

    //header在整个集合中的位置
    @Override
    public int getPositionForSection(int section) {
        if (section >= mHeaderIndices.size()) {
            section = mHeaderIndices.size() - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mHeaderIndices.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mHeaderIndices.size(); i++) {
            if (position < mHeaderIndices.get(i)) {
                return i - 1;
            }
        }
        return mHeaderIndices.size() - 1;
    }

    public static class ViewHolder {
        public View iv_more;
        public ImageView iv_good;
        public TextView tv_name;
        public TextView tv_info;
        public TextView tv_money;
        public TextView tv_shop_name;

        public ViewHolder(View convertView) {
            iv_more = convertView.findViewById(R.id.iv_more);
            iv_good = (ImageView) convertView.findViewById(R.id.iv_good);
            tv_name = (TextView) convertView.findViewById(R.id.tv_section);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
        }
    }
}
