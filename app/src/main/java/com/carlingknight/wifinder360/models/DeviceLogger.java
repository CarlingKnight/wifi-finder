package com.carlingknight.wifinder360.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Carling Knight on 01/04/2017.
 */

public class DeviceLogger extends RealmObject{
    @PrimaryKey
    public Integer id;


    @JsonProperty("entries")
    public RealmList<LogEntry> entries;

    @JsonProperty("wifiinfo")
    public RealmList<WiFiDetail> wiFiDetails;

    @JsonProperty("logger_name")
    public String loggerName;

    /**
     * Required empty constructor
     */
    public DeviceLogger(){

    }


}
