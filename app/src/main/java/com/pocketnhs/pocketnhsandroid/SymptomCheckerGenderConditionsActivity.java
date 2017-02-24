package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.pocketnhs.pocketnhsandroid.ui.SymptomCheckerGenderConditionLinksAdapter;

public class SymptomCheckerGenderConditionsActivity extends AppCompatActivity implements DataReadyListener {

    RecyclerView mRecyclerView;
    SymptomCheckerGenderConditionLinksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker_sub_parts);
        setupToolbar();
        getGenderConditionsData();
    }

    private void getGenderConditionsData() {
        if (ApplicationState.sLastGenderConditionsUrl !=null) {
            ServerDataManager.getInstance().getGenderConditions(ApplicationState.sLastGenderConditionsUrl,this);
        }else{
            Toast.makeText(this,"Error - couldn't connect to server",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView)findViewById(R.id.title_view);
        tvTitle.setText("Symptom Checker");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(SymptomCheckerGenderConditionsActivity.this,InfoActivity.class);
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


    private void setupRecyclerView() {
        if (ApplicationState.sLastGenderConditionsLinksArray !=null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new SymptomCheckerGenderConditionLinksAdapter(ApplicationState.sLastGenderConditionsLinksArray, SymptomCheckerGenderConditionsActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        else{
            Toast.makeText(this,"couldn't fetch data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (message.equals(MESSAGE_OK)){
            setupRecyclerView();
        }else{
            Toast.makeText(this, "Couldn't get results", Toast.LENGTH_SHORT).show();
        }
    }
}
