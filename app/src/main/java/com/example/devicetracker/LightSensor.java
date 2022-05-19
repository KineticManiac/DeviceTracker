package com.example.devicetracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public final class LightSensor{

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final LightSensorEventListener listener;

    private float value;
    private OnValueChangedListener onValueChangedListener;

    public LightSensor(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        listener = new LightSensorEventListener();
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener){
        this.onValueChangedListener = onValueChangedListener;
    }

    public void pause() {
        sensorManager.unregisterListener(listener);
    }

    public void resume() {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setValue(float value, long timestamp) {
        this.value = value;
        if(onValueChangedListener != null)
            onValueChangedListener.onValueChanged(value, timestamp);
    }

    private class LightSensorEventListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            setValue(sensorEvent.values[0], sensorEvent.timestamp);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }
    }

    public interface OnValueChangedListener{
        void onValueChanged(float newVal, long timestamp);
    }
}
