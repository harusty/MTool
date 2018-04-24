package com.zzkx.mtool.presenter;

import com.zzkx.mtool.bean.HistoryBean;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.util.SearchHistoryCacheUtil;
import com.zzkx.mtool.view.iview.ISearchHistoryView;

import java.util.List;

/**
 * Created by sshss on 2017/9/16.
 */

public class HistoryPresenter extends BasePresenter<ISearchHistoryView, HistoryBean> {

    private int mSearchType;

    public HistoryPresenter(ISearchHistoryView view, int searchType) {
        super(view);
        mSearchType = searchType;
    }

    @Override
    public void onSuccessM(HistoryBean bean) {
        getView().showProgress(false);
        getView().showData(bean);
    }

    public void getHotSearch() {
        switch (mSearchType){
            case Const.SEARCH_TYPE_FOODSHOP:
                getView().showProgress(true);
                getHttpModel().request(API.HOT_SEARCH, null);
                break;
            case Const.SEARCH_TYPE_STATE:

                break;
        }
    }

    public void getCacheHistory() {
        List<String> history = SearchHistoryCacheUtil.getHistory(mSearchType);
        if (history != null && history.size() > 0)
            getView().showLoacalCache(history);
    }

    public void clearHistory() {
        SearchHistoryCacheUtil.clear(mSearchType);
    }
}
