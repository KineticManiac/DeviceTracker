package com.example.devicetracker;

import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

public final class WalkDetector {
    private static final long DELAY = 10000;
    private static final long MS2NS = 1000000;

    private final StepDetector stepDetector;
    private final StepListener stepListener;

    private Timer timer;
    private boolean isWalking;
    private OnValueChangedListener onValueChangedListener;

    public WalkDetector(SensorManager sensorManager){
        stepDetector = new StepDetector(sensorManager);
        stepListener = new StepListener();
    }

    public void pause(){
        stepDetector.pause();
        stepDetector.setOnStepListener(null);
        timer.cancel();
    }

    public void resume(){
        timer = new Timer();
        stepDetector.setOnStepListener(stepListener);
        stepDetector.resume();
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    private void setWalking(boolean walking, long timestamp) {
        isWalking = walking;
        if(onValueChangedListener != null)
            onValueChangedListener.onValueChanged(walking, timestamp);
    }

    private class StepListener implements StepDetector.OnStepListener{

        private StepTimerTask task;

        @Override
        public void onStep(long timestamp) {
            if(isWalking){
                task.cancel();
            }
            else {
                setWalking(true, timestamp);
            }

            task = new StepTimerTask(timestamp + DELAY * MS2NS);
            timer.schedule(task, DELAY);
        }
    }

    private class StepTimerTask extends TimerTask{

        private final long timestamp;

        public StepTimerTask(long timestamp){
            this.timestamp = timestamp;
        }

        @Override
        public void run() {
            setWalking(false, timestamp);
        }
    }

    public interface OnValueChangedListener{
        void onValueChanged(boolean isWalking, long timestamp);
    }
}
