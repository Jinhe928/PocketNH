package com.pocketnhs.pocketnhsandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Dimension;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.ui.CommonHealthQuestionsDetailsTextAdapter_Depth4;

import java.lang.ref.WeakReference;

public class CommonHQDetailsTextActivity_Depth4 extends AppCompatActivity implements DataReadyListener {

    RecyclerView mRecyclerView;
    CommonHealthQuestionsDetailsTextAdapter_Depth4 mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_hqquestion_details_text);
        setupToolbar();
        getQuestionText();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Common Health Questions");
        tvTitle.setTextSize(Dimension.SP, 18);
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(CommonHQDetailsTextActivity_Depth4.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        //toolbar.setSubtitle("");
        //toolbar.setTitle("");
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

    private void getQuestionText() {
        ServerDataManager.getInstance().getCommonHealthQuestionText(ApplicationState.lastQuestionsTextUrl, this);
    }

    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            UpdateUI();
        } else {
            Toast.makeText(this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUI() {

        if (ServerDataManager.getInstance().mQuestions != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new CommonHealthQuestionsDetailsTextAdapter_Depth4(ServerDataManager.getInstance().mQuestions.list, getApplicationContext(), new WeakReference<Activity>(this));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }


}
