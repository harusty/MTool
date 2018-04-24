package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.BaseCollectBean;
import com.zzkx.mtool.bean.ChatShareBean;
import com.zzkx.mtool.bean.CollectionCatBean;
import com.zzkx.mtool.bean.CusCollectBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.CancleCollectionPresenter;
import com.zzkx.mtool.presenter.CatDeletePresenter;
import com.zzkx.mtool.presenter.CatNameAddPresenter;
import com.zzkx.mtool.presenter.CollectCatListPreseter;
import com.zzkx.mtool.presenter.CollectListPresenter;
import com.zzkx.mtool.presenter.SetCategoryPresenter;
import com.zzkx.mtool.util.AnimationExecutor;
import com.zzkx.mtool.util.LocateUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.MToolShareActivity;
import com.zzkx.mtool.view.customview.DialogCollectionCatList;
import com.zzkx.mtool.view.customview.DialogCollectionMore;
import com.zzkx.mtool.view.customview.DialogState;
import com.zzkx.mtool.view.customview.StateView;
import com.zzkx.mtool.view.iview.ICancleCollectionView;
import com.zzkx.mtool.view.iview.ICatNameAddView;
import com.zzkx.mtool.view.iview.ICatSetView;
import com.zzkx.mtool.view.iview.ICollectCatListView;
import com.zzkx.mtool.view.iview.ICollectListView;
import com.zzkx.mtool.view.iview.IDeleteCatView;

import java.util.List;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by sshss on 2017/9/30.
 */

