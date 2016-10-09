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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.R.attr.y;

public class MonthlyReminderService extends IntentService
{
    public static final String LSURVEY_FILENAME = "lsurvey_file";
    public static final String MSURVEY_FILENAME = "msurvey_file";
    public static final String HSURVEY_FILENAME = "hsurvey_file";
    ArrayList<String> dateList = new ArrayList<String>();
    Context ctx;
    String date;
    String type;
    int dayOfYear;
    int minute = 0;
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

        try {
            FileInputStream hfin= openFileInput(MSURVEY_FILENAME);
            int c;
            String temp="";
            while( (c = hfin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            dateList.add(0,temp);
            hfin.close();
        } catch (FileNotFoundException e) {
            dateList.add(0,"");
            e.printStackTrace();
        } catch (IOException e) {
            dateList.add(0,"");
            e.printStackTrace();
        }
        try {
            FileInputStream hfin= openFileInput(HSURVEY_FILENAME);
            int c;
            String temp="";
            while( (c = hfin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            dateList.add(1,temp);
            hfin.close();
        } catch (FileNotFoundException e) {
            dateList.add(1,"");
            e.printStackTrace();
        } catch (IOException e) {
            dateList.add(1,"");
            e.printStackTrace();
        }
        try {
            FileInputStream hfin= openFileInput(LSURVEY_FILENAME);
            int c;
            String temp="";
            while( (c = hfin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            dateList.add(2,temp);
            hfin.close();
        } catch (FileNotFoundException e) {
            dateList.add(2,"");
            e.printStackTrace();
        } catch (IOException e) {
            dateList.add(2,"");
            e.printStackTrace();
        }



        for(int i=0;i<3;i++) {
            if(dateList.get(i).length()>1) {
                if (i==0){
                    type = "Medication Adherence";
                    minute = 0;
                }
                else if (i==1){
                    type = "Health Literacy";
                    minute = 1;
                }
                else if (i==2){
                    type = "Lifestyle";
                    minute = 2;
                }
                Log.e("Type",type);
                date = dateList.get(i);
                //date = intent.getStringExtra("date");
                //type = intent.getStringExtra("type");
                String y = date.substring(0, 4);
                String m = date.substring(5, 7);
                String d = date.substring(8, 10);//date.length()-1);
                Log.e("year", y);
                Log.e("month", m);
                Log.e("day", d);


                int year = Integer.parseInt(y);
                int month = Integer.parseInt(m);
                int day = Integer.parseInt(d);
                //dayOfYear = intent.getIntExtra("dayOfYear",0);
                //Log.e("DayOfYear",Integer.toString(dayOfYear));
                Calendar getDayOfYear = new GregorianCalendar();
                getDayOfYear.set(Calendar.YEAR, year);
                getDayOfYear.set(Calendar.MONTH, month-1);
                getDayOfYear.set(Calendar.DAY_OF_MONTH, day);

                if ((getDayOfYear.get(Calendar.DAY_OF_YEAR) + 30) > 365) {
                    dayOfYear = (getDayOfYear.get(Calendar.DAY_OF_YEAR) + 30) - 365;
                    year = year + 1;
                } else {
                    dayOfYear = (getDayOfYear.get(Calendar.DAY_OF_YEAR) + 30);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_YEAR, dayOfYear);
                cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
                cal.set(Calendar.MINUTE, 0);//minute);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);

                Intent alarmIntent = new Intent(this, MyAlarmReceiverTwo.class);
                final int _id = (int) cal.getTimeInMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bp_logo_hd)
                        .setContentTitle("BP-n-ME")
                        .setContentText("Take Survey: " + type);

                Intent resultIntent = new Intent(this, LoginActivity.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                int mNotificationId = minute+1;
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

        }
        stopService(intent);


    }
}