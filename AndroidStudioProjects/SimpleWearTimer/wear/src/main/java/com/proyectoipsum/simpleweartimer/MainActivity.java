package com.proyectoipsum.simpleweartimer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends WearableActivity{

    private ViewGroup rootView;
    private PowerManager.WakeLock wakeLock;
    private ArrayList<TimerView> timers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAmbientEnabled();

        ViewGroup tutorial = (ViewGroup) findViewById(R.id.tutorial);
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


        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "awake");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.contains("times")){

            Gson gson = new Gson();
            String json = sharedPref.getString("times", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> timeWhenLeaving = gson.fromJson(json, type) ;

            if(timeWhenLeaving!=null && timeWhenLeaving.size()>0){

                for(int i=0; i<timeWhenLeaving.size(); i++) {

                    if (timeWhenLeaving.get(i).equals("orange")){

                        Timer timer = new StopWatch(this);
                        TimerView timerView = new TimerViewOrange(this, timer, (LinearLayout)findViewById(R.id.contenedor));
                        timers.add(timerView);
                    }
                    else if (timeWhenLeaving.get(i).equals("blue")){

                        Timer timer = new Countdown(this, timeWhenLeaving.get(i-1));
                        TimerView timerView = new TimerViewBlue(this, timer, (LinearLayout)findViewById(R.id.contenedor));
                        timers.add(timerView);
                    }
                }




                /*for(String times:timeWhenLeaving){

                    if (times.equals("00:00")){

                        Timer timer = new StopWatch(this);
                        TimerView timerView = new TimerView(this, timer, (LinearLayout)findViewById(R.id.contenedor));
                        timers.add(timerView);
                    }
                    else{

                        Timer timer = new Countdown(this, times);
                        TimerView timerView = new TimerView(this, timer, (LinearLayout)findViewById(R.id.contenedor));
                        timers.add(timerView);
                    }
                }*/
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

        /*if (findViewById(R.id.box) != null){
            View insetBox = findViewById(R.id.box);
            setCreateTimerListener(insetBox);
        }*/
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails){

        super.onEnterAmbient(ambientDetails);

        if(timersAreRunning()!=null){

            wakeLock.acquire();
        }

        RelativeLayout main = (RelativeLayout)findViewById(R.id.main);
        main.setBackgroundColor(Color.BLACK);
        RelativeLayout tutorial = (RelativeLayout)findViewById(R.id.tutorial);
        tutorial.setBackgroundColor(Color.BLACK);
        /*if(findViewById(R.id.box)!=null){
            View box = findViewById(R.id.box);
            box.setBackgroundColor(Color.BLACK);
        }*/
        if(timers.size()!=0){

            for(TimerView timerView: timers){
                LinearLayout container = timerView.getTimerContainer();
                LinearLayout border = (LinearLayout) container.findViewById(R.id.hijo);
                border.setBackgroundColor(Color.WHITE);
                for (int i=0;i<border.getChildCount();i++){
                    border.getChildAt(i).setBackgroundColor(Color.BLACK);
                    if(border.getChildAt(i) instanceof TextView){
                        ((TextView) border.getChildAt(i)).getPaint().setAntiAlias(false);
                    }
                }
            }
        }
    }

    @Override
    public void onExitAmbient(){

        super.onExitAmbient();

        if(wakeLock.isHeld()){

            wakeLock.release();
        }

        RelativeLayout main = (RelativeLayout) findViewById(R.id.main);
        main.setBackgroundColor(Color.parseColor("#303F9F"));
        RelativeLayout tutorial = (RelativeLayout)findViewById(R.id.tutorial);
        tutorial.setBackgroundColor(Color.parseColor("#303F9F"));
        /*if(findViewById(R.id.box)!=null){
            View box = findViewById(R.id.box);
            box.setBackgroundColor(Color.parseColor("#303F9F"));
        }*/
        if(timers.size()!=0){

            for(TimerView timerView: timers){
                LinearLayout container = timerView.getTimerContainer();
                LinearLayout border = (LinearLayout) container.findViewById(R.id.hijo);
                border.setBackgroundColor(Color.BLACK);
                for (int i=0;i<border.getChildCount();i++){
                    if(container.getTag().toString().equals("blue"))
                        border.getChildAt(i).setBackgroundColor(Color.parseColor("#132584"));
                    else if(container.getTag().toString().equals("orange"))
                        border.getChildAt(i).setBackgroundColor(Color.parseColor("#C07705"));
                    if(border.getChildAt(i) instanceof TextView){
                        ((TextView) border.getChildAt(i)).getPaint().setAntiAlias(true);
                    }
                }
            }
        }
    }

    public void setTimerFragment(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //SetTimerCountdown fragment = new SetTimerCountdown();
        SetNewTimer fragment = new SetNewTimer();
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
        if (wakeLock.isHeld()){
            wakeLock.release();
        }
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
            timeWhenLeaving.add(timerView.getTimerContainer().getTag().toString());
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

