package com.example.elmbay.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.elmbay.R;
import com.example.elmbay.activity.WebViewActivity;
import com.example.elmbay.manager.AppManager;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class WebViewFragment extends Fragment {
    private static final String LOG_TAG = WebViewFragment.class.getName();
    private String mUrl;
    private ProgressBar mSpinner;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_webview, container, false);

        mUrl = getArguments().getString(WebViewActivity.URL_KEY);
        if (!TextUtils.isEmpty(mUrl)) {
            mSpinner = top.findViewById(R.id.spinner);
            mSpinner.setVisibility(View.VISIBLE);

            mWebView = top.findViewById(R.id.webview);

            // App crashes upon back button if we don't set WebViewClient
            mWebView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url){
                    mSpinner.setVisibility(View.GONE);
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
                    String message = getContext().getString(R.string.webview_on_error);
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        message = message + "\n\n" + error.getDescription();
                    }
                    showDialog(message);
                    super.onReceivedError(mWebView, request, error);
                }
            });

//            mWebView.loadUrl(mUrl + "?u=" + AppManager.getInstance().getSessionData().getUserManager().getUserToken());
            setCookie();
            mWebView.loadUrl(mUrl);
        }

        return top;
    }

    private void setCookie() {
        String cookieString = "u=" + AppManager.getInstance().getSessionData().getUserManager().getUserToken() + "; path=/";
        CookieManager.getInstance().setCookie(mUrl, cookieString);
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Light_Dialog));
        builder.setMessage(message)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        }
                    }
                });
        builder.create().show();
    }
}
