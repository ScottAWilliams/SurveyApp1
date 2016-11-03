package com.nonscirenefas.yeshy.surveyapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Intent intent2 = new Intent(context,ReminderService.class);
            int type = intent2.getIntExtra("type", 0);
            intent2.putExtra("type", type);
            context.startService(intent2);


            Intent intent1 = new Intent(context,MonthlyReminderService.class);
            int received = intent1.getIntExtra("recieved", 0);
            intent1.putExtra("received", received);
            context.startService(intent1);
        }
    }
}
