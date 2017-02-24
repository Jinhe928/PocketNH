package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetOrganizationDetailsProtocol implements PocketNHSServerProtocol {

    private static final String PROTOCOL_ID =  "Organisation Details";



    private static final String RESPONSE_ENTRY_ELEMENT = "entry";

    private static final String RESPONSE_NUMBER_OF_RATINGS = "NumberOfRatings";
    private static final String RESPONSE_RATINGS_VALUE = "value";

    public static final String ORGANISATION_DETAILS_LINK = "organisation details link"
            ;
    public static final String SERVICE_INDEX = "index";


    private static PocketNHSServerProtocol mInstance = null;

    @Override
    public String getEndpointURL(Map<String, Object> params)
    {
        String endpoint =  (String) params.get(ORGANISATION_DETAILS_LINK);
        endpoint = endpoint.substring(0, endpoint.indexOf("?"));

        endpoint = endpoint + ".xml?"
                + ServerSettings.API_KEY_STR;

        return  endpoint;
    }

    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetOrganizationDetailsProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs ){
        return 0;
    }


    @Override
    public boolean parseResponse(String response, Object output) {
        NHSOrganisation organisation = (NHSOrganisation) output;

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
                        if (name.equalsIgnoreCase(RESPONSE_ENTRY_ELEMENT)) {
                           // do nothing...
                        } else if (organisation != null) {
                            if (name.equalsIgnoreCase(RESPONSE_NUMBER_OF_RATINGS)) {
                                try {
                                    organisation.mNumberOfRatings = Integer.parseInt(parser.nextText());
                                }catch(NumberFormatException e){
                                    organisation.mNumberOfRatings = 0;
                                }
                            } else if (name.equalsIgnoreCase(RESPONSE_RATINGS_VALUE)) {
                                try {
                                    organisation.mRatingValue = Float.parseFloat(parser.nextText());
                                }catch(NumberFormatException e){
                                    organisation.mRatingValue = 0.0f;
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
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
        int skip = response.indexOf("<");
        return  skip;
    }

    @Override
    public String getProtocolRequestCode() {
        return PROTOCOL_ID;
    }
}

