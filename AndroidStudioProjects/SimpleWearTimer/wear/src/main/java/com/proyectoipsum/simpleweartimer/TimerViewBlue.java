package com.proyectoipsum.simpleweartimer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Rafi on 04/06/2016.
 */
public class TimerViewBlue extends TimerView {

    private LinearLayout timerContainer;

    public TimerViewBlue(final Context context, final Timer timer, ViewGroup parent){

        super(context, timer, parent);
        timerContainer = super.getTimerContainer();
        timerContainer.setTag("blue");
        timerContainer = (LinearLayout) timerContainer.getChildAt(0);
        for (int i=0; i<timerContainer.getChildCount(); i++){

            View child = timerContainer.getChildAt(i);
            child.setBackgroundColor(Color.parseColor("#132584"));
        }
    }

}
