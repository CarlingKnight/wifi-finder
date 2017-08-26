package com.carlingknight.wifinder360.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Set;

/**
 * Created by carlingk on 09/03/17.
 *
 * Handles finding our device from the paired devices attached to the phone.
 *
 * @author carlingk
 * @version 1.0
 */

public class DeviceConnector {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice desiredDevice;

    /**
     * Singleton object, used to find the device and then call {@link #getWiFinderDevice()} to
     *  get the found device
     *
     * Grab a set of paired devices from the bluetooth adapter, then loop through, checking the name
     *  of the devices until we find the one we want.
     *
     * @param deviceName The name of the device to find in the list.
     */
    public DeviceConnector(String deviceName){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Log.e("DeviceConnector", "This device doesn't have a bluetooth adapter?");
        }

        if(!bluetoothAdapter.isEnabled()){
            Log.v("DeviceConnector", "BluetoothAdapter isn't enabled, enabling it now.");
            bluetoothAdapter.enable();
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                String foundDeviceName = device.getName();
                if(foundDeviceName.equals(deviceName)){
                    desiredDevice = device;
                }
            }
        }else{
            Log.e("DeviceConnector", "No paired devices have been found.");
        }
    }

    /**
     * Grab the device from the object.
     *
     * @return The bluetooth device we're looking for.
     */
    @Nullable
    public BluetoothDevice getWiFinderDevice(){
        return desiredDevice;
    }
}
