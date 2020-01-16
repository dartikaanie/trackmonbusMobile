package com.anie.dara.trackmonbus_supir.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Broacast Run", "run");
        if (intent != null) {
            // pengecekan dilakukan agar notifikasi tidak muncul berulang
//            if (getTimeNow() == intent.getStringExtra("validationTime")) {
                Log.e("Broacast Run", "run");
//            }

        }
    }

    private String getTimeNow() {
         long dateTimeMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTimeMillis);

        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }
}
