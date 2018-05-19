package com.example.elmbay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class WebViewFragment extends Fragment {
    private static final String LOG_TAG = WebViewFragment.class.getName();
    String mUrl = "http://google.com";
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_webview, container, false);

        if (!TextUtils.isEmpty(mUrl)) {
            mWebView = top.findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);

            // App crashes upon back button if we don't set WebViewClient
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url){
                    // Target html needs to define javascript function setUserToken(val)
                    mWebView.loadUrl("javascript:setUserToken('" + AppManager.getInstance().getSessionData().getUserToken() + "')");
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    try {
                        mWebView.stopLoading();
                    } catch (Exception e) {
                        if (AppManager.DEBUG) {
                            Log.w(LOG_TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                    super.onReceivedError(mWebView, request, error);
                }
            });

            mWebView.loadUrl(mUrl);
        }

        return top;
    }
}
