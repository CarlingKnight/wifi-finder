package com.carlingknight.wifinder360.service;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.carlingknight.wifinder360.component.Utils;
import com.carlingknight.wifinder360.util.RandomGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by carlingk
 *
 * Handles sending data back and forth between the phone and the device as well as adding the
 *  reported device data into the local database
 *
 * @author carlingk
 * @version 1.0
 */

public class DeviceManager extends IntentService {

    BluetoothDevice device;

    private OutputStream outputStream;
    private InputStream inputStream;

    String dataBlock = "Test";

    public DeviceManager(){
        super("start");
    }

    private static final UUID MY_UUID = UUID.fromString("1e0ca4ea-299d-4335-93eb-27fcfe7fa848");

    public DeviceManager(BluetoothDevice device) {
        super("DeviceManager");
        this.device = device;

        if(device == null){
            Log.e("DeviceManager", "Device manager was given a null device. This will prevent" +
                    "any results being grabbed from the device.");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        device = (BluetoothDevice) intent.getExtras().get("device");

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
                            while (Utils.appIsInForeground && device != null) {
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
                                        if (dataBlock == null){
                                            dataToSend.put("data", "");
                                        }else{
                                            dataToSend.put("data", dataBlock);
                                            dataBlock = null;
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                        Log.e("DeviceManager", "The JSON broke?");
                                    }
                                    byte[] myByteArray = dataToSend.toString().getBytes("UTF-8");
                                    outputStream.write(myByteArray, 0, myByteArray.length);
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

            }catch (IOException e) {
                e.printStackTrace();
                Log.e("DeviceManager", "Can't create a socket to the bluetooth device.");
            }
        }else {
            Log.e("DeviceManager", "The Device Manager has been passed a null object instead of a device.");
        }
    }
}
