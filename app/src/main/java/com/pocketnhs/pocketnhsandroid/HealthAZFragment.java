package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetConditionsProtocol;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;
import com.pocketnhs.pocketnhsandroid.ui.AtoZLinksAdapter;

/**
 * Created by MacBook Pro on 24.8.2016..
 */
public class HealthAZFragment extends Fragment implements DataReadyListener {

    boolean[] active = new boolean[27];
    Button[] mButtons = new Button[27];

    RecyclerView mRecyclerView;
    AtoZLinksAdapter mAdapter;
    NHSTextLinkPairList mCommonConditions;
    AutoCompleteTextView mSearchConditions;

    public static HealthAZFragment newInstance() {
        return new HealthAZFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active[0] = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_health_a_z, container, false);
        mButtons[0] = (Button) v.findViewById(R.id.aButton);
        mButtons[1] = (Button) v.findViewById(R.id.bButton);
        mButtons[2] = (Button) v.findViewById(R.id.cButton);
        mButtons[3] = (Button) v.findViewById(R.id.dButton);
        mButtons[4] = (Button) v.findViewById(R.id.eButton);
        mButtons[5] = (Button) v.findViewById(R.id.fButton);
        mButtons[6] = (Button) v.findViewById(R.id.gButton);
        mButtons[7] = (Button) v.findViewById(R.id.hButton);
        mButtons[8] = (Button) v.findViewById(R.id.iButton);
        mButtons[9] = (Button) v.findViewById(R.id.jButton);
        mButtons[10] = (Button) v.findViewById(R.id.kButton);
        mButtons[11] = (Button) v.findViewById(R.id.lButton);
        mButtons[12] = (Button) v.findViewById(R.id.mButton);
        mButtons[13] = (Button) v.findViewById(R.id.nButton);
        mButtons[14] = (Button) v.findViewById(R.id.oButton);
        mButtons[15] = (Button) v.findViewById(R.id.pButton);
        mButtons[16] = (Button) v.findViewById(R.id.qButton);
        mButtons[17] = (Button) v.findViewById(R.id.rButton);
        mButtons[18] = (Button) v.findViewById(R.id.sButton);
        mButtons[19] = (Button) v.findViewById(R.id.tButton);
        mButtons[20] = (Button) v.findViewById(R.id.uButton);
        mButtons[21] = (Button) v.findViewById(R.id.yButton);
        mButtons[22] = (Button) v.findViewById(R.id.zButton);
        mButtons[23] = (Button) v.findViewById(R.id.vButton);
        mButtons[24] = (Button) v.findViewById(R.id.wButton);
        mButtons[25] = (Button) v.findViewById(R.id.xButton);
        mButtons[26] = (Button) v.findViewById(R.id.zeroNineButton);



