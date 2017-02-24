package com.pocketnhs.pocketnhsandroid.server;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public interface PocketNHSServerProtocol {

        public String getEndpointURL(Map<String, Object> params);

        public boolean parseResponse(String response, Object outputs);

        String getProtocolRequestCode();

        int getDataIndex(Object inputs);

}
