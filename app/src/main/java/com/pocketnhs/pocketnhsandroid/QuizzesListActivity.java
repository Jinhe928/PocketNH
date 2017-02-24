package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.server.NHSToolsEmbedCodes;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSQuizDetails;
import com.pocketnhs.pocketnhsandroid.ui.QuizesLinksAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuizzesListActivity extends AppCompatActivity  implements  GestureDetector.OnGestureListener {

    RecyclerView mRecyclerView;
    QuizesLinksAdapter mAdapter;
    List<NHSQuizDetails> mQuizzes;
    private GestureDetectorCompat mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_list);
        setupToolbar();
        setupRecyclerView();
        setupGestureDetector();
    }

    private void setupGestureDetector() {
        mGestureDetector = new GestureDetectorCompat(this, this);

    }




    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Quizzes");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(QuizzesListActivity.this)
                        .toBundle();
                Intent intent = new Intent(QuizzesListActivity.this,InfoActivity.class);
                startActivity(intent,bundle);
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

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled = mGestureDetector.onTouchEvent(ev);
        return handled;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        double SWIPE_MIN_DISTANCE = 100;
        double SWIPE_THRESHOLD_VELOCITY = 500;
        double diff = e2.getX() - e1.getX();
        if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                && Math.abs(velocityY) < Math.abs(velocityX)) {
            finish();
            return true;
        }else {
            return false;
        }
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        /*if (ServerDataManager.getInstance().mConditionsAZ == null) {
            ServerDataManager.getInstance().mConditionsAZ  = new NHSTextLinkPairList();
        }
        */
        setupQuizzesList();
        mAdapter = new QuizesLinksAdapter(mQuizzes, getApplicationContext(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupQuizzesList() {

        mQuizzes = new ArrayList<NHSQuizDetails>();

        NHSQuizDetails c101 = new NHSQuizDetails();
        c101.mTitle = "BMI healthy weight calculator";
        c101.mText = " - Find out if you or your child are a healthy weight \n - Understand how BMI is calculated \n - Get practical weight loss information";
        c101.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_BMI;
        c101.mImage = "quiz_100_bmi";
        mQuizzes.add(c101);

        NHSQuizDetails c102 = new NHSQuizDetails();
        c102.mTitle = "Mood Self Assessment";
        c102.mText = " We can all feel low, anxious or panicky from time to time. Check your mood using this simple questionnaire and get advice on what might help.";
        c102.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_MOOD;
        c102.mImage = "quiz_102_mood";
        mQuizzes.add(c102);


        NHSQuizDetails c7 = new NHSQuizDetails();
        c7.mTitle = "Depression self-assessment";
        c7.mText = "This is for information only and is not intended to replace a consultation with a GP. The PHQ was developed by Drs Robert L Spitzer, Janet B.W. Williams, Kurt Kroenke and colleagues with a grant from Pfizer Inc. Reproduced with permission.";                ;
        c7.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_MENTAL_HEALTH_DEPRESSION;
        c7.mImage = "quiz_8mentalhealth";
        mQuizzes.add(c7);


        NHSQuizDetails c1 = new NHSQuizDetails();
        c1.mTitle = "VTE self-assessment" ;
        c1.mText = "VTE stands for venous thromboembolism and is a condition where a blood clot forms in a vein. This is most common in a leg vein but a blood clot can form in the lungs. Use this assessment to find out your risk of developing one.";
        c1.mImage = "quiz_2vte";
        c1.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_VTE;
        mQuizzes.add(c1);

        NHSQuizDetails c3 = new NHSQuizDetails();
        c3.mTitle = "Type 2 diabetes self-assessment";
        c3.mText = "Take this quick and simple test to find out if you're at risk of type 2 diabetes. Please note, this tool may not be accurate for anyone undergoing treatment for diabetes.";
        c3.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_DIABETES_2;
        c3.mImage = "quiz_4diabetes2";
        mQuizzes.add(c3);

        /*
        NHSQuizDetails c4 = new NHSQuizDetails();
        c4.mTitle = "Food allergy and intolerance myth buster";
        c4.mImage = "quiz_5foodallergy";
        c4.mText = "This myth buster will help you separate fact from fiction and understand the differences between a food allergy and an intolerance. Learn more about symptoms, treatment and how many people are affected.";
        c4.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_ALLERGY_MYTH_BUSTER;
        mQuizzes.add(c4);
        */


        NHSQuizDetails c5 = new NHSQuizDetails();
        c5.mTitle = "Childhood health assessment";
        c5.mText = "Childhood is a great time to start eating healthily and keeping active. Establishing healthy habits early on can help to improve your child's quality of life in the future. Find out how much you know about raising a healthy 1-5 year old, with this short quiz.";
        c5.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_CHILD_HEALTH;
        c5.mImage = "quiz_6childhealth";
        mQuizzes.add(c5);



        /*NHSQuizDetails c8 = new NHSQuizDetails();
        c8.mTitle = "Hair loss myth buster";
        c8.mImage = "quiz_9hairloss";
        c8.mText = "Test your knowledge of hair-loss-related risks and find out the facts about losing your hair. Can you tell fact from fiction?";
        c8.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_HAIR_LOSS;
        mQuizzes.add(c8);
        */

        NHSQuizDetails c9 = new NHSQuizDetails();
        c9.mTitle = "Fertility self-assessment";
        c9.mText = "This short test is aimed at women who are worried they may be having problems getting pregnant. It will assess their situation and let them know if they should seek further medical advice.";
        c9.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_FERTILITY;
        c9.mImage = "quiz_10fertility";
        mQuizzes.add(c9);


        NHSQuizDetails c11 = new NHSQuizDetails();
        c11.mTitle = "Heavy periods self-assessment";
        c11.mText = "Heavy periods aren't necessarily a sign there is anything wrong, but they can affect you physically and emotionally and can cause disruption to everyday life. This assessment will help you to gauge how heavy your periods are and will explain more about potential causes and treatment options.";
        c11.mImage = "quiz_12period" ;
        c11.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_PERIOD;
        mQuizzes.add(c11);

        NHSQuizDetails c12 = new NHSQuizDetails();
        c12.mTitle = "Sexual health self-assessment";
        c12.mText = "How much do you know about having a safer sex life? Take the safer sex test and find out how much you know about contraception and sexually transmitted infections.";
        c12.mImage = "quiz_13sexualhealth";
        c12.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_SEXUAL_HEALTH;
        mQuizzes.add(c12);


        NHSQuizDetails c13 = new NHSQuizDetails();
        c13.mTitle = "Mole self-assessment";
        c13.mText = "Could you have a cancerous mole and not know it? Use this tool to find out more. This tool is for information only and is not intended to replace a consultation with a GP.";
        c13.mImage = "quiz_14mole";
        c13.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_MOLE;
        mQuizzes.add(c13);


        NHSQuizDetails c14 = new NHSQuizDetails();
        c14.mTitle = "Kidney disease check";
        c14.mText = "People can have kidney disease and not be aware of it. There are often no symptoms until the disease is quite advanced. Use this test to see if you're at higher risk of developing kidney disease and should have your kidney function checked.";
        c14.mImage = "quiz_15kidney";
        c14.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_KIDNEY;
        mQuizzes.add(c14);


        NHSQuizDetails c15 = new NHSQuizDetails();
        c15.mTitle = "Long-term condition assessment";
        c15.mText = "If you're living with a long-term condition, this short assessment will tell you what help and support you may be entitled to. It will assess your situation and give you personalised advice and a printable checklist.";
        c15.mImage = "quiz_16longterm";
        c15.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_LONG_TERM;
        mQuizzes.add(c15);

        NHSQuizDetails c16 = new NHSQuizDetails();
        c16.mTitle = "Bladder self-assessment";
        c16.mText = "A healthy bladder is important to all of us, yet many people suffer in silence even though a lot can be done to improve things. Take this short assessment to find out if you should seek further advice.";
        c16.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_BLADDER;
        c16.mImage = "quiz_17bladder";
        mQuizzes.add(c16);

        NHSQuizDetails c17 = new NHSQuizDetails();
        c17.mTitle = "Asthma self-assessment";
        c17.mText = "Use this self-assessment to find out if you might have asthma. It will help you to understand what the symptoms are and will give advice on treatment.";
        c17.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_ASTHMA;
        c17.mImage = "quiz_18asthma";
        mQuizzes.add(c17);


        NHSQuizDetails c19 = new NHSQuizDetails();
        c19.mTitle = "Your blood pressure reading";
        c19.mText = "Find out what your blood pressure reading means with this simple tool. High blood pressure can raise your risk of developing serious conditions like heart disease and dementia.";
        c19.mHTMLCode = NHSToolsEmbedCodes.TOOL_HTML_CODE_YOUR_BLOOD_PREASURE_READING;
        c19.mImage = "quiz_20bloodpresure";
        mQuizzes.add(c19);


    }
}
