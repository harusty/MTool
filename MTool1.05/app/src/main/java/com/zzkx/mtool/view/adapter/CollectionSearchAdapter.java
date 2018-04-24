package com.zzkx.mtool.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.aries.ui.view.radius.RadiusTextView;
import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CollectionSearchBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2017/10/27.
 */

public class CollectionSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> mData;
    private AMapLocation mMyLocation;

    public CollectionSearchAdapter(Context context, CollectionSearchBean bean) {
        mContext = context;
        mData = bean.cusData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 8;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof String)
            return 0;
        else if (item instanceof CollectionSearchBean.ShopListBean)
            return 1;
        else if (item instanceof CollectionSearchBean.GoodsListBean)
            return 2;
        else if (item instanceof CollectionSearchBean.ForumPostCollectListBean) {
            StateListBean.DataBean item1 = (((CollectionSearchBean.ForumPostCollectListBean) item)).forumPostDo;
            return getStateViewType(item1);
        }

        return 0;
    }


    private int getStateViewType(StateListBean.DataBean item) {
        List<StateListBean.ResData> resources = item.forumThreadResources;
        if (resources != null) {
            if (resources.size() == 0) {
                return 3;
            } else if (resources.size() == 1) {
                return 4;
            } else if (resources.size() > 1 && resources.size() <= 3) {
                return 5;
            } else if (resources.size() > 3 && resources.size() <= 6) {
                return 6;
            } else if (resources.size() > 6) {
                return 7;
            } else {
                return 0;
            }
        } else {
            return 3;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);
        Object holder = null;
        if (convertView == null) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case 0:
                    convertView = View.inflate(mContext, R.layout.item_title, null);
                    holder = new TitleHolder(convertView);
                    convertView.setTag(holder);
                    break;
                case 1:
                    convertView = View.inflate(mContext, R.layout.item_collection_shop, null);
                    holder = new CollectShopAdapter.ViewHolder(convertView);
                    ((CollectShopAdapter.ViewHolder) holder).iv_more.setVisibility(View.INVISIBLE);
                    convertView.setTag(holder);
                    break;
                case 2:
                    convertView = View.inflate(mContext, R.layout.item_collect_menu, null);
                    holder = new CollectMenuAdapter.ViewHolder(convertView);
                    ((CollectMenuAdapter.ViewHolder) holder).iv_more.setVisibility(View.INVISIBLE);
                    ((CollectMenuAdapter.ViewHolder) holder).tv_shop_name.setVisibility(View.INVISIBLE);
                    convertView.setTag(holder);
                    ((CollectMenuAdapter.ViewHolder) holder).tv_money.setText("aaaa");
                    break;
                default:
                    convertView = handleStateView(itemViewType);
                    holder = convertView.getTag();
                    break;
            }
        } else {
            holder = convertView.getTag();
        }
        switch (getItemViewType(position)) {
            case 0:
                ((TitleHolder) holder).section.setText((String) item);
                break;
            case 1:
                CollectShopAdapter.ViewHolder holder1 = (CollectShopAdapter.ViewHolder) holder;
                CollectionSearchBean.MerchantRestaurantsDoBean shopInfo = ((CollectionSearchBean.ShopListBean) item).merchantRestaurantsDo;
                if (shopInfo != null) {
                    GlideUtil.getInstance().display(holder1.iv_image, shopInfo.logoUrl);
                    holder1.title.setText(shopInfo.name);
                    holder1.rating.setCount(shopInfo.serviceScore);

                    String intro = "到店人均：" + shopInfo.avgConsume + "元\\外送：" + shopInfo.deliverAmount + "元起\\配送费：" + shopInfo.toHomeTip + "元";

                    holder1.tv_info.setText(intro);

                    if (mMyLocation != null) {
                        float meter = AMapUtils.calculateLineDistance(new LatLng(shopInfo.latitude, shopInfo.longitude), new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()));
                        double km = meter / 1000.0;
                        DecimalFormat df = new DecimalFormat("0.00km");
                        holder1.tv_dis.setText(df.format(km));
                    }
                    holder1.layout_type.removeAllViews();
                    RadiusTextView tag = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
                    tag.setText("保" + shopInfo.deposit);
                    holder1.layout_type.addView(tag);
                    RadiusTextView tag2 = (RadiusTextView) View.inflate(MyApplication.getContext(), R.layout.item_shop_tag, null);
                    tag2.getDelegate().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.ligthBlue));
                    tag2.setText("餐饮");
                    holder1.layout_type.addView(tag2);
                    ((LinearLayout.LayoutParams) tag2.getLayoutParams()).setMargins(15, 0, 0, 0);
                }
                break;
            case 2:
                CollectMenuAdapter.ViewHolder holder2 = (CollectMenuAdapter.ViewHolder) holder;
                CollectionSearchBean.GoodsListBean item2 = (CollectionSearchBean.GoodsListBean) item;
                GlideUtil.getInstance().display(holder2.iv_good, item2.goodsImg);
                holder2.tv_name.setText(item2.goodsName);
                String info = "好评：";
                if (item2.foodInfo != null) {
                    info += item2.foodInfo.praises + "%/月销量：" + item2.foodInfo.monthlySales;
                    holder2.tv_info.setText(info);
                }
                holder2.tv_money.setText(item2.goodsPrice + "");
                break;
            default:
                StateListAdapter.ViewHolder holder3 = (StateListAdapter.ViewHolder) holder;
                StateListBean.DataBean item3 = ((CollectionSearchBean.ForumPostCollectListBean) item).forumPostDo;
                holder3.ic_msg.setTag(position);
                holder3.ic_support.setTag(position);
                holder3.ic_more.setTag(position);
                GlideUtil glideInstance = GlideUtil.getInstance();
                holder3.tv_content.setText(item3.content);
                holder3.tv_msgs.setText(item3.opposes + "");
                holder3.tv_supports.setText(item3.supports + "");
                if (item3.suppoppType == 1) {
                    holder3.ic_support.setImageResource(R.mipmap.ic_good_red);
                } else {
                    holder3.ic_support.setImageResource(R.mipmap.ic_good);
                }

                StateListBean.UserMemberBean userMember = item3.userMember;
                if (userMember != null) {
                    glideInstance.display(holder3.iv_user_header, userMember.picUrl);
                    holder3.tv_user_name.setText(userMember.nickname);
                }

                holder3.tv_time.setText(DateUtils.getTimestampString(new Date(item3.createTime)));
                switch (getItemViewType(position)) {
                    case 3:
                        break;
                    case 4:
                        glideInstance.display(holder3.image, item3.forumThreadResources.get(0).resourceUrl);
                        break;
                    default:
                        handleImages(holder3, glideInstance, item3);
                        break;
                }

        }
        return convertView;
    }

    private View handleStateView(int itemViewType) {
        View child = null;
        StateListAdapter.ViewHolder holder;
        switch (itemViewType) {
            case 3:
                child = View.inflate(mContext, R.layout.item_state_txt, null);
                break;
            case 4:
                child = View.inflate(mContext, R.layout.item_state_single_image, null);
                break;
            case 5:
                child = View.inflate(mContext, R.layout.item_state_multi_image_1, null);
                break;
            case 6:
                child = View.inflate(mContext, R.layout.item_state_multi_image_2, null);
                break;
            case 7:
                child = View.inflate(mContext, R.layout.item_state_multi_image_3, null);
                break;
        }
        View convertView = View.inflate(mContext, R.layout.item_state, null);
        ((ViewGroup) convertView).addView(child, 1);
        holder = new StateListAdapter.ViewHolder(convertView);
        (holder).layout_bottom.setVisibility(View.GONE);
        (holder).ic_more.setVisibility(View.INVISIBLE);
        if ((holder).image_container != null) {
            (holder).image_container.setGridMode();
            (holder).image_container.setHorizontalSpacing(16);
            (holder).image_container.setVerticalSpacing(16);
        }
        (holder).tv_content.setMaxLines(2);
        (holder).tv_content.setEllipsize(TextUtils.TruncateAt.END);
        System.out.println("ic_msg: " + (holder.ic_msg == null));
        convertView.setTag(holder);
        return convertView;

    }

    private void handleImages(StateListAdapter.ViewHolder viewHolder, GlideUtil glideInstance, StateListBean.DataBean item) {
        for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
            ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
            if (i < item.forumThreadResources.size()) {
                StateListBean.ResData resData = item.forumThreadResources.get(i);
                glideInstance.display(imageview, resData.resourceUrl);
            } else {
                imageview.destroyDrawingCache();
            }
        }
    }

    public void setMyLocation(AMapLocation myLocation) {
        mMyLocation = myLocation;
    }

    private static class TitleHolder {
        TextView section;

        public TitleHolder(View convertView) {
            section = (TextView) convertView.findViewById(R.id.tv_section);
        }
    }


    private static class GoodsHolder {

    }
}
