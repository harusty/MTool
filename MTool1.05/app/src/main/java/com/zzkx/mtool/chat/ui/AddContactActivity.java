/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzkx.mtool.chat.ui;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.UserSearchBean;
import com.zzkx.mtool.presenter.AddAttenionPresenter;
import com.zzkx.mtool.presenter.AddContactPreseter;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.InputUtils;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.IAddContactView;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.iview.IAddAttentionView;

import java.util.List;

import butterknife.BindView;

public class AddContactActivity extends BaseActivity implements IAddContactView, IAddAttentionView {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.layout_contact)
    View mLayoutContact;
    @BindView(R.id.iv_head)
    ImageView mHeader;

    private AddContactPreseter mAddContactPreseter;
    private UserSearchBean.DataBean mUserBean;
    private List<UserSearchBean.DataBean> mData;
    private AddAttenionPresenter mAddAttenionPresenter;

    @Override
    public int getContentRes() {
        return R.layout.activity_add_contact;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("添加朋友");

        mLayoutContact.setVisibility(View.INVISIBLE);
        mAddContactPreseter = new AddContactPreseter(this);
        mAddAttenionPresenter = new AddAttenionPresenter(this);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String keyword = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        InputUtils.hideInput(AddContactActivity.this, mEtSearch);
                        search(keyword);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void search(String keyword) {
        mAddContactPreseter.search(keyword);
    }

    @Override
    public void onReload() {

    }

    public void addContact(View view) {
        final String toAddUsername = (String) mTvName.getTag();
        if (EMClient.getInstance().getCurrentUser().equals(toAddUsername)) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

//        if (DemoHelper.getInstance().getContactList().containsKey(toAddUsername)) {
//            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(toAddUsername)) {
//                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
//                return;
//            }
//            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
//            return;
//        }

        mAddAttenionPresenter.add(mUserBean.id);
//        mStateView.setCurrentState(StateView.ResultState.LOADING);
//
//        new Thread(new Runnable() {
//            public void run() {
//
//                try {
//                    //demo use a hardcode reason here, you need let user to input if you like
//                    String s = getResources().getString(R.string.Add_a_friend);
//
//                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            mStateView.setCurrentState(StateView.ResultState.SUCESS);
//                            String s1 = getResources().getString(R.string.send_successful);
//                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } catch (final Exception e) {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ToastUtils.showToast("添加失败，请重试");
//                        }
//                    });
//                }
//            }
//        }).start();
    }

    @Override
    public void showSearchResult(UserSearchBean bean) {
        if (bean.data != null && bean.data.size() > 0) {
            mLayoutContact.setVisibility(View.VISIBLE);
            mUserBean = bean.data.get(0);
            mTvName.setText(mUserBean.nickname);
            mTvName.setTag(mUserBean.hxUsername);
            GlideUtil.getInstance().display(mHeader, mUserBean.picUrl);
        } else {
            mLayoutContact.setVisibility(View.INVISIBLE);
            mTvName.setText("");
            mTvName.setTag(null);
            ToastUtils.showToast("未搜索到该用户");
        }
    }


    @Override
    public void showAttentionAddSuccess(BaseBean bean) {
        ToastUtils.showToast(bean.msg);

    }

    @Override
    public void showDelAttentionFaild(BaseBean bean) {

    }
}
