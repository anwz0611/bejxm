
package com.zyinfo.brj.ui.news.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

public class NewsBrowserActivity extends BaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.web_view)
    WebView webView;

    public static void startAction(Context context ,String link,String title){
        Intent intent = new Intent(context, NewsBrowserActivity.class);
        intent.putExtra(AppConstant.NEWS_LINK,link);
        intent.putExtra(AppConstant.NEWS_TITLE,title);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.act_news_browser;
    }

    @Override
    public void initPresenter() {

    }
    @Override
    public void initView() {
        ntb.setTitleText(getIntent().getStringExtra(AppConstant.NEWS_TITLE));
        initWebView();
    }


    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        // 打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setSupportZoom(true); //支持缩放
//        webSettings.setBuiltInZoomControls(true); // 放大和缩小的按钮，容易引发异常 http://blog.csdn.net/dreamer0924/article/details/34082687

        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void setWebView() {
        webView.loadUrl(getIntent().getStringExtra(AppConstant.NEWS_LINK));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(webView!=null) {
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