        Typeface latoBold = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "fonts/Lato-Bold.ttf");
        Typeface latoBlack = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "fonts/Lato-Black.ttf");
        Typeface latoRegular = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "fonts/Lato-Regular.ttf");


        TextView browseByIndexTV = (TextView) v.findViewById(R.id.browseByIndex);
        browseByIndexTV.setTypeface(latoBold);
        final TextView commonConditionsTV = (TextView) v.findViewById(R.id.commonConditions);
        commonConditionsTV.setTypeface(latoBold);
        commonConditionsTV.setVisibility(View.VISIBLE);

        for (int i = 0; i < 27; i++) {
            mButtons[i].setTypeface(latoBold);
            if (i == 0) {
                mButtons[i].setTextColor(ContextCompat.getColor(getContext(), R.color.symptomCheckerButtonTextColorActive));
                mButtons[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.active_letter_bg));
            } else {
                mButtons[i].setTextColor(ContextCompat.getColor(getContext(), R.color.symptomCheckerButtonTextColorInactive));
                mButtons[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.inactive_letter_bg));
            }
            final int k = i;
            mButtons[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!active[k]) {
                        mButtons[k].setTextColor(ContextCompat.getColor(getContext(), R.color.symptomCheckerButtonTextColorActive));
                        mButtons[k].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.active_letter_bg));
                        active[k] = true;
                        for (int j = 0; j < 27; j++) {
                            if ((j != k) && (active[j])) {
                                active[j] = false;
                                mButtons[j].setTextColor(ContextCompat.getColor(getContext(), R.color.symptomCheckerButtonTextColorInactive));
                                mButtons[j].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.inactive_letter_bg));
                            }
                        }
                    }
                    int leterIndex = k;
                    String letter = getIndexLetter(leterIndex);
                    commonConditionsTV.setVisibility(View.INVISIBLE);
                    if (letter.length() == 1) {
                        ServerDataManager.getInstance().getConditionsAZ(letter, HealthAZFragment.this);
                    } else {
                        ServerDataManager.getInstance().getConditions09(letter, HealthAZFragment.this);
                    }
                }
            });
        }

        setupRecyclerView(v);
        setupToolbar(v);
        setupAutoCompleteTextView(v);

        return v;
    }

    private void setupAutoCompleteTextView(View v) {
        mSearchConditions = (AutoCompleteTextView) v.findViewById(R.id.search_conditions);
        String[] conditions = ApplicationState.getConditionNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,conditions);
        mSearchConditions.setAdapter(adapter);
        mSearchConditions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                String item=  mSearchConditions.getAdapter().getItem(pos).toString();
                Intent myIntent = new Intent(getActivity(), ShowConditionItemDetails.class);
                ApplicationState.sLastConditionURL = ApplicationState.getConditionLink(item);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                        .toBundle();
                startActivity(myIntent,bundle);
            }
        });
        mSearchConditions.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //if(actionId== EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                if(keyEvent.getAction() ==  KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String item =  mSearchConditions.getText().toString();
                    Intent myIntent = new Intent(getActivity(), ShowConditionItemDetails.class);
                    ApplicationState.sLastConditionURL = ApplicationState.getConditionLink(item);
                    if (ApplicationState.sLastConditionURL != null && ApplicationState.sLastConditionURL.length() > 0) {
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                                .toBundle();
                        startActivity(myIntent, bundle);
                    }else{
                        Toast.makeText(getActivity(),"No such condition",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    private void setupToolbar(View v) {

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) v.findViewById(R.id.title_view);
        tvTitle.setText("Health A-Z");
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        /*if (ServerDataManager.getInstance().mConditionsAZ == null) {
            ServerDataManager.getInstance().mConditionsAZ  = new NHSTextLinkPairList();
        }
        */
        setupCommonConditionsList();
        mAdapter = new AtoZLinksAdapter(mCommonConditions, getContext(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupCommonConditionsList() {

        mCommonConditions = new NHSTextLinkPairList();

        NHSTextLinkPair c0 = new NHSTextLinkPair();
        c0.setText("Chickenpox");
        c0.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/chickenpox");
        mCommonConditions.list.add(c0);

        NHSTextLinkPair c1 = new NHSTextLinkPair();
        c1.setText("Thrush");
        c1.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/thrush");
        mCommonConditions.list.add(c1);

        NHSTextLinkPair c2 = new NHSTextLinkPair();
        c2.setText("Depression");
        c2.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/depression");
        mCommonConditions.list.add(c2);

        NHSTextLinkPair c3 = new NHSTextLinkPair();
        c3.setText("Sciatica");
        c3.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/sciatica");
        mCommonConditions.list.add(c3);

        NHSTextLinkPair c4 = new NHSTextLinkPair();
        c4.setText("Norovirus infections");
        c4.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/norovirus");
        mCommonConditions.list.add(c4);

        NHSTextLinkPair c5 = new NHSTextLinkPair();
        c5.setText("Diabetes");
        c5.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/diabetes-type2");
        mCommonConditions.list.add(c5);

        NHSTextLinkPair c6 = new NHSTextLinkPair();
        c6.setText("Back pain");
        c6.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/back-pain");
        mCommonConditions.list.add(c6);

        NHSTextLinkPair c7 = new NHSTextLinkPair();
        c7.setText("Glandular fever");
        c7.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/glandular-fever");
        mCommonConditions.list.add(c7);

        NHSTextLinkPair c8 = new NHSTextLinkPair();
        c8.setText("Menopause");
        c8.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/menopause");
        mCommonConditions.list.add(c8);

        NHSTextLinkPair c9 = new NHSTextLinkPair();
        c9.setText("Kidney infection");
        c9.setLink("http://v1.syndication.nhschoices.nhs.uk/conditions/articles/Kidney-infection");
        mCommonConditions.list.add(c9);
    }


    private String getIndexLetter(int letterIndex) {
        String letter = "";
        switch (letterIndex) {
            case 0:
                letter = "a";
                break;
            case 1:
                letter = "b";
                break;
            case 2:
                letter = "c";
                break;
            case 3:
                letter = "d";
                break;
            case 4:
                letter = "e";
                break;
            case 5:
                letter = "f";
                break;
            case 6:
                letter = "g";
                break;
            case 7:
                letter = "h";
                break;
            case 8:
                letter = "i";
                break;
            case 9:
                letter = "j";
                break;
            case 10:
                letter = "k";
                break;
            case 11:
                letter = "l";
                break;
            case 12:
                letter = "m";
                break;
            case 13:
                letter = "n";
                break;
            case 14:
                letter = "o";
                break;
            case 15:
                letter = "p";
                break;
            case 16:
                letter = "q";
                break;
            case 17:
                letter = "r";
                break;
            case 18:
                letter = "s";
                break;
            case 19:
                letter = "t";
                break;
            case 20:
                letter = "u";
                break;
            case 21:
                letter = "y";
                break;
            case 22:
                letter = "z";
                break;
            case 23:
                letter = "v";
                break;
            case 24:
                letter = "w";
                break;
            case 25:
                letter = "x";
                break;
            case 26:
                letter = "0123456789";
                break;

        }
        return letter;
    }

    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (requestCode.equals(GetConditionsProtocol.getInstance().getProtocolRequestCode())) {
            handleGetConditionAZ(message);
        }
    }

    private void handleGetConditionAZ(String message) {
        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            mAdapter = new AtoZLinksAdapter(ServerDataManager.getInstance().mConditionsAZ, getContext(), getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Couldn't fetch condition data", Toast.LENGTH_SHORT).show();
        }
    }
}