public abstract class CollectListActivity extends BaseActivity implements
        ICollectListView, ICollectCatListView, ICatNameAddView,
        IDeleteCatView, ICatSetView, ICancleCollectionView {
    @BindView(R.id.lv_list)
    public ExpandableStickyListHeadersListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CollectListPresenter mCollectListPresenter;
    public AMapLocation mMyLocation;
    private DialogCollectionCatList mDialogCollectionCatList;
    private CollectCatListPreseter mCollectCatListPreseter;
    private ImageView mIvEdit;
    private TextView mHeaderTitle;
    private CatNameAddPresenter mCatNameAddPresenter;
    private CatDeletePresenter mCatDeletePresenter;
    private SetCategoryPresenter mSetCategoryPresenter;
    public DialogCollectionMore mDialogCollectionMore;
    private CancleCollectionPresenter mCancleCollectionPresenter;
    DialogState mDialogState;

    @Override
    public int getContentRes() {
        return R.layout.layout_sticky_list;
    }


    @Override
    public void initViews() {
        setMainMenuEnable();
        mCollectListPresenter = new CollectListPresenter(this);
        mCollectCatListPreseter = new CollectCatListPreseter(this);
        mCatNameAddPresenter = new CatNameAddPresenter(this);
        mCatDeletePresenter = new CatDeletePresenter(this);
        mSetCategoryPresenter = new SetCategoryPresenter(this);
        mCancleCollectionPresenter = new CancleCollectionPresenter(this);
        setSecMenu(new int[]{R.mipmap.ic_edit, R.mipmap.ic_tras_can},
                new String[]{"编辑名称", "删除分类"}, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        switch (position) {
                            case 0:
                                mDialogCollectionCatList.show(DialogCollectionCatList.ACTION_EDIT, null);
                                break;
                            case 1:
                                mDialogCollectionCatList.show(DialogCollectionCatList.ACTION_DELETE, null);
                                break;
                        }
                        mMainMenu.dismiss(false);
                    }
                });
        mRefreshLayout.setEnabled(false);

        View header = View.inflate(this, R.layout.header_collection_list, null);
        mHeaderTitle = (TextView) header.findViewById(R.id.title);
        mListView.addHeaderView(header);
        mListView.setAnimExecutor(new AnimationExecutor());

        mIvEdit = (ImageView) header.findViewById(R.id.iv_edit);
        mIvEdit.setTag(false);
        mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toSubmit = (boolean) v.getTag();
                if (toSubmit) {
                    mCatNameAddPresenter.setName(mHeaderTitle.getText().toString(), getCollectType());
                } else {
                    startActivityForResult(new Intent(CollectListActivity.this, CategoryNameEditActivity.class)
                            .putExtra(Const.TYPE, CategoryNameEditActivity.ACTION_CREATE), 9);
                }
            }
        });


        mListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (mListView.isHeaderCollapsed(headerId)) {
                    mListView.expand(headerId);
                } else {
                    mListView.collapse(headerId);
                }
            }
        });

        mDialogCollectionCatList = new DialogCollectionCatList(CollectListActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CollectionCatBean.DataBean> selectList = mDialogCollectionCatList.getSelectList();
                switch (mDialogCollectionCatList.getAction()) {
                    case DialogCollectionCatList.ACTION_DELETE:
                        if (selectList != null && selectList.size() > 0)
                            mCatDeletePresenter.deleteCate(selectList.get(0).id);
                        else
                            ToastUtils.showToast("请选择要删除的分类");
                        break;
                    case DialogCollectionCatList.ACTION_EDIT:
                        if (selectList == null || selectList.size() == 0) {
                            ToastUtils.showToast("请选择需要设置的分类");
                            return;
                        }
                        CollectionCatBean.DataBean dataBean = selectList.get(0);
                        startActivityForResult(new Intent(CollectListActivity.this, CategoryNameEditActivity.class)
                                .putExtra(Const.TYPE, CategoryNameEditActivity.ACTION_EDIT)
                                .putExtra(Const.NAME, dataBean.name)
                                .putExtra(Const.ID, dataBean.id), 9);
                        mDialogCollectionCatList.dismiss();
                        break;
                    case DialogCollectionCatList.ACTION_SELECT:
                        if (selectList == null || selectList.size() == 0) {
                            ToastUtils.showToast("请选择需要设置的分类");
                            return;
                        }
                        mDialogCollectionCatList.dismiss();
                        mSetCategoryPresenter.setCatIds(mDialogCollectionCatList.getItem().collectId, selectList);
                        break;
                }
            }
        });


        mDialogCollectionMore = new DialogCollectionMore(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseCollectBean item = (BaseCollectBean) v.getTag();
                switch (v.getId()) {
                    case R.id.layout_1:
                        mDialogState.show("",0);
                        mDialogCollectionMore.dismiss();
                        break;
                    case R.id.layout_2:
                        String id = item.id;
                        if (getCollectType() == 1)
                            id = item.goodsId;
                        mCancleCollectionPresenter.cancleCollection(id, getCollectType());
                        break;
                    case R.id.layout_3:
                        mDialogCollectionCatList.show(DialogCollectionCatList.ACTION_SELECT, item);
                        mDialogCollectionMore.dismiss();
                        break;
                }
            }
        });

        mDialogState = new DialogState(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatShareBean shareBean = getShareBean();
                if (v.getId() == R.id.layout_share_mtool_friend) {
                    startActivity(new Intent(getApplicationContext(), MToolShareActivity.class)
                            .putExtra(Const.SHARE_INFO, Json_U.toJson(shareBean)));
                } else {
                    mDialogState.onShare(v.getId(), shareBean);
                }
                mDialogState.dismiss();
            }
        });
        mDialogState.hideActionMenu();
    }

    @Override
    public void initNet() {
        showProgress(true);
        LocateUtil.getInstance(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    mCollectListPresenter.getData(getCollectType());
                    mMyLocation = aMapLocation;
                } else {
                    showProgress(false);
                    mStateView.setCurrentState(StateView.ResultState.ERROR);
                }
            }
        }).locate();
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Const.RESULT_SUCESS_CODE) {
            int type = data.getIntExtra(Const.TYPE, CategoryNameEditActivity.ACTION_CREATE);
            switch (type) {
                case CategoryNameEditActivity.ACTION_CREATE:
                    String name = data.getStringExtra(Const.NAME);
                    mHeaderTitle.setText(name);
                    mIvEdit.setImageResource(R.mipmap.ic_check_red);
                    mIvEdit.setTag(true);
                    break;
                case CategoryNameEditActivity.ACTION_EDIT:
                    mCollectCatListPreseter.getCatList(getCollectType());
                    break;

            }
        }
    }

    @Override
    public void showCollectList(final CusCollectBean cusCollectBean) {
        mCollectCatListPreseter.getCatList(getCollectType());
        mListView.setAdapter(getListAdapter(cusCollectBean));
//        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
//        mListView.setDividerHeight(Dip2PxUtils.dip2px(this,5));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < cusCollectBean.mCollectedShops.size(); i++) {
//                    CollectionBean.MerchantRestaurantsListBean merchantRestaurantsListBean = cusCollectBean.mCollectedShops.get(i);
//                    mListView.collapse(merchantRestaurantsListBean.cusGroupPosition);
//                }
//            }
//        }, 500);
    }

    protected abstract StickyListHeadersAdapter getListAdapter(CusCollectBean cusCollectBean);


    @Override
    public void showCatNameList(List<CollectionCatBean.DataBean> data) {
        mDialogCollectionCatList.setData(data);
    }

    @Override
    public void showAddResult(BaseBean bean) {
        if (bean.status == 1) {
            mIvEdit.setImageResource(R.mipmap.ic_edit);
            mIvEdit.setTag(false);
            mHeaderTitle.setText("添加分类");
            ToastUtils.showToast("添加成功");
        } else {
            ToastUtils.showToast("添加失败");
        }
        mCollectCatListPreseter.getCatList(getCollectType());

    }

    @Override
    public void showDeleteResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("删除成功");
            mDialogCollectionCatList.dismiss();
            mCollectCatListPreseter.getCatList(getCollectType());
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showSetCategoryResult(BaseBean bean) {
        if (bean.status == 1) {
            ToastUtils.showToast("设置分类成功");
            mDialogCollectionCatList.dismiss();
            mCollectListPresenter.getData(getCollectType());
        } else {
            ToastUtils.showToast("设置分类失败");
        }
    }

    @Override
    public void showCancleCollectResult(BaseBean bean) {
        if (bean.status == 1) {
            mDialogCollectionMore.dismiss();
            ToastUtils.showToast("取消收藏成功");
            mCollectListPresenter.getData(getCollectType());
        } else {
            ToastUtils.showToast(bean.msg);
        }
    }

    @Override
    public void showError(ErrorBean errorBean) {
        showProgress(false);
        ToastUtils.showToast(getString(R.string.netErroRetry));
    }

    public abstract int getCollectType();

    public abstract ChatShareBean getShareBean();
}
