package com.pocketnhs.pocketnhsandroid.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationConsts;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetArticleDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetCommonHealthQuestionsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetCommonHealthQuestionsSubcategoriesProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetCommonHealthQuestionsTextProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetConditionDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetConditionsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetGenderConditionsListProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetGenderSubPartsConditionsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetOrganizationDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetOrganizationsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetServiceDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetServicesProtocol;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCHQArticle;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public class ServerDataManager {


    private static ServerDataManager mInstance;

    private Context context;

    //Output Data

    public NHSTextLinkPairList mGenderConditionsSubParts;
    public NHSTextLinkPairList mConditionsAZ;
    public NHSTextLinkPairList mQuestions;
    public List<NHSOrganisation> mOrganizations;
    public List<NHSService> mServices;
    public NHSCondition mConditionDetails;
    public NHSOrganisation mDeliverer;
    public NHSOrganisation mSaveOrganisationObject;
    public NHSTextLinkPairList mQuestionCategories;
    public NHSCHQArticle mCHQArticle;


    public static synchronized ServerDataManager getInstance() {
        if (mInstance == null) {
            mInstance = new ServerDataManager();
            initInstance();
        }
        return mInstance;
    }

    private static void initInstance() {
        mInstance.context = ApplicationState.getAppContext();
    }


    private boolean executeRequest(final PocketNHSServerProtocol serverProtocol, final Map<String,Object> inputs, final Object outputs, final DataReadyListener listener) {

        boolean error = false;
        String url = serverProtocol.getEndpointURL(inputs);

        if (url != null) {
            StringRequest jsObjRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (serverProtocol.parseResponse(response, outputs) == true) {
                                if (listener !=null) {
                                    listener.DataReady(DataReadyListener.MESSAGE_OK, serverProtocol.getProtocolRequestCode(), serverProtocol.getDataIndex(inputs));
                                }
                            } else {
                                if (listener !=null) {
                                    listener.DataReady("couldn't get server results", serverProtocol.getProtocolRequestCode(), serverProtocol.getDataIndex(inputs));
                                }
                            }
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error != null) {
                                if (listener !=null) {
                                    listener.DataReady(error.toString(), serverProtocol.getProtocolRequestCode(), serverProtocol.getDataIndex(inputs));
                                }
                            } else {
                                if (listener !=null) {
                                    listener.DataReady("Volley Error null", serverProtocol.getProtocolRequestCode(), serverProtocol.getDataIndex(inputs));
                                }
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/xml");
                    //headers.put("Authorization", "key="+ ApplicationConsts.NHS_API_KEY);
                    return headers;
                }
            };


            jsObjRequest.setRetryPolicy(new
                                                RetryPolicy() {
                                                    @Override
                                                    public int getCurrentTimeout() {
                                                        return 15000;
                                                    }

                                                    @Override
                                                    public int getCurrentRetryCount() {
                                                        return 0;
                                                    }

                                                    @Override
                                                    public void retry(VolleyError error) throws VolleyError {
                                                    }
                                                }

            );
            ServerDataFetcher.getInstance(context).
                    addToRequestQueue(jsObjRequest);
        } else {
            error = true;
        }
        return error;
    }


    // -----------------------------------------------------------------------------//
    //
    //                         API 1 - GetOrganizations
    //
    // -----------------------------------------------------------------------------//

    public boolean getOrganizationsByPostcode(String organizationType, String postcode, String range, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetOrganizationsProtocol.ORGANISATION_TYPE_KEY, organizationType);
        params.put(GetOrganizationsProtocol.LOCATIONS_TYPE_KEY, GetOrganizationsProtocol.LOCATIONS_TYPE_POSTCODE);
        params.put(GetOrganizationsProtocol.LOCATIONS_PARAM_KEY, postcode);
        params.put(GetOrganizationsProtocol.LOCATIONS_RANGE_KEY, range);
        mOrganizations = new ArrayList<>();

        return executeRequest(GetOrganizationsProtocol.getInstance(), params, mOrganizations, listener);
    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 2 - Get A&E Services
    //
    // -----------------------------------------------------------------------------//

    public boolean getAandEServicesByPostcode(String organizationType, String postcode, String range, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetServicesProtocol.ORGANISATION_TYPE_KEY, organizationType);
        params.put(GetServicesProtocol.LOCATIONS_TYPE_KEY, GetOrganizationsProtocol.LOCATIONS_TYPE_POSTCODE);
        params.put(GetServicesProtocol.LOCATIONS_PARAM_KEY, postcode);
        params.put(GetServicesProtocol.LOCATIONS_RANGE_KEY, range);
        mServices = new ArrayList<NHSService>();
        Log.d("SDM", "A adn E");

        return executeRequest(GetServicesProtocol.getInstance(), params, mServices, listener);
    }

    //http://v1.syndication.nhschoices.nhs.uk/services/types/srv0001



    // -----------------------------------------------------------------------------//
    //
    //                         API 3 - GetConditions
    //
    // -----------------------------------------------------------------------------//
    public boolean getConditionsAZ(String indexLetter, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetConditionsProtocol.INDEX_LETTER, indexLetter);
        mConditionsAZ = new NHSTextLinkPairList();

        return executeRequest(GetConditionsProtocol.getInstance(), params, mConditionsAZ , listener);
    }

    public boolean getConditions09(String letter, final DataReadyListener listener) {
        // Fetch all numbers (currently only 2 is not empty so I read only that)
        String NONEMPTY_ARRAY_INDEX = "2";
        Map<String, Object> params = new HashMap<>();
        params.put(GetConditionsProtocol.INDEX_LETTER, NONEMPTY_ARRAY_INDEX);
        mConditionsAZ = new NHSTextLinkPairList();

        return executeRequest(GetConditionsProtocol.getInstance(), params, mConditionsAZ , listener);
    }


    // -----------------------------------------------------------------------------//
    //
    //                         API 4 - Get Gender Sub Parts
    //
    // -----------------------------------------------------------------------------//
    public boolean getGenderSubParts(String gender, String bodyPart, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        mGenderConditionsSubParts = new NHSTextLinkPairList();

        params.put(GetGenderSubPartsConditionsProtocol.INPUT_PARAM_GENDER, gender);
        params.put(GetGenderSubPartsConditionsProtocol.INPUT_PARAM_BODY_PART, bodyPart);

        return executeRequest(GetGenderSubPartsConditionsProtocol.getInstance(), params, mGenderConditionsSubParts, listener);
    }


    // -----------------------------------------------------------------------------//
    //
    //                         API 4A - Get Gender Conditions
    //
    // -----------------------------------------------------------------------------//

    public boolean getGenderConditions(String url, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        ApplicationState.sLastGenderConditionsLinksArray = new NHSTextLinkPairList();

        params.put(GetGenderConditionsListProtocol.LIST_LINK, url);


        return executeRequest(GetGenderConditionsListProtocol.getInstance(), params, ApplicationState.sLastGenderConditionsLinksArray, listener);
    }




    // -----------------------------------------------------------------------------//
    //
    //                         API 5 - Get Condition Details
    //
    // -----------------------------------------------------------------------------//
    public boolean getConditionDetails(String conditionLink, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetConditionDetailsProtocol.CONDITION_LINK, conditionLink);
        mConditionDetails = new NHSCondition();

        return executeRequest(GetConditionDetailsProtocol.getInstance(), params, mConditionDetails , listener);
    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 6 - Get Service Details
    //
    // -----------------------------------------------------------------------------//

    public boolean getServiceDetails(int serviceIndex, final DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        NHSService outputService = mServices.get(serviceIndex);
        params.put(GetServiceDetailsProtocol.SERVICE_DETAILS_LINK, outputService.mID);
        params.put(GetServiceDetailsProtocol.SERVICE_INDEX,serviceIndex);
        Log.d("SDM", "A and E details for:" + serviceIndex);

        return executeRequest(GetServiceDetailsProtocol.getInstance(), params, outputService , listener);

    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 7 - Get Organisation Details
    //
    // -----------------------------------------------------------------------------//

    public boolean getOrganisationDetails(NHSOrganisation organisation, final DataReadyListener listener) {
        mDeliverer = null;
        Map<String, Object> params = new HashMap<>();
        params.put(GetOrganizationDetailsProtocol.ORGANISATION_DETAILS_LINK, organisation.mLinkPairSelf.getLink());
        Log.d("SDM", "Organisation details for:" + organisation.mName);

        return executeRequest(GetOrganizationDetailsProtocol.getInstance(), params, organisation , listener);

    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 8 - Get Deliverer Details
    //
    // -----------------------------------------------------------------------------//
    public boolean getDelivererDetails(String delivererURL, final DataReadyListener listener) {
        mDeliverer = new NHSOrganisation();
        Map<String, Object> params = new HashMap<>();
        params.put(GetOrganizationDetailsProtocol.ORGANISATION_DETAILS_LINK, delivererURL);
        Log.d("SDM", "Deliverer details for:" + delivererURL);

        return executeRequest(GetOrganizationDetailsProtocol.getInstance(), params, mDeliverer , listener);

    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 9 - Get Static Map
    //
    // -----------------------------------------------------------------------------//
    public ImageLoader.ImageContainer getStaticMap(NHSOrganisation organisation, final ImageLoader.ImageListener listener) {
        String requestUrl = getGoogleImageUrl(organisation.mLatitude, organisation.mLongitude);
        Log.w("staticURL",requestUrl);
        mSaveOrganisationObject = organisation;
        ImageLoader.ImageContainer container = ServerDataFetcher.getInstance(ApplicationState.getAppContext()).getImageLoader().get(requestUrl,listener);
        return container;
    }

    private String getGoogleImageUrl(String lat, String longi) {
        //&markers=color:green%7Clabel:G%7C40.711614,-74.012318
        return "http://maps.google.com/maps/api/staticmap?center=" +lat + "," + longi
                + "&markers=color:green%7C"+lat+"," + longi
                + "&zoom=14&size=400x400&API_KEY="
                + ApplicationConsts.STATIC_GOOGLE_MAPS_KEY;
    }


    // -----------------------------------------------------------------------------//
    //
    //                         API 10 - Get Common Health Questions
    //
    // -----------------------------------------------------------------------------//
    public boolean getCommonHealthQuestions(final DataReadyListener listener) {
        mQuestions = new NHSTextLinkPairList();
        return executeRequest(GetCommonHealthQuestionsProtocol.getInstance(), null, mQuestions , listener);

    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 10 - Get Common Health Subcategories
    //
    // -----------------------------------------------------------------------------//

    public boolean getCommonHealthQuestionSubcategories(String lastQuestionsUrl, DataReadyListener listener) {
        mQuestionCategories = new NHSTextLinkPairList();
        Map<String, Object> params = new HashMap<>();
        params.put(GetCommonHealthQuestionsSubcategoriesProtocol.DETAILS_LINK, lastQuestionsUrl);


        return executeRequest(GetCommonHealthQuestionsSubcategoriesProtocol.getInstance(), params, mQuestionCategories , listener);
    }

    // -----------------------------------------------------------------------------//
    //
    //                         API 11 - Get Common Health Question Text
    //
    // -----------------------------------------------------------------------------//

    public boolean getCommonHealthQuestionText(String lastQuestionsTextUrl, DataReadyListener listener) {
        mQuestions = new NHSTextLinkPairList();
        Map<String, Object> params = new HashMap<>();
        params.put(GetCommonHealthQuestionsTextProtocol.DETAILS_LINK, lastQuestionsTextUrl);

        return executeRequest(GetCommonHealthQuestionsTextProtocol.getInstance(), params, mQuestions , listener);
    }


    // -----------------------------------------------------------------------------//
    //
    //                         API 12 - Get Common Health Article details
    //
    // -----------------------------------------------------------------------------//
    public boolean getArticleDetails(String url, DataReadyListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put(GetArticleDetailsProtocol.ARTICLE_LINK, url);
        mCHQArticle = new NHSCHQArticle();

        return executeRequest(GetArticleDetailsProtocol.getInstance(), params, mCHQArticle , listener);

    }
}
