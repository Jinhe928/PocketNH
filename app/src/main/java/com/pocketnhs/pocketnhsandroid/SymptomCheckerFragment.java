package com.pocketnhs.pocketnhsandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetGenderSubPartsConditionsProtocol;

/**
 * Created by MacBook Pro on 23.8.2016..
 */
public class SymptomCheckerFragment extends Fragment implements DataReadyListener {

    boolean male = true;
    boolean[] active = new boolean[5];
    Button[] mButtons = new Button[5];

    Button maleButton;
    Button femaleButton;
    ImageView maleBodyView;
    ImageView femaleBodyView;




    ProgressDialog mProgressDialog;

    public static SymptomCheckerFragment newInstance() {
        return new SymptomCheckerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active[0] = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_symptom_checker,container,false);
        setupToolbar(v);
        maleButton = (Button) v.findViewById(R.id.maleButton);
        femaleButton = (Button) v.findViewById(R.id.femaleButton);
        maleBodyView = (ImageView) v.findViewById(R.id.maleBodyView);
        femaleBodyView = (ImageView) v.findViewById(R.id.femaleBodyView);

        mProgressDialog = new ProgressDialog(getActivity());

        Typeface latoBold = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Bold.ttf");
        Typeface latoBlack = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Black.ttf");
        Typeface latoRegular = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Regular.ttf");

        mButtons[0] = (Button) v.findViewById(R.id.head_neck);
        mButtons[1] = (Button) v.findViewById(R.id.chest_back);
        mButtons[2] = (Button) v.findViewById(R.id.arms_hands);
        mButtons[3] = (Button) v.findViewById(R.id.abdomen);
        mButtons[4] = (Button) v.findViewById(R.id.legs_feet);

        for (int i=0;i<5;i++) {
            mButtons[i].setTypeface(latoBold);
            if (i==0) {
                mButtons[i].setTextColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonTextColorActive));
                mButtons[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonBackgroundColorActive));
            } else {
                mButtons[i].setTextColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonTextColorInactive));
                mButtons[i].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonBackgroundColorInactive));
            }
            final int k = i;
            mButtons[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!active[k]) {
                        mButtons[k].setTextColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonTextColorActive));
                        mButtons[k].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonBackgroundColorActive));
                        active[k] = true;
                        for (int j=0;j<5;j++) {
                            if ((j!=k) && (active[j])) {
                                active[j] = false;
                                mButtons[j].setTextColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonTextColorInactive));
                                mButtons[j].setBackgroundColor(ContextCompat.getColor(getContext(),R.color.symptomCheckerButtonBackgroundColorInactive));
                            }
                        }
                    }
                    int buttonIndex = k;
                    ServerDataManager.getInstance().getGenderSubParts(getSelectedGender(),getSelectedBodyPart(buttonIndex),SymptomCheckerFragment.this);
                    mProgressDialog.show();
                }
            });
        }

        maleButton.setTypeface(latoBlack);
        maleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonActiveColor));
        femaleButton.setTypeface(latoRegular);
        femaleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonInactiveColor));
        maleBodyView.setImageResource(R.drawable.male_figure_active);
        femaleBodyView.setImageResource(R.drawable.female_figure_inactive);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaleButtonsActive();
            }
        });
        maleBodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaleButtonsActive();
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFemaleButtonsActive();
            }
        });
        femaleBodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFemaleButtonsActive();
            }
        });



        return v;
    }

    private void setupToolbar(View v) {

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) v.findViewById(R.id.title_view);
        tvTitle.setText("Symptom Checker");
        ImageView ivInfo = (ImageView) v.findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(getActivity(),InfoActivity.class);
                startActivity(intent);
            }
        });

        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().finishAfterTransition();
                } else{
                    getActivity().finish();
                }

            }
        });


    }


    private String getSelectedBodyPart(int buttonIndex) {
        String bodyPart = "";
        switch (buttonIndex) {
            case 0:  bodyPart = GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_BODY_PART_HEAD;
                break;
            case 1:  bodyPart = GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_BODY_PART_CHEST;
                break;
            case 2:  bodyPart = GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_BODY_PART_ARM;
                break;
            case 3:  bodyPart = GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_BODY_PART_ABDOMEN;
                break;
            case 4:  bodyPart = GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_BODY_PART_LEG;
                break;
        }
        return bodyPart;
    }

    private String getSelectedGender() {
        if (male){
            return GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_GENDER_MALE;
        }else{
            return GetGenderSubPartsConditionsProtocol.REQUEST_PARAM_GENDER_FEMALE;
        }
    }

    public void setMaleButtonsActive() {
        Typeface latoBlack = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Black.ttf");
        Typeface latoRegular = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Regular.ttf");
        if (!male) {
            maleButton.setTypeface(latoBlack);
            maleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonActiveColor));
            femaleButton.setTypeface(latoRegular);
            femaleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonInactiveColor));
            maleBodyView.setImageResource(R.drawable.male_figure_active);
            femaleBodyView.setImageResource(R.drawable.female_figure_inactive);
            male = true;
        }
    }

    public void setFemaleButtonsActive() {
        Typeface latoBlack = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Black.ttf");
        Typeface latoRegular = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),  "fonts/Lato-Regular.ttf");
        if (male) {
            maleButton.setTypeface(latoRegular);
            maleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonInactiveColor));
            femaleButton.setTypeface(latoBlack);
            femaleButton.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonActiveColor));
            maleBodyView.setImageResource(R.drawable.male_figure_inactive);
            femaleBodyView.setImageResource(R.drawable.female_figure_active);
            male = false;
        }
    }


    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (requestCode.equals(GetGenderSubPartsConditionsProtocol.getInstance().getProtocolRequestCode())){
            handleGetGenderConditionsResponse(message);
        }
    }

    private void handleGetGenderConditionsResponse(String message) {
        mProgressDialog.hide();
        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            ApplicationState.sLastLinksArray = ServerDataManager.getInstance().mGenderConditionsSubParts;
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                    .toBundle();
            Intent intent = new Intent(getActivity(), SymptomCheckerBodySubPartsArrayActivity.class);
            startActivity(intent,bundle);
        } else{
            Toast.makeText(getActivity(),"Couldn't get data", Toast.LENGTH_LONG).show();
        }
    }
}
