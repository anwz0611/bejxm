/**
 * 
 */
package com.zyinfo.brj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zyinfo.brj.R;
import com.zyinfo.brj.entity.ShapeLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
* Description:
*
*/
public class ProgressWebView extends RelativeLayout implements OnClickListener{
	
    /**
     * 上下文
     */
    private Context mContext;
    
    /**
     * 浏览器
     */
    private WebView webView;
    private List<String> list=new ArrayList<String>();
    
    /**
     * 加载进度
     */
    private ProgressBar progressBar;
    private ProgressBar showProgressBar;
    private ShapeLoadingDialog dialog;
	private TextView topNext;//上一个
	private TextView next;//下一个
	private TextView player;//播放
	private String currentUrl;
	private int currentLocation=0;
	private List<String> listDate=new ArrayList<String>();
	private TextView dateText;
	
	/**
     * 设置WebView链接
     * @return
     */
    public void getWebViewLoadUrl(List<String> list,List<String> listDate){
    	
    	this.list=list;
    	this.listDate=listDate;
    	initView();
    }
    
    public ProgressWebView(Context context) {
        super(context);
        this.mContext = context;
        // TODO Auto-generated constructor stub
//        initView();
    }
    
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
//        initView();
    }
    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.mContext = context;
//        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_webview, this);
        
        webView = (WebView) findViewById(R.id.view_webView);
        progressBar = (ProgressBar) findViewById(R.id.view_webview_progress);
        showProgressBar= (ProgressBar) findViewById(R.id.progress_bar1);
        topNext= (TextView) findViewById(R.id.top_next);
        player= (TextView) findViewById(R.id.player);
        next= (TextView) findViewById(R.id.next);
        topNext.setOnClickListener(this);
        next.setOnClickListener(this);
        player.setOnClickListener(this);
        dialog=new ShapeLoadingDialog(mContext);
        dateText=(TextView) findViewById(R.id.text_date);
        initWebViewSet();
    }


/**
 * 初始化WebView设置
 */
@SuppressLint("SetJavaScriptEnabled")
private void initWebViewSet() {
	showProgressBar.setMax(list.size());
    showProgressBar.setProgress(list.size());
    // 设置编码
    webView.getSettings().setDefaultTextEncodingName("utf-8");
    webView.getSettings().setTextZoom(70);
    webView.getSettings().setDomStorageEnabled(true);
    webView.setInitialScale(50);
    webView.getSettings().setUseWideViewPort(true);// 这个很关键  
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    // 设置背景颜色 透明
    webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
    // 设置可以支持缩放
    webView.getSettings().setSupportZoom(true);
    
    // 设置缓存模式
    webView.getSettings().setAppCacheEnabled(true);
    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    // //添加Javascript调用java对象
    webView.getSettings().setJavaScriptEnabled(true);
    // 设置出现缩放工具
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setDisplayZoomControls(false);
    // 扩大比例的缩放设置此属性，可任意比例缩放。
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setBlockNetworkImage(false);
    // 不启用硬件加速
    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    // 自适应屏幕
    webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//    webView.loadUrl(list.get(currentLocation));
    
    dateText.setHint(listDate.get(0));
    webView.loadUrl(list.get(0));
    // 重新WebView加载URL的方法
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            url = "http://www.weather.com.cn/satellite/";
        	Log.e("url11shouldOverrideUrlLoading1", url);
            view.loadUrl(url);
            return false;
        }

        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
        	Log.e("url11onPageStarted1", url);
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            dialog.loading("正在加载....");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.GONE);
            showProgressBar.setVisibility(View.VISIBLE);
            showProgressBar.setProgress(currentLocation+1);
            dateText.setText(listDate.get(currentLocation));
            dialog.dismiss();
            super.onPageFinished(view, url);
        }
    });

    webView.setWebChromeClient(new WebChromeClient(){
    	
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    });
    
   
}
    /**
     * 获取WebView
     * @return
     */
    public WebView getWebView(){
        return webView;
    }  

    /**
     * 获取Progressbar
     * @return
     */
    public ProgressBar getProgressbar(){
        return showProgressBar;
    }
    
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * @ 单机操作
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//上一张
		case R.id.top_next:
			if (currentLocation>1) {
				currentLocation--;
				currentUrl=list.get(currentLocation);
				webView.loadUrl(currentUrl);
				showProgressBar.setProgress(currentLocation+1);
				 dateText.setText(listDate.get(currentLocation));
//				Toast.makeText(mContext, ""+currentLocation+1, 100).show();
			}else{
				Toast.makeText(mContext, "已经第一张！", 100).show();
			}
			break;
			
		//播放
		case R.id.player:
				for (int i = currentLocation; i < list.size(); i++) {
					Log.e("11111111111", i+"'");
					currentLocation=i;
					Log.e("222222222222", currentLocation+"");
					currentUrl=list.get(currentLocation);
					webView.loadUrl(currentUrl);
					showProgressBar.setProgress(currentLocation+1);
					dateText.setText(listDate.get(currentLocation));
//					Toast.makeText(mContext, ""+currentLocation+1, 100).show();
				}
			break;
			
		//下一张
		case R.id.next:
			if (currentLocation<list.size()-1) {
				currentLocation++;
				currentUrl=list.get(currentLocation);
				webView.loadUrl(currentUrl);
				showProgressBar.setProgress(currentLocation+1);
				dateText.setText(listDate.get(currentLocation));
//				Toast.makeText(mContext, ""+currentLocation+1, 100).show();
			}else{
				Toast.makeText(mContext, "已经最后一张！", 100).show();
			}
			break;
		default:
			break;
		}
	}
	
}
