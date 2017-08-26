package com.carlingknight.wifinder360.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.carlingknight.wifinder360.component.Utils;
import com.carlingknight.wifinder360.models.WiFiDetail;

import java.util.List;

import io.realm.Realm;

/**
 * Created by carlingk on 07/03/2017
 *
 * This service handles grabbing the data and dumping it into the local database for later analysis
 * This grabs both the nearby WiFi data and the accelerometer information
 * Using realm it dumps it into the database where another method analyses it, this will run
 *  the entire time that the main activity is in view, this is so we don't do incredibly battery
 *  intensive work if the user isn't actively using the app.
 *
 * @author carlingk
 * @version 1.0
 */

public class WiFiScanner extends IntentService implements SensorEventListener {

    private WifiManager wifiManager;
    private SensorManager sensorManager;
    private final Realm realm = Realm.getDefaultInstance();

    private Double accelerometer_x;
    private Double accelerometer_y;
    private Double accelerometer_z;

    private Double[] gravity;

    public WiFiScanner() {
        super("WiFiScanner");
    }

    /**
     * Created by carlingk
     *
     * This starts up our background service then initiates the Sensor and WiFi managers
     *
     * Then it looks repeatedly while the main activity is in view, requesting the scan results
     *  every second, this may need to be increased in the future
     *
     * @param intent The intent to launch this service.
     */

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        while (Utils.appIsInForeground) {
            wifiManager.getScanResults();
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                Log.v("Service/WiFiScanner", "What on earth? The service thread just got interrupted.");
            }
        }
    }

    /**
     * Created by carlingk
     *
     * This gets the WiFi scanner results and then creates a realm object from them and then
     *  commits them to the local database.
     */

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> mScanResults = wifiManager.getScanResults();
            for (ScanResult result : mScanResults) {
                realm.beginTransaction();
                WiFiDetail detail = new WiFiDetail(result.SSID, result.BSSID, "phone", result.level);
                WiFiDetail wiFiDetail = realm.createObject(detail.getClass());
                wiFiDetail.setPhoneDetails(result.SSID, result.BSSID, result.level, 0, accelerometer_x, accelerometer_y, accelerometer_z);
                realm.commitTransaction();
            }
        }
    };

    /**
     * Created by carlingk
     *
     * Both the receiver and the listener need to be killed otherwise another application could
     *  potentially hijack the connections.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mWifiScanReceiver);
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    /**
     * Created by Google
     * This is from the developer page on accessing sensor information
     * Essentially by default the accelerometers actually pick up the Earth's gravity,
     * the below calculations get rid of that, giving us zeroed out values if the phone is lying
     * flat or if the phone is moving we get an actual reading in meters
     *
     * @param sensorEvent The event that is being received
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final double alpha = 0.8;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

        // Remove the gravity contribution with the high-pass filter.
        accelerometer_x = sensorEvent.values[0] - gravity[0];
        accelerometer_y = sensorEvent.values[1] - gravity[1];
        accelerometer_z = sensorEvent.values[2] - gravity[2];
    }

    /**
     * Created by Google
     * Another overridden method for getting results from the sensors
     * This currently is just for logging, I don't actually use this but it could potentially be
     * useful for determining whether the information is useful, if say the accuracy is under
     * 70% then we forget it.
     *
     * @param sensor The sensor receiving the accuracy change
     * @param accuracy The actual accuracy value
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("Service/WiFiScanner", "Accelerometer accuracy changed: " + accuracy);
    }
}
