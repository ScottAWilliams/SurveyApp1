package com.nonscirenefas.yeshy.surveyapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Yeshy on 4/13/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra("type", 0);
        Log.d("Alarm Recieved!", "YAAAY");
        Intent i = new Intent(context, ReminderService.class);
        i.putExtra("type", type);
        context.startService(i);
    }
}