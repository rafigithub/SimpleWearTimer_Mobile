package com.proyectoipsum.simpleweartimer;

import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Rafi on 29/05/2016.
 */
public class StopWatch implements Timer {

    private long startingTimeMillis;
    private MainActivity mainActivity;
    private TimerTask timerTask;
    private java.util.Timer stopWatchTimer;
    private String startingTime;

    public StopWatch (final Context context){

        this.mainActivity = (MainActivity) context;
    }

    public StopWatch(final Context context, String startingTime){

        this.mainActivity = (MainActivity) context;
        this.startingTimeMillis = System.currentTimeMillis() + MilliConversions.stringToMilli(startingTime);
        this.startingTime  = startingTime;
    }




    public void setUpTimer(final TextView timerDisplay){

        stopWatchTimer = new java.util.Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerDisplay.setText(stopWatch());
                    }
                });
            }
        };
        //stopWatchTimer.scheduleAtFixedRate(timerTask, 0, 999);
    }

    public void startTimer(){

        stopWatchTimer.scheduleAtFixedRate(timerTask, 0, 999);

    }

    public void cancelTimer(){

        if (stopWatchTimer!= null){

            timerTask.cancel();
            stopWatchTimer.cancel();
            stopWatchTimer.purge();
        }
    }

    public String getStartingTime(){
        return startingTime;
    }

    public String stopWatch(){

        long nowTime = System.currentTimeMillis() + MilliConversions.stringToMilli(startingTime);
        long elapsed = nowTime - startingTimeMillis;
        Date date = new Date(elapsed);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }

}
