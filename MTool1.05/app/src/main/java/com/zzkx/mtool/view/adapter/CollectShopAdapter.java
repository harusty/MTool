package com.zzkx.mtool.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.aries.ui.view.radius.RadiusTextView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.CollectionBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.customview.MtoolRatingBar;

import java.text.DecimalFormat;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by sshss on 2017/10/17.
 */

public class CollectShopAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private View.OnClickListener mClickListener;
    private LatLng mLatLng;
    private List<Integer> mHeaderIndices;
    private List<BaseCollectBean> mData;

    public CollectShopAdapter(CusCollectBean cusCollectBean, LatLng latLng, View.OnClickListener moreClickListener) {
        mClickListener = moreClickListener;
        mData = cusCollectBean.mCollectedShops;
        mHeaderIndices = cusCollectBean.headerIndices;
        mLatLng = latLng;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        CollectionBean.MerchantRestaurantsListBean merchantRestaurantsListBean = (CollectionBean.MerchantRestaurantsListBean) mData.get(position);
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
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_collection_shop, null);
            holder = new ViewHolder(convertView);
            holder.iv_more.setVisibility(View.VISIBLE);
            holder.iv_more.setOnClickListener(mClickListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectionBean.MerchantRestaurantsListBean shopbean = (CollectionBean.MerchantRestaurantsListBean) mData.get(position);
        holder.iv_more.setTag(shopbean);
        float meter = AMapUtils.calculateLineDistance(new LatLng(shopbean.latitude, shopbean.longitude), mLatLng);
        double km = meter / 1000.0;
        DecimalFormat df = new DecimalFormat("0.00");
        GlideUtil.getInstance().display(holder.iv_image, shopbean.logoUrl);
        holder.title.setText(shopbean.name);
        holder.tv_dis.setText(df.format(km) + "km");
        holder.rating.setCount(shopbean.serviceScore);
        holder.layout_type.removeAllViews();
        RadiusTextView tag = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
        if (shopbean.userMemberAccount != null) {
            tag.setText("保" + shopbean.userMemberAccount.cashPledge);
            holder.layout_type.addView(tag);
        }
        RadiusTextView tag2 = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
        tag2.getDelegate().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.ligthBlue));
        tag2.setText("餐饮");
        holder.layout_type.addView(tag2);
        String intro = "到店人均：" + shopbean.avgConsume + "元\\外送：" + shopbean.deliverAmount + "元起\\配送费：";
        if (shopbean.allofee != null) {
            intro += shopbean.allofee.startPrice + "元";
        }
        holder.tv_info.setText(intro);
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
        public ImageView iv_image;
        public TextView title;
        public MtoolRatingBar rating;
        public TextView tv_dis;
        public ViewGroup layout_type;
        public TextView tv_info;
        public View iv_more;

        public ViewHolder(View convertView) {
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            title = (TextView) convertView.findViewById(R.id.title);
            rating = (MtoolRatingBar) convertView.findViewById(R.id.rating);
            tv_dis = (TextView) convertView.findViewById(R.id.tv_dis);
            layout_type = (ViewGroup) convertView.findViewById(R.id.layout_type);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            iv_more = convertView.findViewById(R.id.iv_more);
        }
    }
}
