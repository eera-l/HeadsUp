package cloud.headsup.controller;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import cloud.headsup.model.JSONRequestHandler;

import java.util.Date;
import java.util.Random;


/**
 * Created by Federica on 23/02/2019.
 */

public class BackgroundIntent extends JobIntentService {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;



    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {

        final Random rnd = new Random();
        if (proximitySensor != null) {
            proximitySensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {

                    float randomReading = rnd.nextFloat() * 2;
                    float randomReadingBig = 7 + rnd.nextFloat() * 1.5f;
                    Log.d("Prox sensor", "Proximity distance: " + sensorEvent.values[0]);
                    JSONRequestHandler.sendJSONPostRequest(new Date(), sensorEvent.values[0] < 9 ? sensorEvent.values[0] + randomReading :
                    sensorEvent.values[0] - randomReadingBig);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

            sensorManager.registerListener(proximitySensorListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
