package com.calc.mathter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class PlugIn{

    Context context;
    boolean charging;
    int batLevel;
    SaveStates st;

    public PlugIn(Context context){
        this.context = context;
        st = new SaveStates(context);
        isPhonePluggedIn();
        checkBatteryLevel();
    }

    public void isPhonePluggedIn() {
        charging = false;
        final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        charging = batteryCharge || usbCharge || acCharge;
        st.setCharging(charging);
    }

    public void checkBatteryLevel(){
        BatteryManager bm = (BatteryManager)context.getSystemService(context.BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        st.setBattary(batLevel);
    }
}
