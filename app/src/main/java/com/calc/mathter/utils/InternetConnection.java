package com.calc.mathter.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection extends Thread{

    Context context;
    SaveStates st;

    public InternetConnection(Context context){
        this.context = context;
        st = new SaveStates(context);
    }

    @Override
    public void run() {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
            st.setInternet(true);
        } else {
            st.setInternet(false);
        }
    }
}
