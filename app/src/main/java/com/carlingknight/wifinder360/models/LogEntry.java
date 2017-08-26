package com.carlingknight.wifinder360.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Carling Knight on 01/04/2017.
 */

public class LogEntry extends RealmObject {
    @PrimaryKey
    Integer id;

    @JsonProperty("message")
    public String message;

    @JsonProperty("severity")
    public Integer severity;

    public long timestamp = System.currentTimeMillis();

    /**
     * Required empty constructor
     */
    public LogEntry(){

    }

    public LogEntry(String message, Integer severity){
        this.message = message;
        this.severity = severity;
    }

    public void setId(Integer id){
        this.id = id;
    }
}
