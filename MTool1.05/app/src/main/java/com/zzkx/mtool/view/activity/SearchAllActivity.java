package com.zzkx.mtool.view.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableHelper;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;

/**
 * Created by sshss on 2017/11/20.
 */

public abstract class SearchAllActivity extends BaseActivity implements ScrollableHelper.ScrollableContainer {
    public ListView mListView;
    public ScrollableLayout mScrollableLayout;
    public EditText mInput;

    @Override
    public int getContentRes() {
        return R.layout.activity_search_all_contact;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        mListView = (ListView) findViewById(R.id.lv_list);
        mScrollableLayout = (ScrollableLayout) findViewById(R.id.sr_layout);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(this);
        mInput = (EditText) findViewById(R.id.search_main);
        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String keyword = mInput.getText().toString().trim();
                    InputUtils.hideInput(SearchAllActivity.this, mInput);
                    search(keyword);
                    return true;
                }
                return false;
            }
        });
    }

    public abstract void search(String keyword);

    @Override
    public void onReload() {

    }

    @Override
    public View getScrollableView() {
        return mListView;
    }
}
