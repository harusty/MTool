package com.zzkx.mtool.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.view.customview.scrollablelayout.ScrollableLayout;
import com.zzkx.mtool.view.fragment.SearchFragment;
import com.zzkx.mtool.view.fragment.SearchHistoryFragment;
import com.zzkx.mtool.view.iview.ISearchView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/9/15.
 */

public abstract class SearchActivity extends BaseActivity implements ISearchView, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.rb_1)
    RadioButton mRb1;
    @BindView(R.id.rb_2)
    RadioButton mRb2;
    @BindView(R.id.search_main)
    EditText mEtSearch;
    @BindView(R.id.scrolable_layout)
    ScrollableLayout mScrollableLayout;
    @BindView(R.id.layout_option)
    View mLayoutOption;
    @BindView(R.id.tv_cat1)
    TextView mCat1;
    @BindView(R.id.tv_cat2)
    TextView mCat2;

    public static final int TYPE_1 = 1;
    public static final int TYPE_0 = 0;
    private int mType = 0;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String keyword = s.toString();
            if (!TextUtils.isEmpty(keyword)) {
//                searchMain(keyword);
            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (mSecFragment != null)
                    fragmentTransaction.hide(mSecFragment);
                fragmentTransaction.hide(mFirstFragment)
                        .show(mSearchHistoryFragment)
                        .commit();
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mSearchHistoryFragment);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    protected abstract String getHintText();

    protected abstract String getMainTitle();

    protected abstract SearchFragment getSecFragment();

    protected abstract SearchFragment getFirsFragment();

    public abstract int getSearchType();


    public void search(String keword) {
        InputUtils.hideSoftKeyboard(this);
        mEtSearch.setText(keword);
        mEtSearch.setSelection(keword.length());
        switch (mType) {
            case TYPE_1:
                if (mSecFragment.isHidden()) {
                    getSupportFragmentManager().beginTransaction()
                            .hide(mSearchHistoryFragment)
                            .hide(mFirstFragment)
                            .show(mSecFragment).commit();
                }
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mSecFragment);
                mSecFragment.search(keword);
                break;
            case TYPE_0:
                if (mFirstFragment.isHidden()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                            .hide(mSearchHistoryFragment);
                    if (mSecFragment != null)
                        transaction.hide(mSecFragment);
                    transaction.show(mFirstFragment).commit();
                }
                mScrollableLayout.getHelper().setCurrentScrollableContainer(mFirstFragment);
                mFirstFragment.search(keword);
                break;
        }
    }

    private SearchHistoryFragment mSearchHistoryFragment;
    private SearchFragment mFirstFragment;
    private SearchFragment mSecFragment;

    @Override
    public int getContentRes() {
        return R.layout.activity_food_shop_search;
    }

    @Override
    public void initViews() {
        setMainTitle(getMainTitle());
        setMainMenuEnable();
        mRb1.setOnCheckedChangeListener(this);
        mRb2.setOnCheckedChangeListener(this);
        mEtSearch.setHint(getHintText());
        mEtSearch.addTextChangedListener(mTextWatcher);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String keyword = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                    InputUtils.hideInput(SearchActivity.this, mEtSearch);
                    search(keyword);
                    }
                    return true;
                }
                return false;
            }
        });


        mSearchHistoryFragment = new SearchHistoryFragment();
        mFirstFragment = getFirsFragment();
        mSecFragment = getSecFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.fr_container, mSearchHistoryFragment)
                .add(R.id.fr_container, mFirstFragment);
        if (mSecFragment != null) {
            transaction.add(R.id.fr_container, mSecFragment)
                    .hide(mSecFragment);
            mCat1.setText(getCat1Title());
            mCat2.setText(getCat2Title());
        } else {
            mLayoutOption.setVisibility(View.GONE);
        }
        transaction.hide(mFirstFragment)
                .show(mSearchHistoryFragment)
                .commit();
        mScrollableLayout.getHelper().setCurrentScrollableContainer(mSecFragment);

    }


    @Override
    public void onReload() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_1:
                if (isChecked) {
                    mType = TYPE_0;
                    mRb2.setChecked(false);
                }
                break;
            case R.id.rb_2:
                if (isChecked) {
                    mType = TYPE_1;
                    mRb1.setChecked(false);
                }
                break;
        }
        String keyword = mEtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            search(keyword);
        }
    }

    public void refreshHistoryLayout(String keyword) {
        mSearchHistoryFragment.refreshHistory(keyword);
    }

    public String getCat1Title() {
        return null;
    }

    public String getCat2Title() {
        return null;
    }
}
