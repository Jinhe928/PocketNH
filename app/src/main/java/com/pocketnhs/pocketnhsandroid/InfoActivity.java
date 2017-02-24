package com.pocketnhs.pocketnhsandroid;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;

public class InfoActivity extends AppCompatActivity {


    EditText mEtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setupToolbar();
        setupButtons();
    }

    private void setupButtons() {
        ImageButton rateApp = (ImageButton) findViewById(R.id.rate_app);
        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        mEtFeedback = (EditText) findViewById(R.id.feedback_text);

        ImageButton sendFeedback = (ImageButton) findViewById(R.id.send_feedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bigbluefightingcockerel@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pocket GP Feedback");
                if (mEtFeedback.getText().toString()!=null && mEtFeedback.getText().toString().length()>0) {
                    intent.putExtra(Intent.EXTRA_TEXT, mEtFeedback.getText().toString());
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
                else{
                    Toast.makeText(InfoActivity.this,"Please enter your feedback...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton visitNHSWebsite = (ImageButton) findViewById(R.id.visit_nhs_website);
        visitNHSWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.nhs.uk";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("About Pocket GP");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setVisibility(View.INVISIBLE);

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
}
