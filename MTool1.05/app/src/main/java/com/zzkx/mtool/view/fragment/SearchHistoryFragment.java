package com.zzkx.mtool.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.HistoryBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.HistoryPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.view.activity.SearchActivity;
import com.zzkx.mtool.view.activity.SearchHistoryActivity;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.iview.ISearchHistoryView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/16.
 */

public class SearchHistoryFragment extends BaseFragment implements ISearchHistoryView, View.OnClickListener {
    @BindView(R.id.history_container)
    CategoryLayout mHistoryContainer;
    @BindView(R.id.hot_container)
    CategoryLayout mHotContainer;
    @BindView(R.id.hot_layout)
    View mHotLayout;
    private HistoryPresenter mPresenter;
    private View.OnClickListener mKeywordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String keyword = ((TextView) v).getText().toString();
            mActivity.search(keyword);
        }
    };
    private SearchActivity mActivity;
    private AlertDialog mClearDialog;
    private int mSearchType;

    @Override
    public int getContentRes() {
        return R.layout.fragment_search_history;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = (SearchActivity) getActivity();
        mSearchType = mActivity.getSearchType();
        mHistoryContainer.setVerticalSpacing(Dip2PxUtils.dip2px(getContext(), 10));
        mHistoryContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(getContext(), 10));
        mHistoryContainer.setMaxLine(4);

        switch (mSearchType) {
            case Const.SEARCH_TYPE_FOODSHOP:
                initHotContainer();
                break;
            case Const.SEARCH_TYPE_STATE:

//                mHotLayout.setVisibility(View.GONE);
                break;
            case Const.SEARCH_TYPE_ORDER:
            case Const.SEARCH_TYPE_COLLECTION:
            case Const.SEARCH_TYPE_MSGFRIEND:
                mHotLayout.setVisibility(View.GONE);
                break;
        }

        mBaseView.findViewById(R.id.iv_tras_can).setOnClickListener(this);
        mBaseView.findViewById(R.id.ic_menu_dot).setOnClickListener(this);
        mPresenter = new HistoryPresenter(this, mSearchType);
        initClearDialog();

    }

    private void initHotContainer() {
        mHotContainer.setVerticalSpacing(Dip2PxUtils.dip2px(getContext(), 10));
        mHotContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(getContext(), 10));
    }

    private void initClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(getString(R.string.confrim_clear_history));
        builder.setPositiveButton(getString(R.string.confrim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mHistoryContainer.removeAllViews();
                mPresenter.clearHistory();
            }
        });
        builder.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mClearDialog = builder.create();
    }

    @Override
    public void initNet() {
        mPresenter.getCacheHistory();
        mPresenter.getHotSearch();
    }

    @Override
    public void onReload() {

    }

    @Override
    public void showData(HistoryBean bean) {
        mHotContainer.removeAllViews();
        List<HistoryBean.DataBean> data = bean.data;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                HistoryBean.DataBean dataBean = data.get(i);
                TextView tag = (TextView) View.inflate(getContext(), R.layout.item_search_tag, null);
                tag.setText(dataBean.keyword);
                mHotContainer.addView(tag);
                tag.setOnClickListener(mKeywordClickListener);
            }
        }
    }

    @Override
    public void showLoacalCache(List<String> history) {
        for (int i = 0; i < history.size(); i++) {
            String keyword = history.get(i);
            TextView tag = (TextView) View.inflate(getContext(), R.layout.item_search_tag, null);
            tag.setText(keyword);
            mHistoryContainer.addView(tag);
            tag.setOnClickListener(mKeywordClickListener);
        }
    }

    public void refreshHistory(String keyword) {
        TextView tag = (TextView) View.inflate(getContext(), R.layout.item_search_tag, null);
        tag.setText(keyword);
        mHistoryContainer.addView(tag);
        tag.setOnClickListener(mKeywordClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tras_can:
                mClearDialog.show();
                break;
            case R.id.ic_menu_dot:
                startActivityForResult(new Intent(getContext(), SearchHistoryActivity.class).putExtra(Const.TYPE, mSearchType), 999);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            String keyword = data.getStringExtra(Const.KEY_WORD);
            mActivity.search(keyword);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
