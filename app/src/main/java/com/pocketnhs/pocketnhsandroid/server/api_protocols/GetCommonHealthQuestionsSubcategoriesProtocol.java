package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetCommonHealthQuestionsSubcategoriesProtocol implements PocketNHSServerProtocol {

    private static final String URL_ENDPOINT =  ServerSettings.SERVER_BASE_URL +  "/chq/articles";

    private static final String PROTOCOL_ID =  "CHQ Subcategory ";

    public static final String DETAILS_LINK = "index letter";

    //private static final String RESPONSE_ROOT_ELEMENT = "ArrayOfLink";
    private static final String RESPONSE_LINK_ELEMENT = "Link";
    private static final String RESPONSE_TEXT_ELEMENT = "text";
    private static final String RESPONSE_URI_ELEMENT = "uri";
    private static final String RESPONSE_SUBCATEGORIES = "Subcategories";
    private static final String RESPONSE_ARTICLES = "Articles";


    private static PocketNHSServerProtocol mInstance = null;

    @Override
    public String getEndpointURL(Map<String, Object> params) {

        String endpoint =  (String)params.get(DETAILS_LINK);
        endpoint = endpoint + ".xml"
                + "?" + ServerSettings.API_KEY_STR;
        return endpoint;
    }

    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetCommonHealthQuestionsSubcategoriesProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs){
        return 0;
    }

    @Override
    public boolean parseResponse(String response, Object outputs) {
        NHSTextLinkPairList links = (NHSTextLinkPairList) outputs;
        boolean error = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response.substring(getSkipValue(response))));
            int eventType = parser.getEventType();
            NHSTextLinkPair textLinkPair = null;
            boolean subcategoriesOrArticlesStarted = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_LINK_ELEMENT)) {
                            textLinkPair = new NHSTextLinkPair();
                        } else if (textLinkPair != null) {
                            if (name.equalsIgnoreCase(RESPONSE_TEXT_ELEMENT)) {
                                textLinkPair.setText(parser
                                        .nextText());
                            } else if (name.equalsIgnoreCase(RESPONSE_URI_ELEMENT)) {
                                textLinkPair.setLink(parser
                                        .nextText());
                            } else if (name.equalsIgnoreCase(RESPONSE_SUBCATEGORIES)){
                                subcategoriesOrArticlesStarted = true;
                            }
                              else if (name.equalsIgnoreCase(RESPONSE_ARTICLES)){
                                subcategoriesOrArticlesStarted = true;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_LINK_ELEMENT)
                                && textLinkPair != null) {
                            if (isCHQ(textLinkPair) && subcategoriesOrArticlesStarted) {
                                links.list.add(textLinkPair);
                            }
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


    private boolean isCHQ(NHSTextLinkPair textLinkPair) {
        if (textLinkPair.mLink.contains("/chq/")){
            return true;
        }else {
            return false;
        }
    }

    private int getSkipValue(String response) {
        // int SKIP_UTF8_BOM = 3;
        int skip = response.indexOf("<");
        return  skip;
    }

    @Override
    public String getProtocolRequestCode() {
        return PROTOCOL_ID;
    }
}

