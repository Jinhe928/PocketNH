package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetServicesProtocol implements PocketNHSServerProtocol {

    public static final String ORGANISATION_TYPE_A_AND_E_PRACTICE = "types/srv0001/";


    public static final String LOCATIONS_TYPE_POSTCODE = "postcode";


    public static final String ORGANISATION_TYPE_KEY = "organization type";
    public static final String LOCATIONS_TYPE_KEY = "location type";
    public static final String LOCATIONS_PARAM_KEY = "location param";
    public static final String LOCATIONS_RANGE_KEY = "location range";

    public static final String LOCATIONS_RANGE_STRING = "range=";

    private static final String URL_ENDPOINT =  ServerSettings.SERVER_BASE_URL
            +  "/services/";
    private static final String PROTOCOL_ID =  "A and E Services";



    private static final String RESPONSE_ENTRY_ELEMENT = "entry";
    private static final String RESPONSE_ID_ELEMENT = "id";
    private static final String RESPONSE_TITLE_ELEMENT = "title";

    private static final String RESPONSE_UPDATED_ELEMENT = "updated";
    private static final String RESPONSE_LINK_ELEMENT = "link";
    private static final String RESPONSE_LINK_ATTRIBUTE_TYPE = "rel";
    private static final String RESPONSE_LINK_ATTRIBUTE_TITLE = "title";
    private static final String RESPONSE_LINK_ATTRIBUTE_HREF = "href";


    private static final String RESPONSE_CONTENT_ELEMENT = "content";

    private static final String RESPONSE_S_SERVICE_SUMMARY = "s:serviceSummary";

    private static final String RESPONSE_S_DELIVERER_NAME = "name";


    private static PocketNHSServerProtocol mInstance = null;

    @Override
    public String getEndpointURL(Map<String, Object> params)
    {
        String endpoint = URL_ENDPOINT;

        String organizationType = (String) params.get(ORGANISATION_TYPE_KEY);
        String locationType = (String) params.get(LOCATIONS_TYPE_KEY);
        String locationParam = (String) params.get(LOCATIONS_PARAM_KEY);
        String locationRange = (String) params.get(LOCATIONS_RANGE_KEY);

        endpoint = endpoint + organizationType + locationType
                + "/" + locationParam + ".xml?"
                + ServerSettings.API_KEY_STR
                + "&" + LOCATIONS_RANGE_STRING + locationRange;

        return  endpoint;
    }

    public static synchronized PocketNHSServerProtocol getInstance() {
        if (mInstance == null) {
            mInstance = new GetServicesProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs){
        return 0;
    }


    @Override
    public boolean parseResponse(String response, Object outputs) {
        List<NHSService> services = (List<NHSService>) outputs;

        boolean error = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response.substring(getSkipValue(response))));
            int eventType = parser.getEventType();
            NHSService service = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_ENTRY_ELEMENT)) {
                            service = new NHSService();
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
                            }  else if (name.equalsIgnoreCase(RESPONSE_S_DELIVERER_NAME)) {
                                service.mName = parser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_ENTRY_ELEMENT)
                                &&  service != null) {
                            services.add(service);
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
        int skip = response.indexOf("<");
        return  skip;
    }

    @Override
    public String getProtocolRequestCode() {
        return PROTOCOL_ID;
    }
}

