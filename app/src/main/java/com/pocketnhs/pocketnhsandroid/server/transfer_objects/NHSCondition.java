package com.pocketnhs.pocketnhsandroid.server.transfer_objects;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MacBook Pro on 9/1/2016.
 */

public class NHSCondition {
    public String mID;
    public String mTitle;
    public String mText;
    public String mSummary;
    public String type;

    public Map getPropertiesHashMap() {
        Map properties = new HashMap();
        Field[] fields =  NHSCondition.class.getDeclaredFields();
        for (Field field: fields) {
            if (field.getType().isAssignableFrom(String.class) && isNonStatic(field)) {
                String value = null;
                try {
                    value = (String)field.get(this);
                } catch (IllegalAccessException e) {
                    Log.e("NHSCondition", "REFLECTION couldn't read value");
                }
                String name = field.getName();
                if ( value != null){
                    properties.put(name,value);
                }

            }
        }
        return properties;
    }

    private boolean isNonStatic(Field field) {
        return !java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }


    public static NHSCondition getCondition(Map properties){

        NHSCondition condition = new NHSCondition();
        Field[] fields =  NHSCondition.class.getDeclaredFields();

        for (Field field: fields) {
            String name = field.getName();
            if (field.getType().isAssignableFrom(String.class)) {
                String value = (String) properties.get(name);
                try {
                    field.set(condition,value);
                } catch (IllegalAccessException e) {
                    Log.e("NHSCondition", "REFLECTION couldn't assign value");
                }
            }
        }
        return condition;
    }

}
