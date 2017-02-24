package com.pocketnhs.pocketnhsandroid.globals;

import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryEventRecordStatus;

/**
 * Created by MacBook Pro on 9/7/2016.
 */

public class Analytics {

    public static String SERVICE_FINDER_START_EVENT = "Service Finder Start Event";
    public static String SYMPTOM_CHECKER_START_EVENT = "Symptom Checker Start Event";
    public static String HEALTH_AZ_START_EVENT = "Health AZ Start Event";
    public static String LIVE_WELL_START_EVENT = "Live Well StartEvent";
    public static String COMMON_HEALTH_QUESTIONS_START_EVENT = "Common Health Questions Start Event";
    public static String QUIZZES_START_EVENT = "Quizzes Start Event";
    public static String MY_LIBRARY_START_EVENT = "My Library Start Event";
    public static String MY_GP_START_EVENT = "My GP Start Event";
    public static String INFO_START_EVENT = "Info Start Event";

    public static void LogServiceFinderStartEvent(){
        logStatus(FlurryAgent.logEvent(SERVICE_FINDER_START_EVENT));
    }

    public static void LogSymptomCheckerStartEvent(){
        logStatus(FlurryAgent.logEvent(SYMPTOM_CHECKER_START_EVENT));
    }

    public static void LogHealthAZStartEvent(){
        logStatus(FlurryAgent.logEvent(HEALTH_AZ_START_EVENT));
    }

    public static void LogLiveWellStartEvent(){
        logStatus(FlurryAgent.logEvent(LIVE_WELL_START_EVENT));
    }


    public static void LogCommonHealthQuestionsStartEvent(){
        logStatus(FlurryAgent.logEvent(COMMON_HEALTH_QUESTIONS_START_EVENT));
    }

    public static void LogQuizzesStartEvent(){
        logStatus(FlurryAgent.logEvent(QUIZZES_START_EVENT));
    }

    public static void LogMyLibraryStartEvent(){
        logStatus(FlurryAgent.logEvent(MY_LIBRARY_START_EVENT));
    }

    public static void LogMyGPStartEvent(){
        logStatus(FlurryAgent.logEvent(MY_GP_START_EVENT));
    }

    public static void LogInfoStartEvent() {
        logStatus(FlurryAgent.logEvent(INFO_START_EVENT));
    }

    private static void logStatus(FlurryEventRecordStatus flurryEventRecordStatus) {
        if (flurryEventRecordStatus.equals(FlurryEventRecordStatus.kFlurryEventRecorded)){
            Log.d("ANALYTICS", "Event recorded");
        } else {
            Log.d("ANALYTICS", flurryEventRecordStatus.toString());
        }
    }


}
