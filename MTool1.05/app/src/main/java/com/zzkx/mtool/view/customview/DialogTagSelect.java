package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ContactTagBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.presenter.ContactTagsPresenter;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IContactTagsView;

import java.util.List;

/**
 * Created by sshss on 2017/12/6.
 */

public class DialogTagSelect implements IContactTagsView {
    private final ContactTagsPresenter mContactTagsPresenter;
    private ListView mListView;
    private Context mContext;
    private SimpleDialog mDialog;
    private boolean isGetting;
    private List<ContactTagBean.DataBean> mData;
    private DialogSort.ViewHolder mHolder;
    private OnConfirmListener mConfirmListener;

    public DialogTagSelect(Context context) {
        mContext = context;
        mDialog = new SimpleDialog(context, R.layout.dialog_select_friend_tag);

        ((TextView) mDialog.getView().findViewById(R.id.tv_title)).setText("选择标签");
        mListView = (ListView) mDialog.getView().findViewById(R.id.lv_list);
        mContactTagsPresenter = new ContactTagsPresenter(this);
        mDialog.getView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null)
                    mConfirmListener.onConfirm(getTagId());
                dismiss();
            }
        });

        reset();
    }

    public void reset() {
        mData = null;
        mContactTagsPresenter.getTags();
    }


    public void show(DialogSort.ViewHolder sortKey) {
        mHolder = sortKey;
        if (isGetting) {
            ToastUtils.showToast("正在获取标签");
        } else if (mData == null) {
            mContactTagsPresenter.getTags();
            ToastUtils.showToast("正在获取标签");
        } else if (mData.size() == 0) {
            ToastUtils.showToast("还没有添加标签");
        } else {
            mDialog.show();
        }
    }

    public DialogSort.ViewHolder getHolder() {
        return mHolder;
    }

    public void dismiss() {
        mDialog.dismiss();
    }


    @Override
    public void showTags(ContactTagBean bean) {
        mData = bean.data;
        if (mData != null)
            mListView.setAdapter(new TagAdapter());
    }

    @Override
    public void showProgress(boolean toShow) {
        isGetting = toShow;
    }

    @Override
    public void showError(ErrorBean errorBean) {
        isGetting = false;
    }

    private class TagAdapter extends BaseAdapter implements RectChekBox.OnCheckChangeListener {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public ContactTagBean.DataBean getItem(int position) {
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
                convertView = View.inflate(mContext, R.layout.item_dialog_tag, null);
                holder = new ViewHolder(convertView);
                holder.checkbox.setOnCheckChangeListener(this);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setTag(position);
            ContactTagBean.DataBean item = getItem(position);
            holder.tv_name.setText(item.name);
            return convertView;
        }

        @Override
        public void onChange(RectChekBox chekBox, boolean b) {
            int position = (int) chekBox.getTag();
            mData.get(position).cusSlected = true;
        }
    }

    private static class ViewHolder {
        TextView tv_name;
        RectChekBox checkbox;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            checkbox = (RectChekBox) convertView.findViewById(R.id.checkbox);
        }
    }

    public String getTagId() {
        if (mData != null && mData.size() > 0)
            for (ContactTagBean.DataBean dataBean : mData) {
                if (dataBean.cusSlected)
                    return dataBean.id;
            }
        return null;
    }


    public void setOnConfirmListener(OnConfirmListener listener) {
        mConfirmListener = listener;
    }

    public static interface OnConfirmListener {
        void onConfirm(String tagId);
    }
}
