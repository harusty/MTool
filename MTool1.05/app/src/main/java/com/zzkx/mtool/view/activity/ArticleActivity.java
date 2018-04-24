package com.zzkx.mtool.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzkx.mtool.R;
import com.zzkx.mtool.bean.ArticleBean;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.presenter.ArticleDetailPresenter;
import com.zzkx.mtool.view.iview.IArticleDetailView;

import butterknife.BindView;

/**
 * Created by sshss on 2017/12/14.
 */

public class ArticleActivity extends BaseActivity implements IArticleDetailView {
    @BindView(R.id.webView)
    WebView mWebView;
    private ArticleDetailPresenter mArticleDetailPresenter;
    private String mId;

    @Override
    public int getContentRes() {
        return R.layout.activity_h5;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle(getIntent().getStringExtra(Const.TITLE));
        mId = getIntent().getStringExtra(Const.ID);
        mArticleDetailPresenter = new ArticleDetailPresenter(this);

        initWebView();
    }

    @Override
    public void initNet() {
        mArticleDetailPresenter.getArticle(mId);
    }

    private void initWebView() {
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
    }

    @Override
    public void onReload() {
        initNet();
    }

    @Override
    public void showArticleDetail(ArticleBean bean) {
        ArticleBean.DataBean data = bean.data;
        if (data != null) {
            mWebView.loadDataWithBaseURL(null, data.contentInfo, "text/html", "utf-8", null);
        }
    }
}
