package com.zzkx.mtool.view.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.ShowAtUtil;
import com.zzkx.mtool.view.activity.StateDetailActivity;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.RoundImageView1_1W;

import java.util.Date;
import java.util.List;

import static android.view.View.inflate;

/**
 * Created by sshss on 2017/9/26.
 */

public class StateListAdapter extends BaseAdapter {

    private View.OnClickListener mBottomListener;
    private FragmentActivity mActivity;
    private List mData;
    public static final int TYPE_SUPPOSED = 1;
    private int mType;
    private View.OnClickListener mResClickListener;

    public StateListAdapter(FragmentActivity activity, List totalData,
                            View.OnClickListener bottomeClickListener) {
        mBottomListener = bottomeClickListener;
        mData = totalData;
        mActivity = activity;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public StateListBean.DataBean getItem(int position) {
        return (StateListBean.DataBean) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        StateListBean.DataBean item = getItem(position);
        List<StateListBean.ResData> resources = item.forumThreadResources;
        if (resources != null) {
            if (resources.size() == 0) {
                return 0;
            } else if (resources.size() == 1) {
                StateListBean.ResData resData = resources.get(0);
                if (resData.type == 0)
                    return 1;
                else
                    return 5;//视频
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
        ViewHolder viewHolder;
        final StateListBean.DataBean item = getItem(position);
        final int itemViewType = getItemViewType(position);
        if (convertView == null) {
            View child = null;

            switch (itemViewType) {
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
                case 5:
                    child = View.inflate(mActivity, R.layout.item_state_video, null);
                    break;
            }
            convertView = inflate(mActivity, R.layout.item_state, null);
            View shareType = inflate(mActivity, R.layout.item_state_txt_share, null);
            shareType.setVisibility(View.GONE);
            ((ViewGroup) convertView).addView(shareType, 1);
            ((ViewGroup) convertView).addView(child, 2);
            viewHolder = new ViewHolder(convertView);
            if (mBottomListener == null) {
                viewHolder.layout_bottom.setVisibility(View.GONE);
                viewHolder.ic_more.setVisibility(View.INVISIBLE);
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
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item.shareType == 1) {
            viewHolder.layout_share_type.setVisibility(View.VISIBLE);
            viewHolder.tv_name_orgin.setText("引用/原著：" + item.firstName + "/");
            viewHolder.tv_show_orgin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, StateDetailActivity.class)
                            .putExtra(Const.ID, item.firstId)
                            .putExtra(Const.TYPE, itemViewType)
                    );
                }
            });
            ShowAtUtil.handleAtUsers(viewHolder.tv_content, item.content, item.userMemberList);
            ShowAtUtil.handleAtUsers(viewHolder.tv_share_content, item.share, item.shareuserMemberList);
        } else {
            ShowAtUtil.handleAtUsers(viewHolder.tv_content, item.content, item.userMemberList);
            viewHolder.layout_share_type.setVisibility(View.GONE);
        }
        viewHolder.iv_user_header.setTag(R.id.child_index, position);
        viewHolder.ic_msg.setTag(position);
        viewHolder.ic_support.setTag(position);
        viewHolder.ic_more.setTag(position);
        GlideUtil glideInstance = GlideUtil.getInstance();


