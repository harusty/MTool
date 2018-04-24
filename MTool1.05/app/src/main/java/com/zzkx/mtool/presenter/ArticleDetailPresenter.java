package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.ArticleBean;
import com.zzkx.mtool.bean.RequestBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.view.iview.IArticleDetailView;

/**
 * Created by sshss on 2018/2/24.
 */

public class ArticleDetailPresenter extends BasePresenter<IArticleDetailView, ArticleBean> {
    public ArticleDetailPresenter(IArticleDetailView view) {
        super(view);
    }

    public void getArticle(String id){
        getView().showProgress(true);
        RequestBean requestBean = new RequestBean();
        requestBean.id = id;
        getHttpModel().request(API.ARTICLE_DETAIL,requestBean);
    }
    @Override
    public void onSuccessM(ArticleBean bean) {
        getView().showArticleDetail(bean);
        getView().showProgress(false);
    }
}
