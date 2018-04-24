package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;
import com.zzkx.mtool.util.SPUtil;
import com.zzkx.mtool.util.TabHelper;
import com.zzkx.mtool.view.fragment.BaseFragment;
import com.zzkx.mtool.view.fragment.MineFragment;
import com.zzkx.mtool.view.fragment.SettingFragment;

import butterknife.BindView;

/**
 * Created by sshss on 2017/8/26.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.shop_top_tab)
    ViewGroup mTopTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_user_header)
    ImageView mIVUserHeader;
    @BindView(R.id.tv_user_nick)
    TextView mTvUserNick;
    @BindView(R.id.tv_user_intro)
    TextView mTvUserIntro;
    private SparseArray<BaseFragment> mCache;

    @Override
    public int getContentRes() {
        return R.layout.activity_mine;
    }

    @Override
    public void initViews() {
//        new MinePresenter(this)
        mTopTab.setVisibility(View.VISIBLE);
        mTopTab.getChildAt(mTopTab.getChildCount() - 1).setVisibility(View.INVISIBLE);
        setMainMenuEnable();
        setMainTitleGone();

        initPager();
        TabHelper tabHelper = new TabHelper();
        tabHelper.bind(mViewPager, mTopTab, new int[]{R.mipmap.ic_top_mine, R.mipmap.ic_top_settting}
                , new int[]{R.mipmap.ic_top_mine_gray, R.mipmap.ic_top_setting_gray});
        findViewById(R.id.layout_mine).setOnClickListener(this);

//        DemoModel demoModel = new DemoModel(this);
//        EaseUser easeUser = new EaseUser("hx1505308274022");
//        easeUser.setNickname("兔子");
//        easeUser.setAvatar("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1302382899,1761475552&fm=27&gp=0.jpg");
//        demoModel.saveContact(easeUser);

//        PreferenceManager.getInstance().setCurrentUserAvatar(SPUtil.getString(Const.USER_HEADER, ""));
    }


    @Override
    protected void onResume() {
        super.onResume();
        String hederUrl = SPUtil.getString(Const.USER_HEADER, "");
        GlideUtil.getInstance().display(mIVUserHeader, hederUrl);
        mTvUserIntro.setText(SPUtil.getString(Const.USER_INTRO, ""));
        mTvUserNick.setText(SPUtil.getString(Const.USER_NICK, ""));
    }

    private void initPager() {
        mCache = new SparseArray<>();
        mCache.put(0, new MineFragment());
        mCache.put(1, new SettingFragment());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCache.get(position);
            }

            @Override
            public int getCount() {
                return mCache.size();
            }
        });
    }

    @Override
    public void initNet() {
    }

    @Override
    public void onReload() {

    }

    public void goQrCard(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_mine:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
        }
    }
}
