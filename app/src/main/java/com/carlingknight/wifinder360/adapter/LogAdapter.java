package com.carlingknight.wifinder360.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carlingknight.wifinder360.R;
import com.carlingknight.wifinder360.models.LogEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Carling Knight on 03/04/2017.
 */

public class LogAdapter extends ArrayAdapter<LogEntry> {

    List<LogEntry> entries;

    public LogAdapter(Context context, List<LogEntry> entries){
        super(context, 0, entries);

        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LogEntry entry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_device_logger, parent, false);
        }

        TextView textTimestamp = (TextView) convertView.findViewById(R.id.log_timestamp);
        TextView textMessage = (TextView) convertView.findViewById(R.id.log_message);
        TextView textSeverity = (TextView) convertView.findViewById(R.id.log_severity);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(entry.timestamp);
        String formattedDate = new String();
        try {
            formattedDate = format.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        textTimestamp.setText(formattedDate);
        textMessage.setText(entry.message);
        textSeverity.setText(entry.severity.toString());

        switch (entry.severity){
            case 1:
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.device_logger_1));
                break;

            case 2:
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.device_logger_2));
                break;

            case 3:
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.device_logger_3));
                break;

            case 4:
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.device_logger_4));
                break;

            case 5:
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.device_logger_5));
                break;
        }

        return convertView;

    }
}
