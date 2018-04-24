package com.zzkx.mtool.presenter;

import android.text.TextUtils;

import com.zzkx.mtool.bean.AttentionUserBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.util.InitialUtil;
import com.zzkx.mtool.view.iview.IAttentionUserView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sshss on 2017/12/3.
 */

public class AttentionUserPresenter extends BasePresenter<IAttentionUserView, AttentionUserBean> {

    private boolean mToSort = true;
    private HashMap<String, Integer> mSetionIndexes = new HashMap<>();
    private List<AttentionUserBean.DataBean> mData;

    public AttentionUserPresenter(IAttentionUserView view) {
        super(view);
    }

    public AttentionUserPresenter(IAttentionUserView view, boolean toSorte) {
        super(view);
        mToSort = toSorte;
    }

    @Override
    public void onSuccessM(AttentionUserBean bean) {
        if (bean.data != null) {
            mData = bean.data;
            getView().showUserData(bean.data);
            getView().showProgress(false);
        } else {
            getView().showEmpty();
        }

    }

    public void getUserList(boolean isFans) {
        getView().showProgress(true);
        String url;
        if (isFans)
            url = API.MY_FANS;
        else
            url = API.ATTENTION_USERS;
        getHttpModel().request(url, null);
    }

    @Override
    public void onSuccessWorkThread(AttentionUserBean bean) {
        if (mToSort) {
            mSetionIndexes.clear();
            List<AttentionUserBean.DataBean> data = bean.data;
            if (data != null && data.size() > 0) {
                for (AttentionUserBean.DataBean dataBean : data) {
                    AttentionUserBean.UserMemberBean userMember = dataBean.userMember;
                    if (userMember != null) {
                        dataBean.cusInitial = InitialUtil.getInitial(userMember.nickname);
                    }
                }

                Collections.sort(data, new Comparator<AttentionUserBean.DataBean>() {

                    @Override
                    public int compare(AttentionUserBean.DataBean lhs, AttentionUserBean.DataBean rhs) {
                        if (lhs.cusInitial.equals(rhs.cusInitial)) {
                            if (lhs.userMember == null || rhs.userMember == null)
                                return 1;
                            return lhs.userMember.nickname.compareTo(rhs.userMember.nickname);
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

                for (int position = 0; position < data.size(); position++) {
                    AttentionUserBean.DataBean dataBean = data.get(position);
                    String header = dataBean.cusInitial;
                    if ((position == 0 || header != null && !header.equals(data.get(position - 1).cusInitial))) {
                        if (TextUtils.isEmpty(header)) {

                        } else {
                            mSetionIndexes.put(header, position);
                        }
                    }
                }
            }
        }
    }


    public Integer getSectionIndex(String alpah) {
        return mSetionIndexes.get(alpah);
    }

    public AttentionUserBean.DataBean getData(int position) {
        return mData.get(position);
    }
}
