package com.calc.mathter.utils;

import android.content.Context;

import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class PointBuilder{

    Context context;
    SaveStates st;
    String str;

    public PointBuilder(Context context, String destinationPoint, String campaign, String appsflyerUID) throws InterruptedException {
        this.context = context;
        st = new SaveStates(context);

        str = destinationPoint
                + "?nmg=" + campaign
                + "&dv_id=" + appsflyerUID
                + "&avr=" + st.getAdId();
        st.setPoint(str);
    }
}
