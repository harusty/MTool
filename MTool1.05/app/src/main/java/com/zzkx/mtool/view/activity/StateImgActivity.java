package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.GlideUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/5.
 */

public class StateImgActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_page_count)
    TextView mPageCount;
    private int mIndex;
    private ArrayList<StateListBean.ResData> mResData;
    private ImgAdapter mAdapter;

    @Override
    public int getContentRes() {
        return R.layout.activity_state_img;
    }


    @Override
    public void initViews() {
        setTitleDisable();
        findViewById(R.id.layout_back).setOnClickListener(this);
        mResData = (ArrayList<StateListBean.ResData>) getIntent().getSerializableExtra(Const.RES);

        mIndex = getIntent().getIntExtra(Const.INDEX, 0);
        mPageCount.setText((mIndex + 1) + "/" + mResData.size());

    }

    @Override
    public void initNet() {
        mAdapter = new ImgAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                mPageCount.setText((mIndex + 1) + "/" + mResData.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ImgAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mResData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup view = (ViewGroup) View.inflate(MyApplication.getContext(), R.layout.item_res_gallary, null);
            EasePhotoView easePhotoView = (EasePhotoView) view.getChildAt(0);
            View transView = view.getChildAt(1);
            final StateListBean.ResData resData = mResData.get(position);
            if (resData.type == 1) {
                GlideUtil.getInstance().display(easePhotoView, resData.coverUrl);
                transView.setVisibility(View.VISIBLE);
                transView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StateImgActivity.this, VideoPlayerAcitivity.class)
                                .putExtra(Const.URL, resData.resourceUrl)
                                .putExtra(Const.COVER_URL, resData.coverUrl)
                                .putExtra(Const.PLAY, true)
                        );
                    }
                });
            } else {
                GlideUtil.getInstance().display(easePhotoView, resData.resourceUrl);
                transView.setVisibility(View.INVISIBLE);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
        }
    }
}
