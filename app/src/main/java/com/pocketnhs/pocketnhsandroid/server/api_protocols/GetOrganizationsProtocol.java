package com.pocketnhs.pocketnhsandroid.server.api_protocols;

import com.pocketnhs.pocketnhsandroid.server.PocketNHSServerProtocol;
import com.pocketnhs.pocketnhsandroid.server.ServerSettings;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class GetOrganizationsProtocol implements PocketNHSServerProtocol {



    public static final String ORGANISATION_TYPE_GP_PRACTICE = "gppractices/";
    public static final String ORGANISATION_TYPE_DENTISTS = "dentists/";

    public static final String LOCATIONS_TYPE_POSTCODE = "postcode";


    public static final String ORGANISATION_TYPE_KEY = "organization type";
    public static final String LOCATIONS_TYPE_KEY = "location type";
    public static final String LOCATIONS_PARAM_KEY = "location param";
    public static final String LOCATIONS_RANGE_KEY = "location range";

    public static final String LOCATIONS_RANGE_STRING = "range=";

    private static final String URL_ENDPOINT =  ServerSettings.SERVER_BASE_URL
            +  "/organisations/";
    private static final String PROTOCOL_ID =  "GET CHANNELS";

    private static final String RESPONSE_ENTRY_ELEMENT = "entry";
    private static final String RESPONSE_ID_ELEMENT = "id";
    private static final String RESPONSE_TITLE_ELEMENT = "title";

    private static final String RESPONSE_UPDATED_ELEMENT = "updated";
    private static final String RESPONSE_LINK_ELEMENT = "link";
    private static final String RESPONSE_LINK_ATTRIBUTE_TYPE = "rel";
    private static final String RESPONSE_LINK_ATTRIBUTE_TITLE = "title";
    private static final String RESPONSE_LINK_ATTRIBUTE_HREF = "href";


    private static final String RESPONSE_CONTENT_ELEMENT = "content";

    private static final String RESPONSE_S_ORGANISATION_SUMMARY = "s:organisationSummary";

    private static final String RESPONSE_S_NAME = "organisationSummary";
    private static final String RESPONSE_S_ODS_CODE = "odscode";
    private static final String RESPONSE_S_ADDRESS = "address";
    private static final String RESPONSE_S_ADDRESS_LINE = "addressLine";
    private static final String RESPONSE_S_POSTCODE = "postcode";
    private static final String RESPONSE_S_CONTACT = "contact";
    private static final String RESPONSE_S_TELEPHONE = "telephone";
    private static final String RESPONSE_S_LONGITUDE = "longitude";
    private static final String RESPONSE_S_LATITUDE = "latitude";


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
            mInstance = new GetOrganizationsProtocol();
        }
        return mInstance;
    }

    @Override
    public int getDataIndex(Object inputs){
        return 0;
    }


    @Override
    public boolean parseResponse(String response, Object outputs) {
        List<NHSOrganisation> organisations = (List<NHSOrganisation>) outputs;

        boolean error = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response.substring(getSkipValue(response))));
            int eventType = parser.getEventType();
            NHSOrganisation organisation = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_ENTRY_ELEMENT)) {
                            organisation = new NHSOrganisation();
                        } else if (organisation != null) {
                            if (name.equalsIgnoreCase(RESPONSE_ID_ELEMENT)) {
                                organisation.mID= parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_TITLE_ELEMENT)) {
                                organisation.mTitle=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_UPDATED_ELEMENT)) {
                                organisation.mUpdated=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_LINK_ELEMENT)) {
                               String type = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_TYPE);
                                String url = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_HREF);
                                String title = parser.getAttributeValue("",RESPONSE_LINK_ATTRIBUTE_TITLE);

                                if (type.equals("self")){
                                    organisation.mLinkPairSelf= new NHSTextLinkPair();
                                    organisation.mLinkPairSelf.setText(title);
                                    organisation.mLinkPairSelf.setLink(url);
                                } else if (type.equals("alternate")) {
                                    organisation.mLinkPairAlternate= new NHSTextLinkPair();
                                    organisation.mLinkPairAlternate.setText(title);
                                    organisation.mLinkPairAlternate.setLink(url);
                                }
                            }  else if (name.equalsIgnoreCase(RESPONSE_S_LONGITUDE)) {
                                organisation.mLongitude=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_S_LATITUDE)) {
                                organisation.mLatitude=parser.nextText();
                            }  else if (name.equalsIgnoreCase(RESPONSE_S_TELEPHONE)) {
                                organisation.mTelephone=parser.nextText();
                            } else if (name.equalsIgnoreCase(RESPONSE_S_ADDRESS_LINE)){
                                if (organisation.mAddress == null){
                                    organisation.mAddress = new ArrayList<String>();
                                }
                                organisation.mAddress.add(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESPONSE_ENTRY_ELEMENT)
                                &&  organisation != null) {
                            organisations.add(organisation);
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

