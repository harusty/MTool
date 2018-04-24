package com.zzkx.mtool.chat.util;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.Json_U;
import com.zzkx.mtool.bean.ContactBaseInfoBean;
import com.zzkx.mtool.bean.ConversationExtBean;
import com.zzkx.mtool.bean.ErrorBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.chat.DemoHelper;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.model.HttpModel;
import com.zzkx.mtool.presenter.ipresenter.IPresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created by sshss on 2017/11/20.
 */

public class ContactUtil {

    private CallBack mCallBack;
    private HttpModel mHttpModel;

    private ContactUtil(CallBack callback) {
        mCallBack = callback;
    }

    public static ContactUtil getInstance(CallBack callback) {
        ContactUtil contactUtil = new ContactUtil(callback);
        contactUtil.setCallBack(callback);
        return contactUtil;
    }

    public void updateEase(Collection<EaseUser> users) {
        if (users == null || users.size() == 0) {
            System.out.println("ContactUpdateUtil：Collection<EaseUser> users empty");
            return;
        }
        List<String> ids = new ArrayList<>();
        for (EaseUser user : users) {
            ids.add(user.getUsername());
        }
        update(ids);
    }

    public void update(List<String> userIds) {
        System.out.println("ContactUtil  :updating!!!!!!!!!!!!!!!!!!!!!!!");
        if (userIds == null || userIds.size() == 0) {
            return;
        }
        mHttpModel = new HttpModel(new IPresenter() {
            @Override
            public void onSuccess(String json, String url, Object tag) {
                ContactBaseInfoBean contactBaseInfoBean = Json_U.fromJson(json, ContactBaseInfoBean.class);
                List<ContactBaseInfoBean.DataBean> data = contactBaseInfoBean.data;
                List<EaseUser> updatelist = new ArrayList<>();
                if (data != null && data.size() > 0) {

                    for (ContactBaseInfoBean.DataBean dataBean : data) {
                        EaseUser easeUser = new EaseUser(dataBean.hxUsername);
                        easeUser.setMtoolId(dataBean.id);
                        if (!TextUtils.isEmpty(dataBean.picUrl))
                            easeUser.setAvatar(dataBean.picUrl);
                        if (!TextUtils.isEmpty(dataBean.nickname))
                            easeUser.setNickname(dataBean.nickname);

                        updatelist.add(easeUser);
                    }

                    if (updatelist.size() > 0) {
                        DemoHelper.getInstance().updateContactList(updatelist);
                    }
                    System.out.println("ContactUtil  :update sucess!!!!!!!!!!!!!!!!!!!!!!!");
                }

                if (mCallBack != null)
                    mCallBack.onSuccess(DemoHelper.getInstance().getContactList());
            }

            @Override
            public void onConnectFaild(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }

            @Override
            public void onResponseError(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }
        });


        RequestBean requestBean = new RequestBean();
        requestBean.hxNames = userIds;
        mHttpModel.request(API.CONTACT_INFO_UPDATE, requestBean);
    }

    public void update(String userId) {
        System.out.println("ContactUtil   :updating single!!!!!!!!!!!!!!!!!!!!!!!");
        if (userId == null) {
            return;
        }
        mHttpModel = new HttpModel(new IPresenter() {
            @Override
            public void onSuccess(String json, String url, Object tag) {
                ContactBaseInfoBean contactBaseInfoBean = Json_U.fromJson(json, ContactBaseInfoBean.class);
                List<ContactBaseInfoBean.DataBean> data = contactBaseInfoBean.data;
                List<EaseUser> updatelist = new ArrayList<>();
                if (data != null && data.size() > 0) {
                    ContactBaseInfoBean.DataBean dataBean = data.get(0);
                    EaseUser easeUser = new EaseUser(dataBean.hxUsername);
                    easeUser.setMtoolId(dataBean.id);
                    if (!TextUtils.isEmpty(dataBean.picUrl))
                        easeUser.setAvatar(dataBean.picUrl);
                    if (!TextUtils.isEmpty(dataBean.nickname))
                        easeUser.setNickname(dataBean.nickname);
                    updatelist.add(easeUser);

                    if (updatelist.size() > 0) {
                        DemoHelper.getInstance().updateContactList(updatelist);
                    }
                    System.out.println("ContactUtil  :update sucess  single!!!!!!!!!!!!!!!!!!!!!!!");
                }

                if (mCallBack != null)
                    mCallBack.onSuccess(DemoHelper.getInstance().getContactList());
            }

            @Override
            public void onConnectFaild(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }

            @Override
            public void onResponseError(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }
        });


        RequestBean requestBean = new RequestBean();
        requestBean.hxNames = new ArrayList<>();
        requestBean.hxNames.add(userId);
        mHttpModel.request(API.CONTACT_INFO_UPDATE, requestBean);
    }

    public void getContact() {
//        if (mCallBack != null)
//            mCallBack.onStart();
        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
//        if (contactList.size() == 0) {
//            DemoHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
//                @Override
//                public void onSuccess(List<String> strings) {
//                    if (strings.size() > 0) {
//                        update(strings);
//                    } else {
//                        if (mCallBack != null)
//                            mCallBack.onSuccess(null);
//                    }
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    if (mCallBack != null)
//                        mCallBack.onFiald();
//                }
//            });
//        } else {
        if (mCallBack != null)
            mCallBack.onSuccess(contactList);
//        }
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void updateConversationHeader(List<String> conversationIds) {
        HttpModel httpModel = new HttpModel(new IPresenter() {
            @Override
            public void onSuccess(String json, String url, Object tag) {
                ContactBaseInfoBean contactBaseInfoBean = Json_U.fromJson(json, ContactBaseInfoBean.class);
                List<ContactBaseInfoBean.DataBean> data = contactBaseInfoBean.data;
                Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
                for (ContactBaseInfoBean.DataBean infoBean : data) {
                    EMConversation conversation = allConversations.get(infoBean.hxUsername);
                    if (conversation != null) {
                        String extField = conversation.getExtField();
                        ConversationExtBean extBean;
                        if (TextUtils.isEmpty(extField))
                            extBean = new ConversationExtBean();
                        else
                            extBean = Json_U.fromJson(extField, ConversationExtBean.class);
                        extBean.header = infoBean.picUrl;
                        extBean.nick = infoBean.nickname;
                        conversation.setExtField(Json_U.toJson(extBean));
                    }
                }
                if (mCallBack != null)
                    mCallBack.onSuccess(null);
            }

            @Override
            public void onConnectFaild(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }

            @Override
            public void onResponseError(ErrorBean bean) {
                if (mCallBack != null)
                    mCallBack.onFiald();
            }
        });


        RequestBean requestBean = new RequestBean();
        requestBean.hxNames = conversationIds;
        httpModel.request(API.CONTACT_INFO_UPDATE, requestBean);
    }


    public interface CallBack {
        void onStart();

        void onSuccess(Map<String, EaseUser> contactList);

        void onFiald();
    }

}
