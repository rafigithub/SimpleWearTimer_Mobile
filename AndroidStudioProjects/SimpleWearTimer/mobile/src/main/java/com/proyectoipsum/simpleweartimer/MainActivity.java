package com.proyectoipsum.simpleweartimer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity {

    //private View rootView;
    private PowerManager.WakeLock wakeLock;
    private ArrayList<TimerView> timers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener(){

            @Override
            public void onLayoutInflated(WatchViewStub stub){

                rootView = stub.findViewById(R.id.main);
            }
        });
        if(rootView!=null)
            setContentView(rootView);*/

        RelativeLayout tutorial = (RelativeLayout) findViewById(R.id.tutorial);
        setCreateTimerListener(tutorial);
        /*tutorial.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this, tutorial){

            @Override
            public void onSwipeLeft(){
                Toast.makeText(getApplicationContext(),"tutorial advance",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight(){
                Toast.makeText(getApplicationContext(),"tutorial reverse",Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.contains("times")){

            Gson gson = new Gson();
            String json = sharedPref.getString("times", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> timeWhenLeaving = gson.fromJson(json, type) ;

            if(timeWhenLeaving!=null && timeWhenLeaving.size()>0){

                for(String times:timeWhenLeaving){

                    Timer timer = new Countdown(this, times);
                    TimerView timerView = new TimerView(this, timer, (LinearLayout)findViewById(R.id.contenedor));
                    timers.add(timerView);
                }
            }
            /*else{

                Toast.makeText(this,"saved time array is empty",Toast.LENGTH_SHORT).show();
            }*/
        }

        if(timers.size()>0){

            tutorial.setVisibility(View.GONE);
        }

        View main = findViewById(R.id.main);
        setCreateTimerListener(main);
        /*main.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            @Override
            public void onSwipeLeft() {

                setTimerFragment();
            }

            @Override
            public void onSwipeRight() {

                finish();
            }
        });*/

        View mainScroll = findViewById(R.id.mainscroll);
        setCreateTimerListener(mainScroll);
        /*mainScroll.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            @Override
            public void onSwipeLeft() {

                setTimerFragment();
            }

            @Override
            public void onSwipeRight() {

                finish();
            }
        });*/
    }



    public void setTimerFragment(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SetTimer fragment = new SetTimer();
        fragmentTransaction.add(R.id.main, fragment);
        fragmentTransaction.commit();
    }

    public ArrayList<TimerView> getTimerArray(){

        return timers;
    }

    private ArrayList<TimerView> timersAreRunning(){

        ArrayList<TimerView> runningTimers = new ArrayList<>();

        if (timers.size()>0){

            for(TimerView timerView: timers){

                ImageButton playButton = (ImageButton)timerView.getTimerContainer().findViewById(R.id.playButton);
                if(playButton.getTag().equals("pause")){
                    runningTimers.add(timerView);
                }
            }
        }

        if(runningTimers.size()>0){
            return runningTimers;
        }
        else {
            return null;
        }
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
        saveData();
        ArrayList<TimerView> runningTimers=timersAreRunning();
        if(runningTimers!=null){
            for(TimerView timerView: runningTimers){

                timerView.cancelTimer();
            }
        }
    }

    public void saveData(){

        ArrayList<String> timeWhenLeaving = new ArrayList<>();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        for(TimerView timerView: timers){

            timeWhenLeaving.add(timerView.getStartingTime());
        }
        String json = gson.toJson(timeWhenLeaving, type);
        editor.putString("times", json);
        editor.apply();
    }

    public void setCreateTimerListener(View view){

        view.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this, view){

            @Override
            public void onSwipeLeft(){

                setTimerFragment();
            }

            @Override
            public void onSwipeRight(){

                finish();
            }
        });
    }
}

