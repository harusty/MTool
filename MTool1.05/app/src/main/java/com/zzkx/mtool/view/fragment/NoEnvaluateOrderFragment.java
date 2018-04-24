package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.OrderListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.NoEnvaluateOrderPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.view.activity.OrderEnvaluateActivity;
import com.zzkx.mtool.view.iview.INoEnvaluateOrderView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/8.
 */

public class NoEnvaluateOrderFragment extends BaseFragment implements INoEnvaluateOrderView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwipLayout;
    private NoEnvaluateOrderPresenter mPresenter;
    private MyAdapter mAdapter;
    private List<OrderListBean.DataBean> mData;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mSwipLayout.setOnRefreshListener(this);
        mSwipLayout.setColorSchemeResources(R.color.colorPrimary);
        mPresenter = new NoEnvaluateOrderPresenter(this);
        mListView.setPadding(0, Dip2PxUtils.dip2px(getContext(),10),0,0);
    }

    @Override
    public void initNet() {
        mPresenter.getData();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showData(List<OrderListBean.DataBean> data) {
        mData = data;
        if (mAdapter == null) {
            mAdapter = new MyAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OrderListBean.DataBean dataBean = mData.get(position);

                    startActivityForResult(new Intent(getActivity(), OrderEnvaluateActivity.class)
                            .putExtra(Const.ID, dataBean.id),9);
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Const.RESULT_SUCESS_CODE){
            initNet();
        }
    }

    @Override
    public void onRefresh() {
        initNet();
    }

    private class MyAdapter extends BaseAdapter {


        public MyAdapter() {

        }

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
            OrderListBean.DataBean dataBean = mData.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_my_chat_history, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String typeName = "";
            switch (dataBean.diningType) {
                case 0://外送
                    holder.avatar.setImageResource(R.mipmap.ic_order_type_deliver);
                    typeName = "外送：";
                    break;
                case 1://到店
                    typeName = "到店：";
                    holder.avatar.setImageResource(R.mipmap.ic_order_type_toshop);
                    break;
                case 2://代送
                    typeName = "外送：";
                    holder.avatar.setImageResource(R.mipmap.ic_order_type_deliver);
                    break;
            }
            holder.name.setText(typeName + dataBean.orderName);
            holder.tv_time.setText(DateUtils.getTimestampString(new Date(dataBean.createTime)));
            return convertView;
        }
    }

    @Override
    public void showProgress(boolean toShow) {
        mSwipLayout.setRefreshing(toShow);
    }

    private static class ViewHolder {

        TextView name;
        ImageView avatar;
        TextView tv_time;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            avatar = (ImageView) convertView.findViewById(R.id.avatar);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);

        }
    }
}
