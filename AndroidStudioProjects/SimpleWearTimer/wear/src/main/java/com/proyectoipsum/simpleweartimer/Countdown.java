package com.proyectoipsum.simpleweartimer;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

/**
 * Created by Rafi on 26/04/2016.
 */
public class Countdown implements Timer {

    private MainActivity mainActivity;
    private CountDownTimer testTimer;
    private long millisTillFinish = -1;
    private String startingTime;


    public Countdown(final Context context, String startingTime){

        mainActivity = (MainActivity) context;
        this.startingTime = startingTime;

    }


    public void setUpTimer(final TextView timerDisplay){

        final String timeString = timerDisplay.getText().toString();
        long millisRemaining = MilliConversions.stringToMilli(timeString);
        createCountdownTimer(millisRemaining, 400, timerDisplay);

    }

    public void playTimer(){

        testTimer.start();
    }

    public void resetTimer(){

        if(testTimer!=null){

            testTimer.cancel();
        }
    }

    public void pauseTimer(){

        if(testTimer!=null){

            testTimer.cancel();
        }
    }

    public void eraseTimer(){

        if(testTimer!=null){

            testTimer.cancel();
        }
    }

    public String getStartingTime(){

        return startingTime;
    }


    private void createCountdownTimer(long time, long tick, final TextView timerDisplay){

        testTimer  = new CountDownTimer(time, tick) {
            @Override
            public void onTick(long millisUntilFinished) {

                millisTillFinish = millisUntilFinished;
                String timeRemaining = MilliConversions.milliToString(millisUntilFinished);
                timerDisplay.setText(timeRemaining);
            }

            @Override
            public void onFinish() {

                Vibrator v = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);

                Intent intent = new Intent(mainActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mainActivity.startActivity(intent);
            }
        };
    }
}