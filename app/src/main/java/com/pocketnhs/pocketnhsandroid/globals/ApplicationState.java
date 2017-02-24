package com.pocketnhs.pocketnhsandroid.globals;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryAgentListener;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class ApplicationState extends Application {

    private static Context sContext;
    public static NHSTextLinkPairList sLastLinksArray;
    public static String sLastLiveWellURL;
    public static String sLastConditionURL;
    public static FlurryAgentListener sFlurryAgentListener;
    public static List<NHSTextLinkPair> sConditionsFromXml;
    public static NHSOrganisation sLastOrganisation;
    public static NHSService sLastService;
    public static boolean sIsShowingOrganization;
    public static String sLastQuizURL;
    public static NHSCondition sLastLibraryCondition;
    public static List<NHSTextLinkPair> subCategoryQuestionsList;
    public static String lastQuestionsUrl;
    public static String lastQuestionsTextUrl;
    public static String sLastCHQArticleURL;
    public static NHSTextLinkPairList sLastGenderConditionsLinksArray;
    public static String sLastGenderConditionsUrl;
    public static String sLastWebViewURL;

    //public static  FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ApplicationState.sContext = getApplicationContext();

        //.withListener(sFlurryAgentListener)
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .withLogLevel(Log.INFO)
                .withContinueSessionMillis(5000L)
                .withCaptureUncaughtExceptions(false)
                .withPulseEnabled(true)
                .build(sContext, ApplicationConsts.FLURRY_API_KEY);

        readConditionsFromFile();
    }

    public static Context getAppContext() {
        return sContext;
    }

    private void  readConditionsFromFile(){
        boolean error = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = getResources().getXml(R.xml.conditions_v1);
            int eventType = parser.getEventType();

            sConditionsFromXml = new ArrayList<>();
            NHSTextLinkPair pair = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("Link")) {
                            pair = new NHSTextLinkPair();
                        } else if (pair != null) {
                            if (name.equalsIgnoreCase("Text")) {
                                pair.setText(parser.nextText());
                            } else if (name.equalsIgnoreCase("Uri")) {
                                pair.setLink(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Link")){
                            if (pair!=null){
                                sConditionsFromXml.add(pair);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            error = true;
        }

    }

    public static String[] getConditionNames() {
        if (sConditionsFromXml!=null && sConditionsFromXml.size()>0) {
            String[] names = new String[sConditionsFromXml.size()];
            for (int i = 0; i< sConditionsFromXml.size(); i++ ) {
                names[i]= sConditionsFromXml.get(i).getText();
            }
            return names;
        } else {
            return new String[0];
        }
    }

    public static String getConditionLink(String conditionText) {
        String link = "";
        if (sConditionsFromXml != null) {
            for (int i = 0;i<sConditionsFromXml.size() ; i++){
                if (sConditionsFromXml.get(i).getText().equalsIgnoreCase(conditionText)){
                    link = sConditionsFromXml.get(i).getLink();
                }
            }
        }
        return link;
    }

    public static boolean isShowingOrganization() {
        return sIsShowingOrganization;
    }
}
