package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BankCardBean;

import java.util.List;

/**
 * Created by sshss on 2018/1/4.
 */

public class DialogCard extends SimpleDialog implements View.OnClickListener {
    private OnAddListener mListener;
    private ListView mListView;
    private List<BankCardBean.Data> mData;

    public DialogCard(Context context, OnAddListener onAddListener) {
        super(context, R.layout.dialog_card);
        mListener = onAddListener;
        mListView = (ListView) findViewById(R.id.lv_list);
        findViewById(R.id.tv_add).setOnClickListener(this);
    }

    public void setData(List<BankCardBean.Data> data) {
        mData = data;
        mListView.setAdapter(new CardAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onCardClick(mData.get(position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        mListener.OnAdd();
    }

    public interface OnAddListener {
        void OnAdd();

        void onCardClick(BankCardBean.Data data);
    }

    private class CardAdapter extends BaseAdapter {

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
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_bank_card, null);
            }
            BankCardBean.Data dataBean = mData.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            name.setText(dataBean.bankName + "(" + dataBean.bankCarNo.substring(dataBean.bankCarNo.length() - 4, dataBean.bankCarNo.length()) + ")");
            return convertView;
        }
    }
}
