package com.nonscirenefas.yeshy.surveyapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
/*
            Intent serviceIntent = new Intent(context, onBootService.class);

            java.util.Calendar cal = new GregorianCalendar();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour<10 | (20<=hour&hour<24)){
                serviceIntent.putExtra("type",1);
            }
            else if (hour<14){
                serviceIntent.putExtra("type",2);
            }
            else if (hour<20){
                serviceIntent.putExtra("type",3);
            }

            int type = serviceIntent.getIntExtra("type", 0);
            Log.e("Type",Integer.toString(type));
            serviceIntent.putExtra("type", type);
            context.startService(serviceIntent);
            */
            Intent serviceIntent = new Intent(context, MonthlyReminderService.class);
            context.startService(serviceIntent);
            Intent serviceIntent2 = new Intent(context, ReminderService.class);
            context.startService(serviceIntent2);
        }
    }
}
