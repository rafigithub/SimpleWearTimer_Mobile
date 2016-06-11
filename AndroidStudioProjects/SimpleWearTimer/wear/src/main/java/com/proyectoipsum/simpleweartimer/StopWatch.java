package com.proyectoipsum.simpleweartimer;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;


/**
 * Created by Rafi on 29/05/2016.
 */
public class StopWatch implements Timer {

    private Chronometer chronometer;
    private long lastPause=-1;
    private String startingTime="00:00";

    public StopWatch (){

    }

    public void setUpTimer(final TextView timerDisplay){

        chronometer = (Chronometer) timerDisplay;
    }

    public void playTimer(){

        if (lastPause==-1){

            chronometer.setBase(SystemClock.elapsedRealtime());
        }
        else{
            chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
        }
        chronometer.start();

    }

    public void pauseTimer(){

        chronometer.stop();
        lastPause = SystemClock.elapsedRealtime();
    }

    public void resetTimer(){

        if (chronometer!=null){

            chronometer.stop();
            lastPause = -1;
        }
    }

    public void eraseTimer(){

        if (chronometer!=null){

            chronometer.stop();
            chronometer = null;
        }

    }

    public String getStartingTime(){

        return startingTime;
    }
}
