package com.zzkx.mtool.util;

import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.bean.MyEMMessage;
import com.zzkx.mtool.config.Const;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshss on 2017/11/1.
 */

public class StateHelperUtil {
    private static StateHelperUtil sInstance;
    private String hxUserName = SPUtil.getString(Const.HX_ID, "");
    private String unReadCountName = hxUserName + "_unread_state_count";
    private String lastMessageName = hxUserName + "_last_state_msg.obj";

    private StateHelperUtil() {

    }

    public static StateHelperUtil getInstance() {
        if (sInstance == null)
            sInstance = new StateHelperUtil();
        return sInstance;
    }

    public int getUnreadCount() {
        return SPUtil.getInt(unReadCountName, 0);
    }

    private void addUnreadCount() {
        SPUtil.putInt(unReadCountName, getUnreadCount() + 1);
    }

    public MyEMMessage getLastNotifyMessage() {
        try {
            File file = new File(MyApplication.getContext().getCacheDir(), lastMessageName);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            MyEMMessage message = (MyEMMessage) in.readObject();
            in.close();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void putCmdMessage(EMMessage message) {
        MyEMMessage myEMMessage = createMyEMMessage(message);
        String action = ((EMCmdMessageBody) message.getBody()).action();
        ArrayList<MyEMMessage> allMsg = getAllMsg(action);
        if (allMsg == null) {
            allMsg = new ArrayList<>();
        }
        allMsg.add(0, myEMMessage);
        EMCmdMessageBody body = (EMCmdMessageBody) message.getBody();
        boolean b = cacheMessage(body.action(), allMsg);
        if (b)
            addUnreadCount();
        cacheLastMessage(myEMMessage);
    }

    private void cacheLastMessage(MyEMMessage message) {
        File file = new File(MyApplication.getContext().getCacheDir().getAbsolutePath(), lastMessageName);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(message);
            out.close();
        } catch (Exception e) {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
        }
    }

    private MyEMMessage createMyEMMessage(EMMessage message) {
        MyEMMessage myEMMessage = new MyEMMessage();
        myEMMessage.msgTime = message.getMsgTime();
        try {
            myEMMessage.msgContent = message.getStringAttribute(Const.MSG);
            myEMMessage.fromNicName = message.getStringAttribute(Const.NAME);
            myEMMessage.userHead = message.getStringAttribute(Const.URL);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return myEMMessage;
    }

    public boolean cacheMessage(String action, List<MyEMMessage> allMsg) {
        File file = new File(MyApplication.getContext().getCacheDir().getAbsolutePath(), hxUserName + action + "_state_msg.obj");
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(allMsg);
            out.close();
            return true;
        } catch (Exception e) {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<MyEMMessage> getAllMsg(String action) {
        try {
            File file = new File(MyApplication.getContext().getCacheDir(), hxUserName + action + "_state_msg.obj");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            ArrayList<MyEMMessage> messages = (ArrayList<MyEMMessage>) in.readObject();
            in.close();
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void subtractUnread() {
        int unreadCount = getUnreadCount();
        if (unreadCount > 0)
            SPUtil.putInt(unReadCountName, unreadCount - 1);
    }
}
