package com.carlingknight.wifinder360.models;

import android.content.Context;

import com.carlingknight.wifinder360.component.DatabaseHandler;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carlingk on 10/02/17.
 *
 * This describes the WiFi information we receive from both devices, in this class we describe
 *  how realm should store the object and the attributes belonging to it.
 *
 *  @author carlingk
 *  @version 1.0
 */

public class WiFiDetail extends RealmObject {

    @PrimaryKey
    private Integer id;

    private String ssid;
    private String bssid;
    private String source;
    private Integer signalStrength;
    private Integer bluetoothStrength = null;

    private Double accelerometer_x;
    private Double accelerometer_y;
    private Double accelerometer_z;

    /**
     * Required empty constructor
     */
    public WiFiDetail(){}

    public WiFiDetail(String ssid, String bssid, String source, Integer signalStrength) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.source = source;
        this.signalStrength = signalStrength;
    }


    public void setPhoneDetails(String ssid,
                                String bssid,
                                Integer signalStrength,
                                Integer bluetoothStrength,
                                Double accelerometer_x,
                                Double accelerometer_y,
                                Double accelerometer_z){

        this.ssid = ssid;
        this.bssid = bssid;
        this.signalStrength = signalStrength;
        this.bluetoothStrength = bluetoothStrength;
        this.accelerometer_x = accelerometer_x;
        this.accelerometer_y = accelerometer_y;
        this.accelerometer_z = accelerometer_z;
    }

    public void setPiDetails(String ssid,
                             String bssid,
                             Integer signalStrength){
        this.ssid = ssid;
        this.bssid = bssid;
        this.signalStrength = signalStrength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getBluetoothStrength() {
        return bluetoothStrength;
    }

    public void setBluetoothStrength(Integer bluetoothStrength) {
        this.bluetoothStrength = bluetoothStrength;
    }

    public Double getAccelerometer_x() {
        return accelerometer_x;
    }

    public void setAccelerometer_x(Double accelerometer_x) {
        this.accelerometer_x = accelerometer_x;
    }

    public Double getAccelerometer_y() {
        return accelerometer_y;
    }

    public void setAccelerometer_y(Double accelerometer_y) {
        this.accelerometer_y = accelerometer_y;
    }

    public Double getAccelerometer_z() {
        return accelerometer_z;
    }

    public void setAccelerometer_z(Double accelerometer_z) {
        this.accelerometer_z = accelerometer_z;
    }
}
