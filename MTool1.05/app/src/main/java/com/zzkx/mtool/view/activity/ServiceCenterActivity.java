package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ServCenterCatBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ServCenterCatPresenter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.view.iview.IServCenterView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/18.
 */

public class ServiceCenterActivity extends BaseActivity implements View.OnClickListener, IServCenterView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.grid)
    GridView mGridView;
    private ServCenterCatPresenter mServCenterCatPresenter;
    private static final String CAT1 = "915171953235757";
    private static final String CAT2 = "915171973605034";

    @Override
    public int getContentRes() {
        return R.layout.activity_service_center;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("客服中心");
        mServCenterCatPresenter = new ServCenterCatPresenter(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServCenterCatBean.DataBean item = ((CatAdapter) mListView.getAdapter()).getItem(position);
                startActivity(new Intent(ServiceCenterActivity.this, QuestionActivity.class)
                        .putExtra(Const.ID, item.id)
                        .putExtra(Const.TITLE, item.name)
                );
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServCenterCatBean.DataBean item = ((CatAdapter) mGridView.getAdapter()).getItem(position);
                startActivity(new Intent(ServiceCenterActivity.this, QuestionActivity.class)
                        .putExtra(Const.ID, item.id)
                        .putExtra(Const.TITLE, item.name)
                );
            }
        });
    }

    @Override
    public void initNet() {
        mServCenterCatPresenter.getCats(CAT1);
        mServCenterCatPresenter.getCats(CAT2);
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layout_deliver:
//                startActivity(new Intent(this, QuestionActivity.class));
//                break;
//            case R.id.layout_feedback:
//                startActivity(new Intent(this, FeedBackActivity.class));
//                break;
//        }
    }

    @Override
    public void showCat(ServCenterCatBean bean) {
        CatAdapter catAdapter = new CatAdapter(bean.data, (String) bean.cusTag);
        if (CAT1.equals(bean.cusTag)) {
            mListView.setAdapter(catAdapter);
        } else {
            mGridView.setAdapter(catAdapter);
        }
    }

    private class CatAdapter extends BaseAdapter {

        private String mTag;
        private List<ServCenterCatBean.DataBean> mData;

        public CatAdapter(List<ServCenterCatBean.DataBean> data, String cusTag) {
            mData = data;
            mTag = cusTag;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public ServCenterCatBean.DataBean getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                if (CAT1.equals(mTag))
                    convertView = View.inflate(ServiceCenterActivity.this, R.layout.item_service_ques1, null);
                else {
                    convertView = View.inflate(ServiceCenterActivity.this, R.layout.item_service_ques2, null);
                    View divider = convertView.findViewById(R.id.divider);
                    if (position % 2 == 1) {
                        divider.setVisibility(View.GONE);
                    } else {
                        divider.setVisibility(View.VISIBLE);
                    }
                }
            }
            ImageView logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
            ServCenterCatBean.DataBean item = getItem(position);
            GlideUtil.getInstance().display(logo, item.iconUrl);
            name.setText(item.name);
            return convertView;
        }
    }
}
