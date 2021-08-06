package com.calc.mathter.utils;

import android.content.Context;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONException;

import java.util.Map;

public class AppsFlyerConnection extends Thread{

    Save save;
    SaveStates st;
    Context context;
    FirebaseConfigInit fci;

    public AppsFlyerConnection(Context context){
        this.context = context;
        st = new SaveStates(context);
        try {
            save = new Save(context);
        } catch (JSONException e) {
        }
    }

    @Override
    public void run() {
        AppsFlyerLib.getInstance().init("oeyivy2EFoUvhQnjR7TKuc", new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                if(save.isFirstFlyer()){
                    fci = new FirebaseConfigInit(context, conversionData);
                    fci.fire();
                    try {
                        save.setFirstFlyer(false);
                    } catch (JSONException e) { }
                }
            }

            @Override
            public void onConversionDataFail(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        }, context);
        AppsFlyerLib.getInstance().start(context);
        AppsFlyerLib.getInstance().enableFacebookDeferredApplinks(true);
        st.setAppsCon(true);
    }
}


