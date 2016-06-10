package com.proyectoipsum.simpleweartimer;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Rafi on 09/06/2016.
 */
public class SetNewTimer extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the dialog in front of the main activity
        View view = inflater.inflate(R.layout.set_new_timer,container, false);
        //Set a listener and handler for right and left swipes.
        ViewGroup newStopwatch = (ViewGroup) view.findViewById(R.id.createStopwatch);
        setCreateStopwatchListener(newStopwatch);
        for(int i = 0; i<newStopwatch.getChildCount(); i++){

            setCreateStopwatchListener(newStopwatch.getChildAt(i));
        }

        ViewGroup newTimer = (ViewGroup) view.findViewById(R.id.createTimer);
        setCreateTimerListener(newTimer);
        for(int i = 0; i<newStopwatch.getChildCount(); i++){

            setCreateTimerListener(newTimer.getChildAt(i));
        }

        return view;
    }


    private void setCreateTimerListener(View view){

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity(),view){

            @Override
            public void onSwipeLeft(){

                MainActivity mainActivity = (MainActivity)getActivity();
                FragmentManager fragmentManager = mainActivity.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SetTimerCountdown fragment = new SetTimerCountdown();
                fragmentTransaction.add(R.id.main, fragment);
                fragmentTransaction.commit();
                dismiss();
            }

            @Override
            public void onSwipeRight(){

                dismiss();
            }
        });
    }

    private void setCreateStopwatchListener(View view){

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity(),view){

            @Override
            public void onSwipeLeft(){

                MainActivity mainActivity = (MainActivity)getActivity();
                LinearLayout parent = (LinearLayout) mainActivity.findViewById(R.id.contenedor);
                Timer timer = new StopWatch(mainActivity);
                TimerView timerView = new TimerViewOrange(mainActivity, timer, parent);
                mainActivity.getTimerArray().add(timerView);
                if(mainActivity.getTimerArray().size()==1){
                    mainActivity.findViewById(R.id.tutorial).setVisibility(View.GONE);
                }
                dismiss();
            }

            @Override
            public void onSwipeRight(){

                dismiss();
            }

        });
    }



}
