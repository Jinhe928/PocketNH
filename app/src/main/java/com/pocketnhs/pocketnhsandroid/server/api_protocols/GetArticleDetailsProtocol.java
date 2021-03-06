package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import android.util.Log;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCHQArticle;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetArticleDetailsProtocol implements PocketNHSServerProtocol {

    //private static final String URL_ENDPOINT =  ServerSettings.SERVER_BASE_URL +  "/conditions/atoz/";

    private static final String PROTOCOL_ID =  "Get Article Details";
    public static final String ARTICLE_LINK = "article_link";

    private static final String RESPONSE_ENTRY_ELEMENT = "entry";
    private static final String RESPONSE_TITLE_ELEMENT = "title";
    private static final String RESPONSE_SUMMARY_ELEMENT = "summary";
    private static final String RESPONSE_CONTENT_ELEMENT = "content";


    private static PocketNHSServerProtocol mInstance = null;

    @Override
    public String getEndpointURL(Map<String, Object> params) {

        String endpoint = (String)params.get(ARTICLE_LINK);
        if (endpoint.indexOf("?") > 0 ) {
            endpoint = endpoint.substring(0, endpoint.indexOf("?"));
        }
        //String endpoint =  URL_ENDPOINT + params.get(CONDITION_LINK);
        endpoint = endpoint + ".xml"
                + "?" + ServerSettings.API_KEY_STR;
        return endpoint;

    }

    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetArticleDetailsProtocol();
        }
        return mInstance;
    }

    @Override
    public boolean parseResponse(String response, Object outputs) {
        NHSCHQArticle article = (NHSCHQArticle) outputs;
        boolean error = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response.substring(getSkipValue(response))));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (article != null) {
                            if (name.equalsIgnoreCase(RESPONSE_TITLE_ELEMENT)) {
                                article.mTitle = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_SUMMARY_ELEMENT)) {
                                article.mSummary = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_CONTENT_ELEMENT)) {
                                article.mText = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_CONTENT_ELEMENT)) {
                            Log.d("GET_COND_DET_PROTOCOL", "loaded condition info");
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            error = true;
        }
        return !error;
    }

    private int getSkipValue(String response) {
        // int SKIP_UTF8_BOM = 3;
        int skip = response.indexOf("<");
        return  skip;
    }

    @Override
    public int getDataIndex(Object inputs){
        return 0;
    }

    @Override
    public String getProtocolRequestCode() {
        return PROTOCOL_ID;
    }
}

