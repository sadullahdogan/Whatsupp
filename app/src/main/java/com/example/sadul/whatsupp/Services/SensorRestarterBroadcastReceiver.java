package com.example.sadul.whatsupp.Services;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CheckMessages myService=new CheckMessages(context);
            context.startService(new Intent(context, myService.getClass()));


    }

}