package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.CollectionCatBean;
import com.zzkx.mtool.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/10/17.
 */

public class DialogCollectionCatList {

    public static final int ACTION_SELECT = 0;
    public static final int ACTION_DELETE = 1;
    public static final int ACTION_EDIT = 2;
    private ListView mListView;
    private int mAction;

    private SimpleDialog mSimpleDialog;
    private List<CollectionCatBean.DataBean> mData;
    private int mLastPosition = -1;
    private boolean mMultiSelect;
    private BaseAdapter mAdapter;
    private List<CollectionCatBean.DataBean> mSelectList;
    private BaseCollectBean mItem;

    public DialogCollectionCatList(Context context, View.OnClickListener confrimClickListener) {

        mSimpleDialog = new SimpleDialog(context, R.layout.dialog_cat_selet);
        TextView title = (TextView) mSimpleDialog.getView().findViewById(R.id.tv_title);
        mListView = (ListView) mSimpleDialog.getView().findViewById(R.id.lv_list);
        title.setText("选择分类");
        mSimpleDialog.getView().findViewById(R.id.tv_confirm).setOnClickListener(confrimClickListener);
        mSimpleDialog.getView().findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void dismiss() {
        mSimpleDialog.dismiss();
    }

    public void show(int action, BaseCollectBean item) {
        if (mData == null) {
            ToastUtils.showToast("还未添加分类");
            return;
        }
        mItem = item;
        for (int i = 0; i < mData.size(); i++) {
            CollectionCatBean.DataBean dataBean = mData.get(i);
            dataBean.cusSlected = item != null && item.catelogIds != null && item.catelogIds.contains(dataBean.id);
        }
        mAdapter.notifyDataSetChanged();
        mAction = action;
        switch (action) {
            case ACTION_SELECT:
                mMultiSelect = true;
                break;
            case ACTION_DELETE:
                mMultiSelect = false;
                break;
            case ACTION_EDIT:
                mMultiSelect = false;
                break;
        }
        mSimpleDialog.show();
        Window window = mSimpleDialog.getDialogWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;//在这定义宽度才可以居中
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;
        mSimpleDialog.getDialog().onWindowAttributesChanged(attributes);

    }

    public void setData(final List<CollectionCatBean.DataBean> data) {
        if (data == null || data.size() == 0)
            return;
        mData = data;
        mAdapter = new BaseAdapter() {
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
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = View.inflate(MyApplication.getContext(), R.layout.item_collection_cat_name, null);
                CollectionCatBean.DataBean dataBean = mData.get(position);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                RectChekBox chekBox = (RectChekBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(dataBean);
                chekBox.setTouchable(false);
                chekBox.setChecked(dataBean.cusSlected);
                name.setText(dataBean.name);
                return convertView;
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionCatBean.DataBean dataBean = mData.get(position);
                boolean cusSlected = dataBean.cusSlected;
                dataBean.cusSlected = !cusSlected;

                if (mLastPosition != -1 && mLastPosition != position && !mMultiSelect) {
                    CollectionCatBean.DataBean lastBean = mData.get(mLastPosition);

                    boolean cusSlected1 = lastBean.cusSlected;
                    if (cusSlected1)
                        lastBean.cusSlected = false;
                }
                mLastPosition = position;
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    public List<CollectionCatBean.DataBean> getSelectList() {
        if (mSelectList == null)
            mSelectList = new ArrayList<>();
        mSelectList.clear();
        for (int i = 0; i < mData.size(); i++) {
            CollectionCatBean.DataBean dataBean = mData.get(i);
            if (dataBean.cusSlected)
                mSelectList.add(dataBean);
        }
        return mSelectList;
    }

    public int getAction() {
        return mAction;
    }

    public BaseCollectBean getItem() {
        return mItem;
    }
}
