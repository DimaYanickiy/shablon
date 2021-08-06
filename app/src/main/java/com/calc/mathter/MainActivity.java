package com.calc.mathter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calc.mathter.utils.Main;
import com.calc.mathter.utils.Save;
import com.calc.mathter.utils.SaveStates;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity{

    Save save;
    SaveStates st;
    Main mainThread;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        st = new SaveStates(this);
        img = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.gif).into(img);
        st.setGo(0);
        try {
            save = new Save(this);
        } catch (JSONException e) {}
        mainThread = new Main(this);
        mainThread.start();
        while(mainThread.isAlive()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
            }
        }

        timer();
    }

    public void nextStep(int parametr) throws JSONException {
        switch (parametr){
            case 1:
                startActivity(new Intent(MainActivity.this, DesignActivity.class));
                finish();
                break;
            case 2:
                save.setPoint("");
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                finish();
                break;
        }
    }

    public void timer(){
        Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if(st.getGo() == 1 || st.getGo() == 2){
                    try {
                        nextStep(st.getGo());
                    } catch (JSONException e) {}
                } else{
                    h.postDelayed(this, 2000);
                }
            }
        });
    }
}