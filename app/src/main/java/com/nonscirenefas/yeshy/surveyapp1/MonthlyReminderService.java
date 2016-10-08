package com.nonscirenefas.yeshy.surveyapp1;

import android.app.IntentService;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthlyReminderService extends IntentService
{

    public static final String MED_FILENAME = "med_file";
    public static final String FREQ_FILENAME = "freq_file";
    public static final String PREFS_UID = "MyPrefsFile";
    ArrayList<String> medicationList = new ArrayList<>();
    ArrayList<String> freqList = new ArrayList<>();
    Context ctx;
    int dayOfYear;

    public MonthlyReminderService() {
        super("MonthlyReminderService");
    }

    /**
 +     * The IntentService calls this method from the default worker thread with
 +     * the intent that started the service. When this method returns, IntentService
 +     * stops the service, as appropriate.
 +     */


    //@Override
    protected void onHandleIntent(Intent intent) {

        //dayOfYear = intent.getIntExtra("dayOfYear",0);
        //Log.e("DayOfYear",Integer.toString(dayOfYear));
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 32);
        cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);

        Intent alarmIntent = new Intent(this, MyAlarmReceiverTwo.class);
        alarmIntent.putExtra("type", 0);
        final int _id = (int) cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


        int typeOfNotification =  intent.getIntExtra("type", 0);

        if (typeOfNotification==0) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.bp_logo_hd)
                    .setContentTitle("BP-n-ME")
                    .setContentText("Take Survey");

            Intent resultIntent = new Intent(this, LoginActivity.class);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
            stopService(intent);

    }
}