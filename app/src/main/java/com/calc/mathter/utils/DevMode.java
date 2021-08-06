package com.calc.mathter.utils;

import android.content.Context;

public class DevMode{

    Context context;
    boolean developer;
    SaveStates st;

    public DevMode(Context context){
        this.context = context;
        st = new SaveStates(context);
        devMode();
    }

    public void devMode(){
        developer = android.provider.Settings.Secure.getInt(context.getContentResolver(),
                android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) != 0;
        st.setDeveloper(developer);
    }
}
