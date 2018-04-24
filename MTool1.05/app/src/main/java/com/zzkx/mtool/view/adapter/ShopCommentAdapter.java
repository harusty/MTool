package com.zzkx.mtool.view.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ShopCommentListBean;
import com.zzkx.mtool.util.GlideUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by sshss on 2017/10/14.
 */

public class ShopCommentAdapter extends BaseAdapter {
    private View.OnClickListener mBottomListener;
    private Activity mActivity;
    private List<ShopCommentListBean.DataBean> mData;
    private View.OnClickListener mResClickListener;

    public ShopCommentAdapter(Activity activity, List<ShopCommentListBean.DataBean> totalData, View.OnClickListener bottomeClickListener) {
        mActivity = activity;
        mData = totalData;
        mBottomListener = bottomeClickListener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ShopCommentListBean.DataBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        ShopCommentListBean.DataBean dataBean = mData.get(position);
        List<ShopCommentListBean.DataBean.MerchantRes> resources = dataBean.merchantRestaurantsCommentImgs;
        if (resources != null) {
            if (resources.size() == 0) {
                return 0;
            } else if (resources.size() == 1) {
                return 1;
            } else if (resources.size() > 1 && resources.size() <= 3) {
                return 2;
            } else if (resources.size() > 3 && resources.size() <= 6) {
                return 3;
            } else if (resources.size() > 6) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StateListAdapter.ViewHolder viewHolder;
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
                case 3:
                    child = View.inflate(mActivity, R.layout.item_state_multi_image_2, null);
                    break;
                case 4:
                    child = View.inflate(mActivity, R.layout.item_state_multi_image_3, null);
                    break;
            }
            convertView = View.inflate(mActivity, R.layout.item_state, null);
            ((ViewGroup) convertView).addView(child, 1);
            viewHolder = new StateListAdapter.ViewHolder(convertView);
            viewHolder.ic_more.setVisibility(View.INVISIBLE);
            if (mBottomListener == null) {
                viewHolder.layout_bottom.setVisibility(View.GONE);
            } else {
                viewHolder.ic_msg.setOnClickListener(mBottomListener);
                viewHolder.ic_support.setOnClickListener(mBottomListener);
                viewHolder.ic_more.setOnClickListener(mBottomListener);
                viewHolder.iv_user_header.setOnClickListener(mBottomListener);
            }
            if (viewHolder.image_container != null) {
                viewHolder.image_container.setGridMode();
                viewHolder.image_container.setHorizontalSpacing(16);
                viewHolder.image_container.setVerticalSpacing(16);
            }
            viewHolder.tv_content.setMaxLines(2);
            viewHolder.tv_content.setEllipsize(TextUtils.TruncateAt.END);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StateListAdapter.ViewHolder) convertView.getTag();
        }
        ShopCommentListBean.DataBean item = mData.get(position);
        GlideUtil.getInstance().display(viewHolder.iv_user_header, item.memPicUrl);
        viewHolder.tv_user_name.setText(item.memNickname);
        viewHolder.tv_content.setText(item.content);
        viewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(item.createTime)));
        viewHolder.iv_user_header.setTag(R.id.child_index, position);

        viewHolder.ic_msg.setTag(position);
        viewHolder.ic_support.setTag(position);
        viewHolder.ic_more.setTag(position);
        GlideUtil glideInstance = GlideUtil.getInstance();
        viewHolder.tv_msgs.setText(item.opposes + "");
        viewHolder.tv_supports.setText(item.praises + "");

        if (item.suppoppType == 1) {
            viewHolder.ic_support.setImageResource(R.mipmap.ic_good_red);
        } else {
            viewHolder.ic_support.setImageResource(R.mipmap.ic_good);
        }

        switch (getItemViewType(position)) {
            case 0:
                break;
            case 1:
                if (mResClickListener != null)
                    viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.parent_index, position);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, item.merchantRestaurantsCommentImgs.get(0).url);
                break;
            default:
                handleImages(viewHolder, glideInstance, item, position);
                break;
        }

        return convertView;
    }

    private void handleImages(StateListAdapter.ViewHolder viewHolder, GlideUtil glideInstance, ShopCommentListBean.DataBean item, int position) {
        for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
            ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
            if (i < item.merchantRestaurantsCommentImgs.size()) {
                ShopCommentListBean.DataBean.MerchantRes resData = item.merchantRestaurantsCommentImgs.get(i);
                imageview.setTag(R.id.child_index, i);
                imageview.setTag(R.id.parent_index, position);
                if (mResClickListener != null)
                    imageview.setOnClickListener(mResClickListener);
                glideInstance.display(imageview, resData.url);
            } else {
                imageview.destroyDrawingCache();
            }
        }
    }

    public void setOnResClickListener(View.OnClickListener clickListener) {
        mResClickListener = clickListener;
    }
}
