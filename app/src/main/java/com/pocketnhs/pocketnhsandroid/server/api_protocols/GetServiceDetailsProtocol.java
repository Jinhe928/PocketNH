package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetServiceDetailsProtocol implements PocketNHSServerProtocol {

    public static final String ORGANISATION_TYPE_A_AND_E_PRACTICE = "types/srv0001/";


    public static final String LOCATIONS_TYPE_POSTCODE = "postcode";



    public static final String LOCATIONS_RANGE_STRING = "range=";

    private static final String URL_ENDPOINT =  ServerSettings.SERVER_BASE_URL
            +  "/services/";

    private static final String PROTOCOL_ID =  "Service Details";



    private static final String RESPONSE_ENTRY_ELEMENT = "entry";
    private static final String RESPONSE_ID_ELEMENT = "id";
    private static final String RESPONSE_TITLE_ELEMENT = "title";

    private static final String RESPONSE_UPDATED_ELEMENT = "updated";
    private static final String RESPONSE_LINK_ELEMENT = "link";
    private static final String RESPONSE_LINK_ATTRIBUTE_TYPE = "rel";
    private static final String RESPONSE_LINK_ATTRIBUTE_TITLE = "title";
    private static final String RESPONSE_LINK_ATTRIBUTE_HREF = "href";

    private static final String RESPONSE_LINK_ATTRIBUTE = "link";

    private static final String RESPONSE_CONTENT_ELEMENT = "content";

    private static final String RESPONSE_S_SERVICE_SUMMARY = "s:serviceSummary";

    private static final String RESPONSE_S_PHONE = "phone";
    private static final String RESPONSE_S_ADDRESS_LINE = "addressLine";
    private static final String RESPONSE_S_LONGITUDE = "longitude";
    private static final String RESPONSE_S_LATITUDE = "latitude";

    public static final String SERVICE_DETAILS_LINK = "service details link"
            ;
    public static final String SERVICE_INDEX = "index";
    private static final String RESPONSE_DELIVERER = "deliverer";


    private static PocketNHSServerProtocol mInstance = null;

    @Override
    public String getEndpointURL(Map<String, Object> params)
    {
        String endpoint =  (String) params.get(SERVICE_DETAILS_LINK);

        endpoint = endpoint + ".xml?"
                + ServerSettings.API_KEY_STR;

        return  endpoint;
    }

    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetServiceDetailsProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs ){
        Map inputMap = (Map) inputs;
        return (Integer) inputMap.get(SERVICE_INDEX);
    }


    @Override
    public boolean parseResponse(String response, Object output) {
        NHSService service = (NHSService) output;

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
                        } else if (service != null) {
                            if (name.equalsIgnoreCase(RESPONSE_ID_ELEMENT)) {
                                service.mID= parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_TITLE_ELEMENT)) {
                                service.mTitle=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_UPDATED_ELEMENT)) {
                                service.mUpdated=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_LINK_ELEMENT)) {
                                String type = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_TYPE);
                                String url = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_HREF);
                                String title = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_TITLE);

                                if (type.equals("self")){
                                    service.mLinkPairSelf= new NHSTextLinkPair();
                                    service.mLinkPairSelf.setText(title);
                                    service.mLinkPairSelf.setLink(url);
                                } else if (type.equals("alternate")) {
                                    service.mLinkPairAlternate= new NHSTextLinkPair();
                                    service.mLinkPairAlternate.setText(title);
                                    service.mLinkPairAlternate.setLink(url);
                                }
                            }  else if (name.equalsIgnoreCase(RESPONSE_S_LONGITUDE)) {
                                service.mLongitude = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_DELIVERER)) {
                                service.mDelivererURL = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE);
                                service.mDeliverer = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_S_LATITUDE)) {
                                service.mLatitude = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_S_PHONE)) {
                                service.mTelephone = parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_S_ADDRESS_LINE)) {
                                if (service.mAddress == null){
                                    service.mAddress = new ArrayList<String>();
                                }
                                service.mAddress.add(parser.nextText());
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

