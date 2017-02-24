package com.pocketnhs.pocketnhsandroid.offline_library;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.InfoActivity;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationUtils;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;

public class ShowMyLibArticleActivity extends AppCompatActivity {

    NHSCondition mCondition = ApplicationState.sLastLibraryCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);


        setupToolbar();
        setupTitle();
        setupSummary();
        setupDetails();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("My Library");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ShowMyLibArticleActivity.this)
                        .toBundle();
                Intent intent = new Intent(ShowMyLibArticleActivity.this, InfoActivity.class);
                startActivity(intent, bundle);
            }
        });

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }


    private void setupDetails() {
        TextView tvDetails = (TextView) findViewById(R.id.tvDetails_Lib);
        if (mCondition !=null){
            if (mCondition.mText != null){
                mCondition.mText = ApplicationUtils.cleanText(mCondition.mText);
                if (mCondition != null) {
                    tvDetails.setText(Html.fromHtml(mCondition.mText));
                }
            }
        }
    }

    private void setupSummary() {
        TextView tvSummary = (TextView) findViewById(R.id.tvSummary_Lib);
        if (mCondition !=null){
            if (mCondition.mText != null){
                tvSummary.setText(mCondition.mSummary);
            }
        }
    }

    private void setupTitle() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle_Lib);
        if (mCondition !=null){
            if (mCondition.mTitle != null){
                tvTitle.setText(mCondition.mTitle);
            }
        }


    }
}
