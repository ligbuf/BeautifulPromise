package com.beautifulpromise.common.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Webview 관련 환경설정 세팅 클래스
 * @author JM
 *
 */
public class WebViewManager extends WebViewClient {

	/**
	 * 요청을 기본 웹킷아닌 액티비티내의 웹뷰에서 처리한다(캐싱 세팅을 위해 필요)
	 * 여길 거치지 않으면 캐싱 세팅이 안먹힘
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}
}
