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

public class GetGenderSubPartsConditionsProtocol implements PocketNHSServerProtocol {

    private static final String URL_ENDPOINT = ServerSettings.SERVER_BASE_URL + "/conditions/genders/";
    private static final String PROTOCOL_ID = "GET CHANNELS";

    public static final String INPUT_PARAM_GENDER = "gender";
    public static final String INPUT_PARAM_BODY_PART = "body part";

    public static final String REQUEST_PARAM_GENDER_MALE = "male";
    public static final String REQUEST_PARAM_GENDER_FEMALE = "female";

    private static final String REQUEST_BUILD_STRING_BODY_PART = "bodyparts";

    public static final String REQUEST_PARAM_BODY_PART_HEAD = "head";
    public static final String REQUEST_PARAM_BODY_PART_CHEST = "chest";
    public static final String REQUEST_PARAM_BODY_PART_ARM = "arm";
    public static final String REQUEST_PARAM_BODY_PART_ABDOMEN = "abdomen";
    public static final String REQUEST_PARAM_BODY_PART_LEG = "leg";

    private static final String RESPONSE_LINK_ELEMENT = "Link";
    private static final String RESPONSE_TEXT_ELEMENT = "Text";
    private static final String RESPONSE_URI_ELEMENT = "Uri";

    private static PocketNHSServerProtocol mInstance = null;


    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetGenderSubPartsConditionsProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs){
        return 0;
    }

    @Override
    public String getEndpointURL(Map<String, Object> params) {
        String endpoint = URL_ENDPOINT;
        String gender = (String) params.get(INPUT_PARAM_GENDER);
        String bodyPart = (String) params.get(INPUT_PARAM_BODY_PART);

        endpoint = endpoint + gender
                + "/" + REQUEST_BUILD_STRING_BODY_PART
                + "/" + bodyPart
                + ".xml?" + ServerSettings.API_KEY_STR;


        return endpoint;

        //    <a href="http://v1.syndication.nhschoices.nhs.uk/conditions/genders/female/bodyparts/arm?apikey=WDUATIMA">
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
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_LINK_ELEMENT)
                                && textLinkPair != null) {
                            links.list.add(textLinkPair);
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
    public String getProtocolRequestCode() {
        return PROTOCOL_ID;
    }
}

