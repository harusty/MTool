package com.zzkx.mtool.view.iview;

import com.zzkx.mtool.bean.BaseBean;
import com.zzkx.mtool.bean.CodeBean;

/**
 * Created by sshss on 2017/9/8.
 */

public interface IRegistView extends IView {
    void showReceiveCode(CodeBean bean);

    void validateSuccess(BaseBean baseBean);

    void resetSuccess(BaseBean baseBean);

    void registSuccess(BaseBean baseBean);

    void bindPhoneResult(BaseBean baseBean);
}
