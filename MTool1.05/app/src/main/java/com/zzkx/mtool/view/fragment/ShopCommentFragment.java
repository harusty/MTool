package com.zzkx.mtool.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ShopCommentListBean;
import com.zzkx.mtool.bean.StateListBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.BaseListPresenter;
import com.zzkx.mtool.presenter.ShopCommentListPresenter;
import com.zzkx.mtool.presenter.ShopCommentSupportCanclePresenter;
import com.zzkx.mtool.presenter.ShopCommentSupportPresenter;
import com.zzkx.mtool.util.Dip2PxUtils;
import com.zzkx.mtool.util.HeadClickUtil;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.activity.ShopCommentDetailActivity;
import com.zzkx.mtool.view.activity.StateImgActivity;
import com.zzkx.mtool.view.adapter.ShopCommentAdapter;
import com.zzkx.mtool.view.iview.IShopCommentListView;
import com.zzkx.mtool.view.iview.ISupportCancleView;
import com.zzkx.mtool.view.iview.ISupportView;

import java.util.ArrayList;

/**
 * Created by sshss on 2017/10/14.
 */

public class ShopCommentFragment extends BaseListFragment<ShopCommentListBean.DataBean> implements IShopCommentListView, ISupportView, ISupportCancleView {
    private ShopCommentListPresenter mPresenter;
    private int mClickPosition;
    private ShopCommentAdapter mShopCommentAdapter;
    private ShopCommentSupportPresenter mShopCommentSupportPresenter;
    private ShopCommentSupportCanclePresenter mShopCommentSupportCanclePresenter;

    @Override
    public BaseAdapter getAdapter() {
        mShopCommentAdapter = new ShopCommentAdapter(getActivity(), getTotalData(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag instanceof Integer) {
                    mClickPosition = (int) tag;
                } else {
                    mClickPosition = (int) v.getTag(R.id.child_index);
                }
                ShopCommentListBean.DataBean item = mShopCommentAdapter.getItem(mClickPosition);
                switch (v.getId()) {
                    case R.id.iv_user_header:
                        HeadClickUtil.handleClick(getContext(), item.memId, null);
                        break;
                    case R.id.ic_msg:
                        startActivityForResult(new Intent(getActivity(), ShopCommentDetailActivity.class)
                                .putExtra(Const.TO_REPLY, true)
                                .putExtra(Const.ID, item.id), 9
                        );
                        break;
                    case R.id.ic_support:
                        if (item.suppoppType == 1) {
                            mShopCommentSupportCanclePresenter.cancleSupport(item.id, mClickPosition);
                        } else {
                            mShopCommentSupportPresenter.support(item.id, mClickPosition);
                        }
                        break;
                }
            }
        });
        mShopCommentAdapter.setOnResClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int parentIndex = (int) v.getTag(R.id.parent_index);
                int childIndex = (int) v.getTag(R.id.child_index);
                ShopCommentListBean.DataBean item = mShopCommentAdapter.getItem(parentIndex);
                ArrayList<StateListBean.ResData> list = new ArrayList<StateListBean.ResData>();
                for (ShopCommentListBean.DataBean.MerchantRes res : item.merchantRestaurantsCommentImgs) {
                    StateListBean.ResData resData = new StateListBean.ResData();
                    resData.resourceUrl = res.url;
                    list.add(resData);
                }
                startActivity(new Intent(getActivity(), StateImgActivity.class)
                        .putExtra(Const.RES, list)
                        .putExtra(Const.INDEX, childIndex)
                );
            }
        });
        return mShopCommentAdapter;
    }

    @Override
    public BaseListPresenter getPresenter() {
        String id = getActivity().getIntent().getStringExtra(Const.ID);
        mPresenter = new ShopCommentListPresenter(this, id);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                if (position >= 0) {
                    ShopCommentListBean.DataBean item = (ShopCommentListBean.DataBean) mAdapter.getItem(position);
                    startActivityForResult(new Intent(getActivity(), ShopCommentDetailActivity.class).putExtra(Const.TO_REPLY, false)
                            .putExtra(Const.ID, item.id)
                            .putExtra(Const.TYPE, mShopCommentAdapter.getItemViewType(position)), 9);
                }

            }
        });
        return mPresenter;
    }

    @Override
    public void initNet() {
        mPresenter.getListData(1);
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleDisable();
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(Dip2PxUtils.dip2px(getContext(), 10));
        HeaderUtil.addHeader(getContext(), mListView, 10);
        mShopCommentSupportPresenter = new ShopCommentSupportPresenter(this);
        mShopCommentSupportCanclePresenter = new ShopCommentSupportCanclePresenter(this);
    }

    @Override
    public void onSuppotedSuccess(BaseBean bean) {
        if (bean.status == 1) {
            String cusTag = (String) bean.cusTag;
            if (!TextUtils.isEmpty(cusTag)) {

                ShopCommentListBean.DataBean dataBean = mTotalData.get(Integer.parseInt(cusTag));
                dataBean.praises++;
                dataBean.suppoppType = 1;
                mShopCommentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showCancleSupportResult(BaseBean bean) {
        if (bean.status == 1) {
            if (bean.cusTag == null) {
                initNet();
                return;
            }
            int cusTag = (int) bean.cusTag;
            ShopCommentListBean.DataBean dataBean = mTotalData.get(cusTag);
            dataBean.praises--;
            dataBean.suppoppType = 0;
            mShopCommentAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }
}
