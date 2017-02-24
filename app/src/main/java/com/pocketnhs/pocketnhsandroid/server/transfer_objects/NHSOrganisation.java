package com.pocketnhs.pocketnhsandroid.server.transfer_objects;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class NHSOrganisation {

    private static final String TAG = "NHSOrganization";

    public String mImageFilePath;

    public String mID;
    public String mTitle;
    public String mUpdated;
    public NHSTextLinkPair mLinkPairSelf;
    public NHSTextLinkPair mLinkPairAlternate;

    // summary
    public  String mName;
    public String mOdsCode;
    public List<String> mAddress;
    public  String mPostCode;

    // contact
    public  String mTelephone;
    public  String mEmail;
    public  String mLongitude;
    public  String mLatitude;

    public String mDistance;

    public float mRatingValue;
    public int mNumberOfRatings;
    public Marker mMarker;


    public static NHSOrganisation getOrganization(Map properties){

        NHSOrganisation organization = new NHSOrganisation();
        Field[] fields =  NHSOrganisation.class.getDeclaredFields();

        for (Field field: fields) {
            String name = field.getName();
            if (field.getType().isAssignableFrom(String.class)) {
                String value = (String) properties.get(name);
                try {
                    field.set(organization,value);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "REFLECTION couldn't assign value");
                }
            }
        }
        organization.mNumberOfRatings = (Integer) properties.get("mNumberOfRatings");
        organization.mRatingValue = ((Double) properties.get("mRatingValue")).floatValue();

        organization.mAddress = new ArrayList<>();
        Map map = (Map)properties.get("Address");
        String addressLine0 = (String) map.get("AddressLine0");
        String addressLine1 = (String) map.get("AddressLine1");
        String addressLine2 = (String) map.get("AddressLine2");
        organization.mAddress.add(addressLine0);
        organization.mAddress.add(addressLine1);
        organization.mAddress.add(addressLine2);


        return organization;
    }

    public Map getPropertiesHashMap() {
        Map properties = new HashMap();
        Field[] fields =  NHSOrganisation.class.getDeclaredFields();
        for (Field field: fields) {
            if (field.getType().isAssignableFrom(String.class) && isNonStatic(field)) {
                String value = null;
                try {
                    value = (String)field.get(this);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "REFLECTION couldn't read value");
                }
                String name = field.getName();
                if ( value != null){
                    properties.put(name,value);
                }

            }
        }

        properties.put("mRatingValue",mRatingValue);
        properties.put("mNumberOfRatings",mNumberOfRatings);

        if (mLinkPairSelf!=null) {
            Map mapPairSelf = new HashMap();
            mapPairSelf.put("mLinkPairSelf_Name", mLinkPairSelf.getText());
            mapPairSelf.put("mLinkPairSelf_URL", mLinkPairSelf.getLink());
            properties.put("PairSelf", mapPairSelf);
        }

        if (mLinkPairAlternate !=null) {
            Map mapPairAlt = new HashMap();
            mapPairAlt.put("mLinkPairAlt_Name", mLinkPairAlternate.getText());
            mapPairAlt.put("mLinkPairAlt_URL", mLinkPairAlternate.getLink());
            properties.put("PairAlt", mapPairAlt);
        }

        if (mAddress != null) {
            Map mapAddress = new HashMap();
            for (int i = 0; i < mAddress.size(); i++) {
                mapAddress.put("AddressLine" + i, mAddress.get(i));
            }
            properties.put("Address", mapAddress);
        }
        return properties;
    }

    private boolean isNonStatic(Field field) {
        return !java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }


    public String defineFilename() {
        mImageFilePath =  UUID.randomUUID().toString() + ".png";
        return  mImageFilePath ;
    }
}
