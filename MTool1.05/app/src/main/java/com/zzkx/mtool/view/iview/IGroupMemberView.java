package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.GroupMemberBean;
import com.zzkx.mtool.view.iview.IView;

/**
 * Created by sshss on 2017/12/8.
 */

public interface IGroupMemberView extends IView {
    void showMembers(GroupMemberBean bean);

}
