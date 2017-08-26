package com.carlingknight.wifinder360.component;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlingknight.wifinder360.models.WiFiDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlingk on 10/02/17.
 *
 * Probably going to drop this class as realm is being used instead.
 *
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifinder.db";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Signals(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "source VARCHAR, ssid VARCHAR, bssid VARCHAR, strength INTEGER" +
                "btStrength INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public Boolean createTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Signals(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "source VARCHAR, ssid VARCHAR, bssid VARCHAR, strength INTEGER," +
                "btStrength INTEGER)");
        db.close();
        return true;
    }

    public Boolean emptyDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Signals;");
        db.close();
        return true;
    }

    public Boolean addSignalRecord(WiFiDetail wiFiDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertionData = new ContentValues();
        insertionData.put("source", wiFiDetail.getSource());
        insertionData.put("ssid", wiFiDetail.getSsid());
        insertionData.put("bssid", wiFiDetail.getBssid());
        insertionData.put("strength", wiFiDetail.getSignalStrength());
        insertionData.put("btStrength", wiFiDetail.getBluetoothStrength());
        Long result = db.insert("Signals", null, insertionData);
        db.close();
        if (result > 0){
            return true;
        }else{
            return false;
        }
    }

    public List<WiFiDetail> getWifiDetailsForSSID(String SSID){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            List<WiFiDetail> wifiDetails = new ArrayList<>();
            Cursor cursor = db.query("Signals", new String[]{"ssid", "bssid", "source", "strength"},
                    "ssid = ?", new String[]{SSID}, null, null, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                wifiDetails.add(i, new WiFiDetail(cursor.getString(0), cursor.getString(2),
                        cursor.getString(2), cursor.getInt(3)));
                cursor.moveToNext();
            }
            cursor.close();
            return wifiDetails;
        }finally {
            db.close();
        }
    }

}

