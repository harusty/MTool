package com.zzkx.mtool.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.ContactTagBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.AddContactTagPresenter;
import com.zzkx.mtool.presenter.CatDeletePresenter;
import com.zzkx.mtool.presenter.ContactTagsPresenter;
import com.zzkx.mtool.presenter.SearchUserTagPresenter;
import com.zzkx.mtool.presenter.SetMultiTagPresenter;
import com.zzkx.mtool.util.HeaderUtil;
import com.zzkx.mtool.util.ToastUtils;
import com.zzkx.mtool.view.iview.ISetTagView;
import com.zzkx.mtool.view.adapter.ContactTagsAdapter;
import com.zzkx.mtool.view.customview.DialogCollectionCatList;
import com.zzkx.mtool.view.customview.DialogContactTagsList;
import com.zzkx.mtool.view.iview.IAddContactTagView;
import com.zzkx.mtool.view.iview.IContactTagsView;
import com.zzkx.mtool.view.iview.IDeleteCatView;
import com.zzkx.mtool.view.iview.ISearchUserTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/4.
 */

public class TagsActivity extends BaseActivity implements IContactTagsView, IAddContactTagView,
        IDeleteCatView, ISetTagView, ISearchUserTagView {
    @BindView(R.id.lv_list)
    ListView mListView;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ContactTagsPresenter mContactTagsPresenter;
    private ContactTagsAdapter mAdapter;
    private List<ContactTagBean.DataBean> mData = new ArrayList<>();
    private View mFooter;
    private AddContactTagPresenter mAddContactTagPresenter;
    private DialogContactTagsList mDialogTagList;
    private CatDeletePresenter mCatDeletePresenter;
    private int mAction;
    public static final int ACTION_SELECT = 1;
    private SetMultiTagPresenter mSetMultiTagPresenter;
    private SearchUserTagPresenter mSearchUserTagPresenter;
    private String mUserId;
    private List<String> mUserTagData;

    @Override
    public int getContentRes() {
        return R.layout.layout_list;
    }

    @Override
    public void initViews() {
        mSwipeRefreshLayout.setEnabled(false);
        setMainMenuEnable();
        mAction = getIntent().getIntExtra(Const.ACTION, 0);

        if (mAction == ACTION_SELECT)
            setMainTitle("设置标签");
        else
            setMainTitle("标签管理");

        mContactTagsPresenter = new ContactTagsPresenter(this);
        mAddContactTagPresenter = new AddContactTagPresenter(this);
        mCatDeletePresenter = new CatDeletePresenter(this);

        if (mAction == 0) {
            initCatDialog();
            setSecMenu(new int[]{R.mipmap.ic_edit, R.mipmap.ic_tras_can},
                    new String[]{"编辑名称", "删除标签"}, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) v.getTag();
                            switch (position) {
                                case 0:
                                    mDialogTagList.show(DialogCollectionCatList.ACTION_EDIT, null);
                                    break;
                                case 1:
                                    mDialogTagList.show(DialogCollectionCatList.ACTION_DELETE, null);
                                    break;
                            }
                            mMainMenu.dismiss(false);
                        }
                    });

            mFooter = View.inflate(this, R.layout.item_contact_tag, null);
            ((TextView) mFooter.findViewById(R.id.tv_name)).setText("添加新标签");
            mFooter.findViewById(R.id.ic_arrow).setVisibility(View.GONE);
            mFooter.findViewById(R.id.ic_edit).setVisibility(View.VISIBLE);
            mFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(TagsActivity.this, CategoryNameEditActivity.class)
                            .putExtra(Const.TYPE, CategoryNameEditActivity.ACTION_CREATE), 9);
                }
            });
        } else {
            mUserId = getIntent().getStringExtra(Const.ID);
            mSearchUserTagPresenter = new SearchUserTagPresenter(this);
            mSetMultiTagPresenter = new SetMultiTagPresenter(this);
            mFooter = View.inflate(this, R.layout.item_footer_save, null);
            mFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> selectIds = mAdapter.getSelectId();
                    mSetMultiTagPresenter.setTag(mUserId,selectIds);
                }
            });
        }
        mListView.addFooterView(mFooter);
        HeaderUtil.addHeader(this, mListView, 20);
        notifyAdapter();
    }

    private void initCatDialog() {
        mDialogTagList = new DialogContactTagsList(TagsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactTagBean.DataBean> selectList = mDialogTagList.getSelectList();
                switch (mDialogTagList.getAction()) {
                    case DialogCollectionCatList.ACTION_DELETE:
                        if (selectList != null && selectList.size() > 0)
                            mCatDeletePresenter.deleteCate(selectList.get(0).id, API.DEL_CONTACT_TAG);
                        else
                            ToastUtils.showToast("请选择要删除的标签");
                        mDialogTagList.dismiss();
                        break;
                    case DialogCollectionCatList.ACTION_EDIT:
                        if (selectList == null || selectList.size() == 0) {
                            ToastUtils.showToast("请选择需要设置的标签");
                            return;
                        }
                        ContactTagBean.DataBean dataBean = selectList.get(0);
                        startActivityForResult(new Intent(TagsActivity.this, CategoryNameEditActivity.class)
                                .putExtra(Const.TYPE, CategoryNameEditActivity.ACTION_EDIT)
                                .putExtra(Const.NAME, dataBean.name)
                                .putExtra(Const.URL, API.EDIT_CONTACT_TAG)
                                .putExtra(Const.ID, dataBean.id), 9);
                        mDialogTagList.dismiss();
                        break;
                    case DialogCollectionCatList.ACTION_SELECT:
//                        if (selectList == null || selectList.size() == 0) {
//                            ToastUtils.showToast("请选择需要设置的标签");
//                            return;
//                        }
//                        mSetCategoryPresenter.setCatIds(mDialogTagList.getItem().collectId, selectList);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.RESULT_SUCESS_CODE) {
            int action = data.getIntExtra(Const.TYPE, 0);
            switch (action) {
                case CategoryNameEditActivity.ACTION_CREATE:
                    String name = data.getStringExtra(Const.NAME);
                    prepareAddTag(name);
                    break;
                case CategoryNameEditActivity.ACTION_EDIT:
                    initNet();
                    break;
            }
        }
    }

    private void prepareAddTag(final String name) {
        ((TextView) mFooter.findViewById(R.id.tv_name)).setText(name);
        ImageView edit = (ImageView) mFooter.findViewById(R.id.ic_edit);
        edit.setImageResource(R.mipmap.ic_check_red);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddContactTagPresenter.addContactTag(name);
            }
        });
    }

    private void notifyAdapter() {
        if (mAdapter == null) {
            mAdapter = new ContactTagsAdapter(mData);
            mAdapter.setAction(mAction);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position = position - mListView.getHeaderViewsCount();
                    if (position > -1) {
                        if (mAction == ACTION_SELECT) {
                            ContactTagsAdapter.ViewHolder holder = (ContactTagsAdapter.ViewHolder) view.getTag();
                            ContactTagBean.DataBean item = mAdapter.getItem(position);
                            item.cusSlected = !item.cusSlected;
                            holder.checkBox.setChecked(item.cusSlected);
                        } else {
                            ContactTagBean.DataBean item = mAdapter.getItem(position);
                            startActivity(new Intent(TagsActivity.this, ContactTagDetailActivity.class)
                                    .putExtra(Const.ID, item.id)
                            );
                        }
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initNet() {
        if (mAction == ACTION_SELECT) {
            mSearchUserTagPresenter.searchUserTag(mUserId);
        } else {
            mContactTagsPresenter.getTags();
        }
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showTags(ContactTagBean bean) {
        List<ContactTagBean.DataBean> data = bean.data;
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            if (mDialogTagList != null)
                mDialogTagList.setData(mData);
            if (mUserTagData != null && mUserTagData.size() > 0) {
                for (ContactTagBean.DataBean tagBean : data) {
                    if( mUserTagData.contains(tagBean.id))
                        tagBean.cusSlected = true;
                }
            }
            notifyAdapter();
        }
    }

    @Override
    public void showError(ErrorBean errorBean) {
        if (errorBean.url == API.CREATE_CONTACT_TAG) {
            ToastUtils.showToast("创建失败，请重试");
        } else {
            super.showError(errorBean);
        }

    }

    @Override
    public void showAddContactTag(BaseBean bean) {
        if (bean.status == 1) {
            resetAddTag();
            initNet();
        }
    }

    private void resetAddTag() {
        ((TextView) mFooter.findViewById(R.id.tv_name)).setText("添加新标签");
        ImageView edit = (ImageView) mFooter.findViewById(R.id.ic_edit);
        edit.setImageResource(R.mipmap.ic_edit);
    }

    @Override
    public void showDeleteResult(BaseBean bean) {
        mDialogTagList.dismiss();
        if (bean.status == 1) {
            initNet();
            mDialogTagList.resest();
        } else
            ToastUtils.showToast("删除失败，请重试");
    }

    @Override
    public void showTagSetResult(BaseBean bean) {
        if(bean.status == 1){
            ToastUtils.showToast("设置成功");
            finish();
        }else{
            ToastUtils.showToast("设置标签失败，请重试");
        }
    }

    @Override
    public void showUserTag(List<String> bean) {
        mUserTagData = bean;
        mContactTagsPresenter.getTags();
    }
}
