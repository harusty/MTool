package com.zzkx.mtool.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzkx.mtool.MyApplication;
import com.zzkx.mtool.R;
import com.zzkx.mtool.config.API;
import com.zzkx.mtool.config.Const;
import com.zzkx.mtool.http.PersistentCookieStore;

import java.util.List;

import butterknife.BindView;
import okhttp3.Cookie;

/**
 * Created by sshss on 2017/12/14.
 */

public class H5ShowActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    private String mUrl;

    @Override
    public int getContentRes() {
        return R.layout.activity_h5;
    }

    @Override
    public void initViews() {
        setMainMenuEnable();
        setMainTitle(getIntent().getStringExtra(Const.TITLE));
        mUrl = getIntent().getStringExtra(Const.URL);
        String content = getIntent().getStringExtra(Const.CONTENT);

        if (!TextUtils.isEmpty(content)) {
            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            setMainTitle("详情");
        } else {
            initWebView();
            synCookies();
            mWebView.loadUrl(mUrl);
        }
    }

    private void synCookies() {
        List<Cookie> cookies_request = new PersistentCookieStore(MyApplication.getContext()).getCookies();
        if (cookies_request == null || cookies_request.size() == 0) {
            Log.e("COOKIE", "cookie empty");
            return;
        }
        try {
            CookieSyncManager.createInstance(this);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
//            String oldCookie = cookieManager.getCookie(API.HOST);
//            if (oldCookie != null) {
////                Log.("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
//            }
            for (Cookie cookie : cookies_request) {

                String cookieString = cookie.name() + "=" + cookie.value()
                        + "; domain=" + cookie.domain()
                        + "; path=" + cookie.path();
                cookieManager.setCookie(API.HOST, cookieString);
                CookieSyncManager.getInstance().sync();
            }
            String newCookie = cookieManager.getCookie(API.HOST);
            if (newCookie != null) {
                Log.e("COOKIE", "cookie null!!!!");
            }
        } catch (Exception e) {
            Log.e("COOKIE", e.toString());
        }
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
                setMainTitle(mWebView.getTitle());
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

    }
}
