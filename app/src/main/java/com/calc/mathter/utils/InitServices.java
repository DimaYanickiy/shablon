package com.calc.mathter.utils;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;

public class InitServices extends Thread{

    Context context;
    SaveStates st;

    public InitServices(Context context){
        this.context = context;
        st = new SaveStates(context);
    }

    @Override
    public void run() {
        OneSignal.initWithContext(context);
        OneSignal.setAppId("2a837289-cddd-4bc8-bf5b-e11852d1b040");
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        st.setInitServices(true);
    }
}
