package com.example.devicetracker;

import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public final class MainActivity extends AppCompatActivity {

    LightSensor lightSensor;
    TextView luxValue;

    WalkDetector walkDetector;
    TextView walkingValue;

    PlayPauseBroadcaster broadcaster;
    private boolean isWalking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String permission = Manifest.permission.ACTIVITY_RECOGNITION; //işe yaramadı
        String permission = "android.permission.ACTIVITY_RECOGNITION";

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        walkDetector = new WalkDetector(sensorManager);
        walkDetector.setOnValueChangedListener((isWalking, timestamp) -> onWalkValueChanged(isWalking));

        walkingValue = findViewById(R.id.walkingValue);

        if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
            ActivityResultLauncher<String> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                        if (!isGranted) {
                            walkingValue.setText("Adım dedektörüne erişilemedi.");
                        }
                    });
            requestPermissionLauncher.launch(permission);
        }

        lightSensor = new LightSensor(sensorManager);
        lightSensor.setOnValueChangedListener((value, timestamp) -> onLightValueChanged(value));

        luxValue = findViewById(R.id.luxValue);

        broadcaster = new PlayPauseBroadcaster(this);
    }

    private void onWalkValueChanged(boolean isWalking) {
        this.isWalking = isWalking;
        walkingValue.setText(Boolean.toString(isWalking));
        if(isWalking)
            broadcaster.play();
    }

    private void onLightValueChanged(Float value) {
        luxValue.setText(value.toString());
        if(!isWalking){
            broadcaster.sendBroadcast(value < 40.0);
        }
    }

    @Override
    protected void onPause() {
        lightSensor.pause();
        walkDetector.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lightSensor.resume();
        walkDetector.resume();
    }
}