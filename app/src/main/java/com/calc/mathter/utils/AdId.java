package com.calc.mathter.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public class AdId extends Thread{

    Context context;
    String ad_id = "";
    SaveStates st;

    public AdId(Context context){
        this.context = context;
        st = new SaveStates(context);
    }

    @Override
    public void run() {
        try {
            AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            ad_id = adInfo != null ? adInfo.getId() : null;
            st.setAdId(ad_id);
        } catch (Exception ex) {}
    }
}
