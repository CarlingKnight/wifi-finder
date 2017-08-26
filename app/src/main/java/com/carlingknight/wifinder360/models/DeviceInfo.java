package com.carlingknight.wifinder360.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Carling Knight on 30/03/2017.
 */

public class DeviceInfo extends RealmObject {

    @PrimaryKey
    public Integer id;

    public String timestamp;
    public String message;

    public Integer severity;

    /**
     * Required empty constructor
     */
    public DeviceInfo(){}

    public DeviceInfo(String timestamp,
                      String message,
                      Integer severity){
        this.timestamp = timestamp;
        this.message = message;
        this.severity = severity;
    }
}
