package com.calc.mathter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveStates {

    Context context;
    SharedPreferences sp;

    public SaveStates(Context context){
        this.context = context;
        sp = context.getSharedPreferences("PREFERENCES", context.MODE_PRIVATE);
    }

    public void setInternet(boolean internet){
        sp.edit().putBoolean("internet", internet).apply();
    }
    public void setDeveloper(boolean developer){
        sp.edit().putBoolean("developer", developer).apply();
    }
    public void setBattary(int batlevel){
        sp.edit().putInt("batlevel", batlevel).apply();
    }
    public void setCharging(boolean charging){
        sp.edit().putBoolean("charging", charging).apply();
    }
    public void setAdId(String adId){
        sp.edit().putString("adid", adId).apply();
    }
    public void setPoint(String point){
        sp.edit().putString("point", point).apply();
    }
    public void setInitServices(boolean init){
        sp.edit().putBoolean("init", init).apply();
    }
    public void setAppsCon(boolean init){
        sp.edit().putBoolean("apps", init).apply();
    }
    public void setGo(int go){
        sp.edit().putInt("go", go).apply();
    }


    public boolean getInternet(){
        return sp.getBoolean("internet",false);
    }
    public boolean getDeveloper(){
        return sp.getBoolean("developer",false);
    }
    public int getBattary(){
        return sp.getInt("batlevel",0);
    }
    public boolean getCharging(){
        return sp.getBoolean("charging",false);
    }
    public String getAdId(){
        return sp.getString("adid", "");
    }
    public String getPoint(){
        return sp.getString("point", "");
    }
    public boolean getInitServices(){
        return sp.getBoolean("init",false);
    }
    public boolean getAppsCon(){
        return sp.getBoolean("apps",false);
    }
    public int getGo(){
        return sp.getInt("go",2);
    }
}
