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
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;
import com.pocketnhs.pocketnhsandroid.ui.CommonHealthQuestionsLinksAdapter_Depth1;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CommonHealthQuestionsActivity_Depth1 extends AppCompatActivity implements DataReadyListener {

    RecyclerView mRecyclerView;
    CommonHealthQuestionsLinksAdapter_Depth1 mAdapter;
    List<NHSTextLinkPairList> mCategoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_health_questions);
        setupToolbar();
        setQuestionsData();
        setupRecyclerView();

    }

    private void setQuestionsData() {

        mCategoriesList = new ArrayList<>();

        addHealthyLiving(mCategoriesList);
        addFamily(mCategoriesList);
        addWomensHealth(mCategoriesList);
        addMenHealth(mCategoriesList);
        addVaccinations(mCategoriesList);
        addFirstAidAccidentsNHSServices(mCategoriesList);
        addCaringCarersLongTermConditions(mCategoriesList);
        addTravelHealth(mCategoriesList);
        addMedicineAdviceTips(mCategoriesList);
        addIlnesses(mCategoriesList);
        addSexualHealth(mCategoriesList);
        addMenHealth(mCategoriesList);
        addWorkplaceHealth(mCategoriesList);

//        ServerDataManager.getInstance().getCommonHealthQuestions(this);
    }

    private void addFamily(List<NHSTextLinkPairList> categoriesList) {

        NHSTextLinkPairList family = new NHSTextLinkPairList();
        family.list = new ArrayList<>();
        family.mListURL = null;
        family.mListText="Family";

        family.addPair("Pregnancy","http://v1.syndication.nhschoices.nhs.uk/chq/categories/54-pregnancy");
        family.addPair("Children's health","http://v1.syndication.nhschoices.nhs.uk/chq/categories/62-childrens-health");
        categoriesList.add(family);
    }


    private void addIlnesses(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList illnesses = new NHSTextLinkPairList();
        illnesses.list = new ArrayList<>();
        illnesses.mListURL = null;
        illnesses.mListText="Illnesses";

        illnesses.addPair("Cancer","http://v1.syndication.nhschoices.nhs.uk/chq/categories/96-cancer");
        illnesses.addPair("H1N1 flu (swine flu)","http://v1.syndication.nhschoices.nhs.uk/chq/categories/5-h1n1-flu-(swine-flu)");
        illnesses.addPair("Infections","http://v1.syndication.nhschoices.nhs.uk/chq/categories/200-infections");
        categoriesList.add(illnesses);

    }

    private void addMedicineAdviceTips(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList medicineAdviceTips = new NHSTextLinkPairList();
        medicineAdviceTips.list = new ArrayList<>();
        medicineAdviceTips.mListURL = null;
        medicineAdviceTips.mListText="Medicine, Advice and Tips";

        medicineAdviceTips.addPair("Medicines","http://v1.syndication.nhschoices.nhs.uk/chq/categories/73-medicines");
        medicineAdviceTips.addPair("Winter health","http://v1.syndication.nhschoices.nhs.uk/chq/categories/89-winter-health");
        categoriesList.add(medicineAdviceTips);

    }


    private void addWomensHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList womensHealt = new NHSTextLinkPairList();
        womensHealt.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/60-womens-health";
        womensHealt.mListText = "Women's health";
        womensHealt.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(womensHealt);
    }

    private void addMenHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList mensHealth = new NHSTextLinkPairList();
        mensHealth.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/61-mens-health";
        mensHealth.mListText = "Men's health";
        mensHealth.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(mensHealth);
    }

    private void addVaccinations(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList  vaccinations= new NHSTextLinkPairList();
        vaccinations.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/67-vaccinations";
        vaccinations.mListText = "Vaccinations";
        vaccinations.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(vaccinations);
    }

    private void addCaringCarersLongTermConditions(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList cCLTC = new NHSTextLinkPairList();
        cCLTC.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/155-caring-carers-and-long-term-conditions";
        cCLTC.mListText = "Caring, carers and long-term conditions";
        cCLTC.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(cCLTC);
    }

    private void addTravelHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList travelHealth = new NHSTextLinkPairList();
        travelHealth.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/70-travel-health";
        travelHealth.mListText = "Travel health";
        travelHealth.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(travelHealth);
    }


    private void addSexualHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList sexualHealth = new NHSTextLinkPairList();
        sexualHealth.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/118-sexual-health";
        sexualHealth.mListText = "Sexual health";
        sexualHealth.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(sexualHealth);
    }

    private void addMentalHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList mentalHealth = new NHSTextLinkPairList();
        mentalHealth.mListURL = "http://v1.syndication.nhschoices.nhs.uk/chq/categories/139-mental-health";
        mentalHealth.mListText = "Mental health";
        mentalHealth.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(mentalHealth);
    }

    private void addWorkplaceHealth(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList workplaceHealth = new NHSTextLinkPairList();
        workplaceHealth.mListURL ="http://v1.syndication.nhschoices.nhs.uk/chq/categories/190-workplace-health";
        workplaceHealth.mListText = "Workplace health";
        workplaceHealth.list = new ArrayList<>(); // empty list (no subtext)
        categoriesList.add(workplaceHealth);
    }

    private void addHealthyLiving(List<NHSTextLinkPairList> categoriesList) {
        NHSTextLinkPairList healthyLiving = new NHSTextLinkPairList();
        healthyLiving.list = new ArrayList<>();
        healthyLiving.mListURL = null;
        healthyLiving.mListText="Healthy Living";

        healthyLiving.addPair("Food and diet","http://v1.syndication.nhschoices.nhs.uk/chq/categories/51-food-and-diet");
        healthyLiving.addPair("Exercise","http://v1.syndication.nhschoices.nhs.uk/chq/categories/52-exercise");
        healthyLiving.addPair("Stopping smoking","http://v1.syndication.nhschoices.nhs.uk/chq/categories/53-stopping-smoking");
        healthyLiving.addPair("Dental health","http://v1.syndication.nhschoices.nhs.uk/chq/categories/74-dental-health");
        healthyLiving.addPair("Lifestyle","http://v1.syndication.nhschoices.nhs.uk/chq/categories/87-lifestyle");
        healthyLiving.addPair("Blood pressure","http://v1.syndication.nhschoices.nhs.uk/chq/categories/201-blood-pressure");
        categoriesList.add(healthyLiving);
    }

    private void addFirstAidAccidentsNHSServices(List<NHSTextLinkPairList> categoriesList){

        NHSTextLinkPairList firstAidEtc = new NHSTextLinkPairList();
        firstAidEtc.list = new ArrayList<>();
        firstAidEtc.mListURL = null;
        firstAidEtc.mListText="First Aid, Accidents, NHS Services";


        firstAidEtc.addPair("NHS services and treatments","http://v1.syndication.nhschoices.nhs.uk/chq/categories/68-nhs-services-and-treatments");
        firstAidEtc.addPair("Operations, tests and procedures","http://v1.syndication.nhschoices.nhs.uk/chq/categories/69-operations-tests-and-procedures");
        firstAidEtc.addPair("Accidents, first aid and treatments","http://v1.syndication.nhschoices.nhs.uk/chq/categories/72-accidents-first-aid-and-treatments");
        categoriesList.add(firstAidEtc);
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Common Health Questions");
        tvTitle.setTextSize(Dimension.SP,18);
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(CommonHealthQuestionsActivity_Depth1.this,InfoActivity.class);
                startActivity(intent);
            }
        });

        //toolbar.setSubtitle("");
        //toolbar.setTitle("");
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

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mAdapter = new CommonHealthQuestionsLinksAdapter_Depth1(mCategoriesList,getApplicationContext(),new WeakReference<Activity>(this));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

    }

    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (message.equals(DataReadyListener.MESSAGE_OK)){
            setupRecyclerView();
        }else{
            Toast.makeText(CommonHealthQuestionsActivity_Depth1.this,"Couldn't get questions",Toast.LENGTH_SHORT).show();
        }
    }
}
