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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.activity.BaseActivity;
import com.zzkx.mtool.view.fragment.NewFriendFragment;
import com.zzkx.mtool.view.fragment.NewSearchFriendFragment;
import com.zzkx.mtool.view.fragment.SystemRcommendFragment;

import butterknife.BindView;

/**
 * Application and notification
 */
public class NewFriendsMsgActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.shop_top_tab)
    ViewGroup mTopTab;

    private SystemRcommendFragment mSystemRcommendFragment;
    private NewFriendFragment mNewFriendFragment;
    private NewSearchFriendFragment mNewSearchFriendFragment;

    @Override
    public int getContentRes() {
        return R.layout.layout_viewpager;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle("");
        mTopTab.setVisibility(View.VISIBLE);
        mSystemRcommendFragment = new SystemRcommendFragment();
        mNewFriendFragment = new NewFriendFragment();
        mNewSearchFriendFragment = new NewSearchFriendFragment();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mSystemRcommendFragment;
                    case 1:
                        return mNewFriendFragment;
                    case 2:
                        return mNewSearchFriendFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        new TabHelper().bind(mViewPager, mTopTab, new int[]{R.mipmap.ic_28, R.mipmap.ic_29, R.mipmap.ic_30}
                , new int[]{R.mipmap.ic_28_c, R.mipmap.ic_29_c, R.mipmap.ic_30_c}
        );

        int index = getIntent().getIntExtra(Const.INDEX, 0);
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onReload() {

    }

    public void back(View view) {
        finish();
    }
}
