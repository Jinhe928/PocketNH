package com.pocketnhs.pocketnhsandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.offline_library.LibraryDataManager;
import com.pocketnhs.pocketnhsandroid.offline_library.MyLibraryActivity;
import com.pocketnhs.pocketnhsandroid.offline_library.ShowGPActivity;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;

public class MainActivity extends AppCompatActivity {

    ImageView mServicesFinderView;
    ImageView mSymptomsCheckerView;
    ImageView mHealthAZView;
    ImageView mLiveWellView;
    ImageView mCommonHealthQuestionsView;
    ImageView mQuizzesView;

    ImageView mMyGP;
    ImageView mMyLibrary;
    private Vibrator myVib;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtons();
        setupBottomToolbarImageViews();
        setupToolbar();
        setupVibrator();
    }

    private void setupVibrator() {
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        toolbar.setSubtitle("");
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                startActivityWithTransition(InfoActivity.class);
            }
        });


            /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            */

    }

    private void setupBottomToolbarImageViews() {
        ImageView call_111 = (ImageView) findViewById(R.id.nhs_111);
        call_111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telNumber = "111";
                if (telNumber != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + telNumber));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        });

        mMyGP = (ImageView) findViewById(R.id.my_gp);
        mMyGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                NHSOrganisation myGP = LibraryDataManager.getInstance().getMyGP();
                if (myGP != null){
                    Analytics.LogMyGPStartEvent();
                    ApplicationState.sIsShowingOrganization = true;
                    ApplicationState.sLastOrganisation = myGP;
                    startActivityWithTransition(ShowGPActivity.class);
                }else{
                    Toast.makeText(MainActivity.this,"Please use Services Finder to save GP",Toast.LENGTH_LONG).show();
                }

            }
        });

        mMyLibrary = (ImageView) findViewById(R.id.my_library);
        mMyLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                Analytics.LogMyLibraryStartEvent();
                startActivityWithTransition(MyLibraryActivity.class);
            }
        });
    }


    private void setupButtons() {

        mServicesFinderView = (ImageView) findViewById(R.id.MAIN_services_finder);
        mServicesFinderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogServiceFinderStartEvent();
                startActivityWithTransition(ServicesFinderActivity.class);
            }
        });


        mSymptomsCheckerView = (ImageView) findViewById(R.id.MAIN_symptom_checker);
        mSymptomsCheckerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogSymptomCheckerStartEvent();
                startActivityWithTransition(SymptomCheckerActivity.class);
            }
        });


        mHealthAZView = (ImageView) findViewById(R.id.MAIN_health_a_z);
        mHealthAZView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogHealthAZStartEvent();
                startActivityWithTransition(HealthAZActivity.class);

            }
        });


        mLiveWellView = (ImageView) findViewById(R.id.MAIN_live_well);
        mLiveWellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogLiveWellStartEvent();
                startActivityWithTransition(LiveWellActivity.class);
            }

        });




        mCommonHealthQuestionsView = (ImageView) findViewById(R.id.MAIN_common_health_questions);
        mCommonHealthQuestionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogCommonHealthQuestionsStartEvent();
                startActivityWithTransition(CommonHealthQuestionsActivity_Depth1.class);
            }
        });


        mQuizzesView = (ImageView) findViewById(R.id.MAIN_quizzes);
        mQuizzesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(50);
                Analytics.LogQuizzesStartEvent();
                startActivityWithTransition(QuizzesListActivity.class);
            }
        });



    }

    private void startActivityWithTransition( final Class<? extends AppCompatActivity> activityToOpen) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this)
                .toBundle();
        Intent intent = new Intent(MainActivity.this, activityToOpen);
        startActivity(intent,bundle);
    }


}
