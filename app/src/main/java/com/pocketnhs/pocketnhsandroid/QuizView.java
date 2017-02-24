package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;

public class QuizView extends AppCompatActivity {

    WebView quizWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_view);
        setupToolbar();
        setupWebView();
    }
    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Quiz");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(QuizView.this,InfoActivity.class);
                startActivity(intent);
            }
        });

        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else{
                    finish();
                }
            }
        });


    }


    private void setupWebView() {
        /*
        String quizHtml = "<script language=\"javascript\">\n" +
                "$(window).on('message', function(e) {\n" +
                "\tvar tmp = (eval('(' +e.originalEvent.data+')'));\n" +
                "\twindow.hasOwnProperty = window.hasOwnProperty || Object.prototype.hasOwnProperty;\n" +
                "\tif(tmp.hasOwnProperty('nhs_redirect')){\n" +
                "\t\twindow.location.href = tmp.nhs_redirect;\n" +
                "\t}\n" +
                "});    \n" +
                "</script>\n" +
                "<div id=\"assessment_webpart_wrapper\" style=\"width:320px;\">\n" +
                "  <iframe style=\"width:90%;height:466px;\" title=\"self assessments\" src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/assessment.html?ASid=76&syndicate=true\" frameborder=\"no\" scrolling=\"no\"></iframe>\n" +
                "  <div id=\"assessment_webpart_branding\" style=\"float:right;\"><a href=\"http://nhs.uk/tools/\" alt=\"content provided by NHS Choices\"><img src=\"http://media.nhschoices.nhs.uk/tools/documents/self_assessments_js/images/syndication.png\" width=\"264\" height=\"38\" border=\"0\" alt=\"content provided by NHS Choices\" /></a></div>\n" +
                "</div>";
                */
        String quizHtml = ApplicationState.sLastQuizURL;

        quizWebView = (WebView) findViewById(R.id.quizWebView);
        if (quizHtml.contains("BMI Healthy")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            quizWebView.getSettings().setLoadWithOverviewMode(true);
            quizWebView.getSettings().setUseWideViewPort(true);
            float deviceDensity = getResources().getDisplayMetrics().density;

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
                int scaleLevel = (int) (50 * deviceDensity);
                quizWebView.setInitialScale(scaleLevel);
            }
            //articleWebView.setInitialScale(220);
        }
        String userAgentStr = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        quizWebView.getSettings().setUserAgentString(userAgentStr);

        quizWebView.setWebViewClient(new QuizWebViewClient());
        //articleWebView.loadUrl("http://www.nhs.uk/Tools/Pages/Bloodpressurequiz.aspx?Tag=Self+assessments");
        quizWebView.loadDataWithBaseURL("", quizHtml, "text/html", "UTF-8", "");

        WebSettings webSettings = quizWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
    }

    // Use When the user clicks a link from a web page in your WebView
    private class QuizWebViewClient extends WebViewClient {

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
