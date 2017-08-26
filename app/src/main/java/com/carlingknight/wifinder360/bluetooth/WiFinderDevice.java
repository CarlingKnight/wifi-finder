package com.carlingknight.wifinder360.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.carlingknight.wifinder360.component.Utils;
import com.carlingknight.wifinder360.models.DeviceLogger;
import com.carlingknight.wifinder360.models.LogEntry;
import com.carlingknight.wifinder360.models.WiFiDetail;
import com.carlingknight.wifinder360.util.RandomGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by Carling Knight on 30/03/2017.
 */

public class WiFinderDevice {

    private OutputStream outputStream;
    private InputStream inputStream;

    private static final UUID MY_UUID = UUID.fromString("1e0ca4ea-299d-4335-93eb-27fcfe7fa848");

    private String dataBlock;

    public WiFinderDevice(final BluetoothDevice device, final Context context) {
        if (device != null) {
            Log.v("DeviceManager", "Beginning attempts to communicate to: " + device.getName());
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                //Throws exception here:
                socket.connect();

                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

                Thread connector = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Realm.init(context);
                            final Realm realm = Realm.getDefaultInstance();

                            final ObjectMapper mapper = new ObjectMapper();

                            while (Utils.appIsInForeground) {
                                if (outputStream == null) {
                                    Log.e("DeviceManager", "Looks like the outputStream is null...");
                                    if (inputStream == null) {
                                        Log.e("DeviceManager", "And input stream is null, are you even connected to the device?");
                                    } else {
                                        Log.e("DeviceManager", "Although strangely, input stream has been set.");
                                    }
                                } else {
                                    JSONObject dataToSend = new JSONObject();
                                    try {
                                        dataToSend.put("transmission_code", RandomGenerator.String(10));
                                        if (dataBlock == null) {
                                            dataToSend.put("data", "");
                                        } else {
                                            dataToSend.put("data", dataBlock);
                                            dataBlock = null;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("DeviceManager", "The JSON broke?");
                                    }
                                    byte[] myByteArray = dataToSend.toString().getBytes("UTF-8");
                                    outputStream.write(myByteArray, 0, myByteArray.length);

                                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                                    if (inputStream.available() > 0){
                                        while (inputStream.available() > 0){
                                            byteBuffer.write(inputStream.read());
                                        }
                                    }
                                    String jsonString = byteBuffer.toString();
                                    if (jsonString.length() > 0){
                                        DeviceLogger logger = mapper.readValue(jsonString, DeviceLogger.class);
//                                        Log.v("Extended Test", "Received details on " + logger.wiFiDetails.size() + " access points");
                                        realm.beginTransaction();
                                        Integer numberOfPointsAdded = 0;
                                        for (WiFiDetail wiFiDetail : logger.wiFiDetails){
                                            Number itemId = realm.where(WiFiDetail.class).max("id");
                                            if (itemId == null){
                                                wiFiDetail.setId(0);
                                            }else{
                                                wiFiDetail.setId(itemId.intValue() + 1);
                                            }
                                            numberOfPointsAdded++;
                                            realm.copyToRealmOrUpdate(wiFiDetail);
                                        }



                                        for (LogEntry entry : logger.entries){
                                            Number itemId = realm.where(LogEntry.class).max("id");
                                            if (itemId == null){
                                                entry.setId(0);
                                            }else{
                                                entry.setId(itemId.intValue() + 1);
                                            }
                                            realm.copyToRealmOrUpdate(entry);
                                        }

                                        LogEntry entry = new LogEntry("Added " + numberOfPointsAdded + " WiFiDetail point(s) to the database.", 2);
                                        Number itemId = realm.where(LogEntry.class).max("id");
                                        if (itemId == null){
                                            entry.setId(0);
                                        }else{
                                            entry.setId(itemId.intValue() + 1);
                                        }
                                        realm.copyToRealmOrUpdate(entry);

                                        realm.commitTransaction();

                                    }

                                }
                                Thread.sleep(1000);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("DeviceManager", "Can't create a socket to the bluetooth device.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("DeviceManager", "The sleep thread was interrupted.");
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Log.e("DeviceManager", "A null pointer exception, this is probably not good.");
                        }
                    }
                });

                connector.start();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("DeviceManager", "Can't create a socket to the bluetooth device.");
            }
        } else {
            Log.e("DeviceManager", "The Device Manager has been passed a null object instead of a device.");
        }
    }

    public void setDataBlock(String string){
        this.dataBlock = string;
    }
}
