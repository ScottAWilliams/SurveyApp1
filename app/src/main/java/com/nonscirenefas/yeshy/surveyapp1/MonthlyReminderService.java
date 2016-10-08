package com.nonscirenefas.yeshy.surveyapp1;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MonthlyReminderService extends IntentService {
    public static final String PREFS_UID = "MyPrefsFile";
    public static final String LSURVEY_FILENAME = "lsurvey_file";
    public static final String MSURVEY_FILENAME = "msurvey_file";
    public static final String HSURVEY_FILENAME = "hsurvey_file";
    Context ctx;
    String surveytext = "";
    String lsurveydate = "";
    String hsurveydate = "";
    String msurveydate = "";

    public MonthlyReminderService() {
        super("MonthlyReminderService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */


    //@Override
    protected void onHandleIntent(Intent intent) {

        try {
            FileInputStream finLSurvey = openFileInput(LSURVEY_FILENAME);
            int c;
            lsurveydate = "";
            while ((c = finLSurvey.read()) != -1) {
                lsurveydate = lsurveydate + Character.toString((char) c);
            }
            Log.e("Lifestyle Date", lsurveydate);
            finLSurvey.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream finLSurvey = openFileInput(HSURVEY_FILENAME);
            int c;
            hsurveydate = "";
            while ((c = finLSurvey.read()) != -1) {
                hsurveydate = hsurveydate + Character.toString((char) c);
            }
            Log.e("Health Date", hsurveydate);
            finLSurvey.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream finLSurvey = openFileInput(MSURVEY_FILENAME);
            int c;
            msurveydate = "";
            while ((c = finLSurvey.read()) != -1) {
                msurveydate = msurveydate + Character.toString((char) c);
            }
            Log.e("Med Adherence Date", msurveydate);
            finLSurvey.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //dayOfYear = intent.getIntExtra("dayOfYear",0);
        //Log.e("DayOfYear",Integer.toString(dayOfYear));
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_YEAR, Integer.parseInt(msurveydate) + 32);
        cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, Integer.parseInt(hsurveydate) + 32);
        cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, Integer.parseInt(lsurveydate) + 32);
        cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);

        if (Integer.parseInt(msurveydate) == Integer.parseInt(lsurveydate) & Integer.parseInt(msurveydate) == Integer.parseInt(hsurveydate)) {

            Intent alarmIntent = new Intent(this, MyAlarmReceiverTwo.class);
            alarmIntent.putExtra("type", 0);
            final int _id = (int) cal.getTimeInMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

            surveytext = "Lifestyle, Medication, and Health Literacy";


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.bp_logo_hd)
                    .setContentTitle("BP-n-ME")
                    .setContentText("Please Take Survey(s): " + surveytext);

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
