package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;


public class LiveWellWebViewArticleActivity extends AppCompatActivity {


    WebView articleWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_well_activity_web_view_article);
        setupWebView();
    }

    private void setupWebView() {
        articleWebView = (WebView) findViewById(R.id.articleWebView);
        articleWebView.setWebViewClient(new ArticleWebViewClient());
        articleWebView.loadUrl(ApplicationState.sLastLiveWellURL);

        WebSettings webSettings = articleWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
    }

    // Use When the user clicks a link from a web page in your WebView
    private class ArticleWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Uri.parse(url).getHost().equals("media.nhschoices.nhs.uk")
                    || Uri.parse(url).getHost().equals("www.nhs.uk")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

}
