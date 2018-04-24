package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.AttentionShopBean;
import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.InitialBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AttentionShopPresenter;
import com.zzkx.mtool.presenter.AttentionUserPresenter;
import com.zzkx.mtool.util.InitialUtil;
import com.zzkx.mtool.view.activity.ShopDetailActivity;
import com.zzkx.mtool.view.activity.UserDetailActivity;
import com.zzkx.mtool.view.adapter.IdolAdapter;
import com.zzkx.mtool.view.iview.IAttentionShopView;
import com.zzkx.mtool.view.iview.IAttentionUserView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2018/1/17.
 */

public class IdolFragment extends ContactSubFragment {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private AttentionUserPresenter mAttentionUserPresenter;
    private AttentionShopPresenter mAttentionShopPresenter;
    private boolean userLoaded;
    private boolean shopLoaded;
    private boolean isError;
    private List<InitialBean> mTotalData = new ArrayList<>();
    private HashMap<String, Integer> mSetionIndexes = new HashMap<>();
    private FragmentActivity mActivity;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        setTitleDisable();
        mActivity = getActivity();
        mSwipeRefreshLayout.setEnabled(false);
        mAttentionUserPresenter = new AttentionUserPresenter(new IAttentionUserView() {
            @Override
            public void showUserData(List<AttentionUserBean.DataBean> data) {
                userLoaded = true;
                mTotalData.addAll(data);
                System.out.println("handleData showUserData" + data.size());
                handleData();
            }

            @Override
            public void showEmpty() {
                userLoaded = true;
                System.out.println("handleData showEmpty showUserData" + mTotalData.size());
                handleData();
            }

            @Override
            public void showProgress(boolean toShow) {
                IdolFragment.this.showProgress(toShow);
            }

            @Override
            public void showError(ErrorBean errorBean) {
                IdolFragment.this.showError(errorBean);
            }
        }, false);
        mAttentionShopPresenter = new AttentionShopPresenter(new IAttentionShopView() {
            @Override
            public void showShopData(List<AttentionShopBean.DataBean> data) {
                shopLoaded = true;
                mTotalData.addAll(data);
                System.out.println("handleData showShopData" + data.size());
                handleData();
            }

            @Override
            public void showEmpty() {
                shopLoaded = true;
                System.out.println("handleData showEmpty showShopData" + mTotalData.size());
                handleData();
            }

            @Override
            public void showProgress(boolean toShow) {
                IdolFragment.this.showProgress(toShow);
            }

            @Override
            public void showError(ErrorBean errorBean) {
                IdolFragment.this.showError(errorBean);
            }
        }, false);
    }

    @Override
    public void initNet() {
        mTotalData.clear();
        mAttentionUserPresenter.getUserList(false);
        mAttentionShopPresenter.getShopList();
    }

    private void handleData() {
        if (shopLoaded && userLoaded) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mSetionIndexes.clear();
                    if (mTotalData != null && mTotalData.size() > 0) {
                        for (InitialBean dataBean : mTotalData) {
                            if (dataBean instanceof AttentionShopBean.DataBean) {
                                dataBean.cusName = ((AttentionShopBean.DataBean) dataBean).shopName;
                                dataBean.cusHead = ((AttentionShopBean.DataBean) dataBean).shopLogo;
                            } else {
                                dataBean.cusName = ((AttentionUserBean.DataBean) dataBean).userMember.nickname;
                                dataBean.cusHead = ((AttentionUserBean.DataBean) dataBean).userMember.picUrl;
                            }
                            dataBean.cusInitial = InitialUtil.getInitial(dataBean.cusName);
                        }

                        Collections.sort(mTotalData, new Comparator<InitialBean>() {

                            @Override
                            public int compare(InitialBean lhs, InitialBean rhs) {
                                if (lhs.cusInitial.equals(rhs.cusInitial)) {
                                    return lhs.cusName.compareTo(rhs.cusName);
                                } else {
                                    if ("#".equals(lhs.cusInitial)) {
                                        return 1;
                                    } else if ("#".equals(rhs.cusInitial)) {
                                        return -1;
                                    }
                                    return lhs.cusInitial.compareTo(rhs.cusInitial);
                                }

                            }
                        });

                        for (int position = 0; position < mTotalData.size(); position++) {
                            InitialBean dataBean = mTotalData.get(position);
                            String header = dataBean.cusInitial;
                            if ((position == 0 || header != null && !header.equals(mTotalData.get(position - 1).cusInitial))) {
                                if (TextUtils.isEmpty(header)) {

                                } else {
                                    mSetionIndexes.put(header, position);
                                }
                            }
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setAdapter(new IdolAdapter(mTotalData));
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Object item = mListView.getAdapter().getItem(position);
                                        if (item instanceof AttentionUserBean.DataBean) {
                                            AttentionUserBean.UserMemberBean userMember = ((AttentionUserBean.DataBean) item).userMember;
                                            if (userMember != null)
                                                startActivity(new Intent(mActivity, UserDetailActivity.class)
                                                        .putExtra(Const.ID, userMember.id));
                                        } else if (item instanceof AttentionShopBean.DataBean) {
                                            startActivity(new Intent(mActivity, ShopDetailActivity.class).putExtra(Const.ID, ((AttentionShopBean.DataBean) item).shopId));
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void showError(ErrorBean errorBean) {
        if (isError)
            return;
        super.showError(errorBean);
        mAttentionUserPresenter.getHttpModel().cancleAll();
        mAttentionShopPresenter.getHttpModel().cancleAll();
        isError = true;
    }

    @Override
    public void showProgress(boolean toShow) {
        if (isError)
            return;
        boolean flag = !toShow && shopLoaded && userLoaded;
        super.showProgress(!flag);
    }

    @Override
    public void onReload() {
        userLoaded = false;
        shopLoaded = false;
        isError = false;
        initNet();
    }

    @Override
    void onInitialClick(View view) {
        String alpah = (String) view.getTag();
        Integer sectionIndex = mSetionIndexes.get(alpah);
        if (sectionIndex != null) {
            mListView.setSelection(sectionIndex);
        }
    }
}
