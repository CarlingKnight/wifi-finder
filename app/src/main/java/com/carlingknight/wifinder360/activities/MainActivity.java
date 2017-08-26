package com.carlingknight.wifinder360.activities;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.carlingknight.wifinder360.map.MapObject;
import com.carlingknight.wifinder360.adapter.LogAdapter;
import com.carlingknight.wifinder360.bluetooth.DeviceConnector;
import com.carlingknight.wifinder360.bluetooth.WiFinderDevice;
import com.carlingknight.wifinder360.canvas.LocalMap;
import com.carlingknight.wifinder360.component.Utils;
import com.carlingknight.wifinder360.R;
import com.carlingknight.wifinder360.models.DeviceLogger;
import com.carlingknight.wifinder360.models.LogEntry;
import com.carlingknight.wifinder360.service.WiFiScanner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    WifiManager wifiManager;
    List<android.net.wifi.ScanResult> results;
    Integer size = 0;

    DeviceLogger logger;
    Long start_time = System.currentTimeMillis();

    List<LogEntry> logEntries;
    RealmResults<LogEntry> entries;

    public static int REQUEST_ENABLE_BT = 56;

    WiFinderDevice wifinder;

    @BindView(R.id.wifi_scan_button) Button wifi_scan_button;
    @BindView(R.id.testeditor) EditText testInput;
    @BindView(R.id.main_activity_toolbar) Toolbar main_activity_toolbar;
    @BindView(R.id.device_logger_view) ListView device_logger_view;
    @BindView(R.id.local_user_map)LocalMap local_user_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        main_activity_toolbar.setTitle("WiFinder 360");
        setSupportActionBar(main_activity_toolbar);

        Realm.init(getBaseContext());
//        RealmConfiguration config = new RealmConfiguration
//                .Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm realm = Realm.getInstance(config);

        Realm realm = Realm.getDefaultInstance();

        entries = realm.where(LogEntry.class).greaterThan("timestamp", System.currentTimeMillis()).findAll();

        updateLoggerAdapter();

        entries.addChangeListener(new RealmChangeListener<RealmResults<LogEntry>>() {
            @Override
            public void onChange(RealmResults<LogEntry> results) {
                entries = results;
                updateLoggerAdapter();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wifi_scan_button:
                wifinder.setDataBlock(testInput.getText().toString());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.appIsInForeground = true;

        Intent scannerService = new Intent(this, WiFiScanner.class);
        startService(scannerService);

        int number = 5;


        wifinder = new WiFinderDevice(new DeviceConnector("WiFinder360").getWiFinderDevice(), getBaseContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.appIsInForeground = false;
    }

    private void updateLoggerAdapter(){
        LogAdapter adapter = new LogAdapter(this, entries);
        device_logger_view.setAdapter(adapter);
        device_logger_view.setSelection(device_logger_view.getCount() - 1);
    }

}