        viewHolder.tv_msgs.setText(item.opposes + "");
        viewHolder.tv_supports.setText(item.supports + "");
        if (item.suppoppType == 1 || mType == TYPE_SUPPOSED) {
            viewHolder.ic_support.setImageResource(R.mipmap.ic_good_red);
        } else {
            viewHolder.ic_support.setImageResource(R.mipmap.ic_good);
        }
        StateListBean.UserMemberBean userMember = item.userMember;
        if (userMember != null) {
            glideInstance.display(viewHolder.iv_user_header, userMember.picUrl);
            viewHolder.tv_user_name.setText(userMember.nickname);
        }
        viewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(item.createTime)));

        switch (itemViewType) {
            case 0:
                break;
            case 1:
                if (mResClickListener != null)
                    viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.parent_index, position);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, item.forumThreadResources.get(0).resourceUrl);
                break;
            case 5:
                if (mResClickListener != null)
                    viewHolder.image.setOnClickListener(mResClickListener);
                viewHolder.image.setTag(R.id.parent_index, position);
                viewHolder.image.setTag(R.id.child_index, 0);
                glideInstance.display(viewHolder.image, item.forumThreadResources.get(0).coverUrl);
                break;
            default:
                handleImages(viewHolder, glideInstance, item, position);
                break;
        }
        return convertView;
    }


    private void handleImages(ViewHolder viewHolder, GlideUtil glideInstance, StateListBean.DataBean item, int position) {
        for (int i = 0; i < viewHolder.image_container.getChildCount(); i++) {
            ImageView imageview = (ImageView) viewHolder.image_container.getChildAt(i);
            if (i < item.forumThreadResources.size()) {
                StateListBean.ResData resData = item.forumThreadResources.get(i);
                imageview.setTag(R.id.child_index, i);
                imageview.setTag(R.id.parent_index, position);
                if (mResClickListener != null)
                    imageview.setOnClickListener(mResClickListener);
                glideInstance.display(imageview, resData.resourceUrl);
            } else {
                imageview.destroyDrawingCache();
            }
        }
    }

    private ImageView createImageView() {
        RoundImageView1_1W roundImageView = new RoundImageView1_1W(MyApplication.getContext());
        return roundImageView;
    }

    public static class ViewHolder {

        public ImageView iv_user_header;
        public TextView tv_content;
        public TextView tv_share_content;
        public TextView tv_name_orgin;
        public TextView tv_show_orgin;
        public TextView tv_user_name;
        public TextView tv_msgs;
        public TextView tv_msgs2;
        public TextView tv_supports;
        public TextView tv_supports2;
        public ImageView image;
        public ImageView ic_msg;
        public ImageView ic_msg2;
        public ImageView ic_support;
        public ImageView ic_support2;
        public CategoryLayout image_container;
        public TextView tv_time;
        public TextView tv_section;
        public View ic_more;
        public View layout_bottom;
        public View layout_share_type;


        public ViewHolder(View convertView) {
            tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            tv_show_orgin = (TextView) convertView.findViewById(R.id.tv_show_orgin);
            tv_name_orgin = (TextView) convertView.findViewById(R.id.tv_name_orgin);
            tv_share_content = (TextView) convertView.findViewById(R.id.tv_share_content);
            iv_user_header = (ImageView) convertView.findViewById(R.id.iv_user_header);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_msgs = (TextView) convertView.findViewById(R.id.tv_msgs);
            tv_msgs2 = (TextView) convertView.findViewById(R.id.tv_msgs2);
            tv_supports = (TextView) convertView.findViewById(R.id.tv_supports);
            tv_supports2 = (TextView) convertView.findViewById(R.id.tv_supports2);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_section = (TextView) convertView.findViewById(R.id.tv_section);
            image = (ImageView) convertView.findViewById(R.id.image);
            ic_support = (ImageView) convertView.findViewById(R.id.ic_support);
            ic_support2 = (ImageView) convertView.findViewById(R.id.ic_support2);
            ic_msg = (ImageView) convertView.findViewById(R.id.ic_msg);
            ic_msg2 = (ImageView) convertView.findViewById(R.id.ic_msg2);
            image_container = (CategoryLayout) convertView.findViewById(R.id.image_container);
            ic_more = convertView.findViewById(R.id.iv_more);
            layout_bottom = convertView.findViewById(R.id.layout_bottom);
            layout_share_type = convertView.findViewById(R.id.layout_share_type);
        }
    }

    public void setOnResClickListener(View.OnClickListener clickListener) {
        mResClickListener = clickListener;
    }

    public interface OnResClickListener {
        void OnClick();
    }
}
