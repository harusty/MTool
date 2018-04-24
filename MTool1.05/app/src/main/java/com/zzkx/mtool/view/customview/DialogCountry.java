package com.zzkx.mtool.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.CountrySelecBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2018/1/9.
 */

public class DialogCountry extends SimpleDialog {
    private  OnSelectListener mListener;
    private ListView mListView;
    private List<CountrySelecBean> mData = new ArrayList<>();

    public DialogCountry(final Context context,OnSelectListener listener) {
        super(context, R.layout.dialog_select_country);
        mListener = listener;
        mListView = (ListView) findViewById(R.id.lv_list);
        mData.add(new CountrySelecBean("中国", "+86"));
        mData.add(new CountrySelecBean("香港", "+852"));
        mData.add(new CountrySelecBean("澳门", "+853"));
        mData.add(new CountrySelecBean("台湾", "+886"));
        mListView.setAdapter(new BaseAdapter() {
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
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null)
                    convertView = View.inflate(context, R.layout.item_country_selec, null);
                TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                CountrySelecBean countrySelecBean = mData.get(position);
                tv_name.setText(countrySelecBean.name);
                tv_phone.setText(countrySelecBean.phone);
                return convertView;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onSelect(mData.get(position));
                dismiss();
            }
        });
    }

    public interface OnSelectListener {
        void onSelect(CountrySelecBean bean);
    }
}
