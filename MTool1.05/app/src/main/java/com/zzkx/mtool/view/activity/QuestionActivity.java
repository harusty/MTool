package com.zzkx.mtool.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.QuesDetailListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.QuesDetailListPresenter;
import com.zzkx.mtool.presenter.ServicePhonePresenter;
import com.zzkx.mtool.view.iview.IQuesDetailView;
import com.zzkx.mtool.view.iview.IServicePhoneView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/18.
 */

public class QuestionActivity extends BaseActivity implements IQuesDetailView, IServicePhoneView, View.OnClickListener {
    private String mId;
    private QuesDetailListPresenter mQuesDetailListPresenter;

    @BindView(R.id.lv_list)
    ListView mListView;
    private ServicePhonePresenter mServicePhonePresenter;
    private String mPhone;

    @Override
    public int getContentRes() {
        return R.layout.question_deliver;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mId = getIntent().getStringExtra(Const.ID);
        String title = getIntent().getStringExtra(Const.TITLE);
        setMainTitle(title);

        mQuesDetailListPresenter = new QuesDetailListPresenter(this);
        mServicePhonePresenter = new ServicePhonePresenter(this);
        findViewById(R.id.layout_phone).setOnClickListener(this);
    }

    @Override
    public void initNet() {
        mQuesDetailListPresenter.getQuesDetail(mId);
        mServicePhonePresenter.getServicePhone();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showQuesDetail(QuesDetailListBean bean) {
        List<QuesDetailListBean.DataBean> data = bean.data;
        if (data != null) {
            QuesAdapter quesAdapter = new QuesAdapter(data);
            mListView.setAdapter(quesAdapter);
        }
    }

    @Override
    public void showPhone(String data) {
        mPhone = data;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_phone:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("呼叫："+mPhone);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

        }
    }

    private class QuesAdapter extends BaseAdapter {

        private List<QuesDetailListBean.DataBean> mData;

        public QuesAdapter(List<QuesDetailListBean.DataBean> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public QuesDetailListBean.DataBean getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(QuestionActivity.this, R.layout.item_question, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.tv_title);
            TextView content = (TextView) convertView.findViewById(R.id.tv_content);
            QuesDetailListBean.DataBean item = getItem(position);
            title.setText(item.title);
            content.setText(item.contentInfo);
            return convertView;
        }
    }
}
