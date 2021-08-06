package com.calc.mathter.utils;

import android.content.Context;

import com.calc.mathter.MainActivity;

import org.json.JSONException;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Main extends Thread{

    Save save;
    Context context;
    InternetConnection internetConnection;
    AppsFlyerConnection apps;
    InitServices initServices;
    SaveStates st;
    public boolean running = false;
    AdId adId;

    public Main(Context context) {
        this.context = context;
        try {
            save = new Save(context);
        } catch (JSONException e) {
        }
        st = new SaveStates(context);
        adId = new AdId(context);
        adId.start();
    }

    @Override
    public void run() {
        internetConnection = new InternetConnection(context);
        initServices = new InitServices(context);
        internetConnection.start();
        initServices.start();
        while (internetConnection.isAlive() || initServices.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
        try {
            if (!save.isFirstRef()) {
                if (st.getPoint().isEmpty() || st.getPoint().equals("")) {
                    st.setGo(2);
                    running = true;
                } else {
                    st.setGo(1);
                    running = true;
                }
            } else {
                if (st.getInternet()) {
                    if (st.getInitServices()) {
                        apps = new AppsFlyerConnection(context);
                        apps.start();
                    } else{
                        st.setGo(2);
                    }
                    running = true;
                } else {
                    st.setGo(2);
                    running = true;
                }
            }
        } catch (Exception e) {
        }
    }
}
