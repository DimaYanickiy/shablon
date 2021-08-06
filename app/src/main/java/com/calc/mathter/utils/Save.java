package com.calc.mathter.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class Save {
    
    SharedPreferences saver;
    public boolean firstRef, firstFlyer, first;
    public String point;
    JSONObject jsonParams;

    public Save(Context context) throws JSONException {
        saver = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        getData();
        unpackData();
    }

    public String getData(){
        return saver.getString("File", "{\"first_ref\":\"true\",\"first_flyer\":\"true\",\"first\":\"true\",\"point\":\"\"}");
    }

    public void setData(String file){
        saver.edit().putString("File", file).apply();
    }

    public void unpackData() throws JSONException {
        jsonParams = new JSONObject(getData());
        firstRef = jsonParams.optBoolean("first_ref");
        firstFlyer = jsonParams.optBoolean("first_flyer");
        first = jsonParams.optBoolean("first");
        point = jsonParams.optString("point");
    }

    public void packData(boolean first_param, boolean second_param, boolean third_param, String fourth_param) throws JSONException {
        JSONObject packData = new JSONObject();
        packData.put("first_ref", first_param);
        packData.put("first_flyer", second_param);
        packData.put("first", third_param);
        packData.put("point", fourth_param);
        setData(packData.toString());
    }

    public boolean isFirstRef() {
        return firstRef;
    }

    public void setFirstRef(boolean firstRef) throws JSONException {
        this.firstRef = firstRef;
        packData(firstRef, firstFlyer, first, point);
    }

    public boolean isFirstFlyer() {
        return firstFlyer;
    }

    public void setFirstFlyer(boolean firstFlyer) throws JSONException {
        this.firstFlyer = firstFlyer;
        packData(firstRef, firstFlyer, first, point);
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) throws JSONException {
        this.first = first;
        packData(firstRef, firstFlyer, first, point);
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) throws JSONException {
        this.point = point;
        packData(firstRef, firstFlyer, first, point);
    }
}
