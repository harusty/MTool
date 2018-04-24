package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.view.customview.CategoryLayout;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.IView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/18.
 */

public class SearchHistoryActivity extends BaseActivity implements IView {
    @BindView(R.id.history_container)
    CategoryLayout mContainer;

    @Override
    public int getContentRes() {
        return R.layout.activity_all_serach_history;
    }
    private View.OnClickListener mKeywordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String keyword = ((TextView) v).getText().toString();
            search(keyword);
        }
    };

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle(getString(R.string.search_history));
        mContainer.setVerticalSpacing(Dip2PxUtils.dip2px(this, 10));
        mContainer.setHorizontalSpacing(Dip2PxUtils.dip2px(this, 10));
        int type = getIntent().getIntExtra(Const.TYPE, 0);
        List<String> history = SearchHistoryCacheUtil.getHistory(type);
        if (history != null && history.size() > 0) {
            for (int i = 0; i < history.size(); i++) {
                String keyword = history.get(i);
                TextView tag = (TextView) View.inflate(this, R.layout.item_search_tag, null);
                tag.setText(keyword);
                mContainer.addView(tag);
                tag.setOnClickListener(mKeywordClickListener);
            }
        } else {
            mStateView.setCurrentState(StateView.ResultState.EMPTY);
        }
    }
    public void search(String keword) {
        Intent intent = new Intent().putExtra(Const.KEY_WORD, keword);
        setResult(Const.RESULT_SUCESS_CODE,intent);
        finish();
    }
    @Override
    public void onReload() {

    }
}
