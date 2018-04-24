package com.zzkx.mtool.presenter;

import android.content.Context;
import android.content.Intent;

import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.bean.SearchContactBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.view.activity.UserDetailActivity;
import com.zzkx.mtool.view.iview.IContactSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/11/20.
 */

public class ContactSearchPresenter extends BasePresenter<IContactSearchView, SearchContactBean> {


    private int mSearchType;
    private Context mContext;
    public ContactSearchPresenter(IContactSearchView view) {
        super(view);
        mContext = getView().getContext();
    }

    public void search(String key) {
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.keyword = key;
        getHttpModel().request(API.SEARCH_CONTACT, requestBean);
    }

    @Override
    public void onSuccessM(SearchContactBean bean) {
        getView().showProgress(false);
        if (bean.cusData.size() > 0)
            getView().showData(bean);
        else
            getView().showEmpty();
    }

    @Override
    public void onSuccessWorkThread(SearchContactBean bean) {
        super.onSuccessWorkThread(bean);
        SearchContactBean.DataBean data = bean.data;
        bean.cusData = new ArrayList<>();
        if (data != null) {
            List<SearchContactBean.FanListBean> friendList = data.friendList;
            List<SearchContactBean.FanListBean> idolList = data.idolList;
            List<SearchContactBean.FanListBean> fanList = data.fanList;
            List<SearchContactBean.FanListBean> groupList = data.groupList;
            if (mSearchType == Const.FRIEND) {
                addData(null, bean.cusData, friendList, Const.FRIEND);
            } else {
                addData("好  友", bean.cusData, friendList, Const.FRIEND);
                addData("关  注", bean.cusData, idolList, Const.IDOLE);
                addData("粉  丝", bean.cusData, fanList, Const.FAN);
                addData("群  聊", bean.cusData, groupList, Const.GROUP);
            }
        }
    }


    private void addData(String title, List<Object> bean, List<SearchContactBean.FanListBean> friendList, int type) {
        if (friendList != null && friendList.size() > 0) {
            if (title != null)
                bean.add(title);
            for (SearchContactBean.FanListBean fanBean : friendList) {
                bean.add(fanBean);
                fanBean.cusType = type;
            }
        }
    }

    public void setSearchType(int searchType) {
        mSearchType = searchType;
    }

    public void onItemClick(Object item, int type) {
        if (type != 0) {
            SearchContactBean.FanListBean contactBaseInfoBean = (SearchContactBean.FanListBean) item;
            switch (type){
                case Const.FRIEND:
                case Const.FAN:
                case Const.IDOLE:
                    SearchContactBean.UserMemberBean userMember = contactBaseInfoBean.userMember;
                    mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra(Const.ID,userMember.id));
                    break;
                case Const.GROUP:
                    SearchContactBean.UserMemberBean chatGroup = contactBaseInfoBean.chatGroup;
                    break;
            }
        }
    }
}
