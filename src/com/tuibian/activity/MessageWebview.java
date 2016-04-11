package com.tuibian.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tuibian.common.CGlobal;
import com.tuibian.main.BaseActivity;
import com.tuibian.R;

public class MessageWebview extends BaseActivity implements OnClickListener  {
	private WebView h_report_wv;

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tack.addActivity(this);
		addView(R.layout.h_report_webview);
		
		String url = getIntent().getStringExtra("url");
		String title = getIntent().getStringExtra("title");
		
		if(title==null)
			title = "我的消息";
		
		mTitleBar.setTitleText(title);
		
		h_report_wv = (WebView) findViewById(R.id.h_report_wv);
		h_report_wv.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式

		WebSettings settings = h_report_wv.getSettings();

		settings.setUseWideViewPort(true);//设定支持viewport
		settings.setJavaScriptEnabled(true); 
		settings.setLoadWithOverviewMode(true);

		//settings.setBuiltInZoomControls(true);
		//settings.setSupportZoom(true);//设定支持缩放
		
		//自适应屏幕
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//end8
		
		h_report_wv.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url)
		    { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
	            view.loadUrl(url);
	            return true;
		    }
		});
		
		if (url != null && !url.equals("")) {
			h_report_wv.loadUrl(url);
		}
		
	}

	@Override
	public void onClick(View v) {
		
	}
	@Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

}
