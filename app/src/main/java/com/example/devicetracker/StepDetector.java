package com.example.devicetracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public final class StepDetector {

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final StepDetectorEventListener listener;

    private OnStepListener onStepListener;

    public StepDetector(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        listener = new StepDetectorEventListener();
    }

    public void setOnStepListener(OnStepListener onStepListener) {
        this.onStepListener = onStepListener;
    }

    public void pause(){
        sensorManager.unregisterListener(listener);
    }

    public void resume(){
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private class StepDetectorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(onStepListener != null)
                onStepListener.onStep(sensorEvent.timestamp);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }
    }

    public interface OnStepListener {
        void onStep(long timestamp);
    }
}
