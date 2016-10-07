package com.nonscirenefas.yeshy.surveyapp1;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class onBootService extends IntentService
{
    public onBootService() {
        super("onBootService");
    }

    public static final String MED_FILENAME = "med_file";
    public static final String FREQ_FILENAME = "freq_file";
    public static final String PREFS_UID = "MyPrefsFile";
    public static final String LSURVEY_FILENAME = "lsurvey_file";
    ArrayList<String> medicationList = new ArrayList<>();
    ArrayList<String> freqList = new ArrayList<>();
    Context ctx;
    String USER_FILENAME = "user_file";
    String printedMeds;
    String tenAmMeds="";
    String twoPmMeds="";
    String eightPmMeds="";
    String lsurveydate="";

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */


    //@Override
    protected void onHandleIntent(Intent intent) {
        try {
            FileInputStream fin = openFileInput(MED_FILENAME);
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //Log.e("Login Attempt", temp);
            if (temp.length()>1){
                Log.e("temp",temp);
                while(temp.indexOf("\n")!=-1){
                    medicationList.add(temp.substring(0,temp.indexOf("\n")));
                    temp = temp.substring(temp.indexOf("\n")+1,temp.length());
                }
                medicationList.add(temp.substring(0,temp.length()));
                Log.e("index",Integer.toString(temp.indexOf("\n")));
                Log.e("medList",medicationList.toString());
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream finfreq = openFileInput(FREQ_FILENAME);
            int c;
            String temp2="";
            while( (c = finfreq.read()) != -1){
                temp2 = temp2 + Character.toString((char)c);
            }
            //Log.e("Login Attempt", temp);
            if (temp2.length()>1){
                Log.e("temp2",temp2);
                while(temp2.indexOf("\n")!=-1){
                    freqList.add(temp2.substring(0,temp2.indexOf("\n")));
                    temp2 = temp2.substring(temp2.indexOf("\n")+1,temp2.length());
                }
                freqList.add(temp2.substring(0,temp2.length()));
                Log.e("index",Integer.toString(temp2.indexOf("\n")));
                Log.e("freqList",freqList.toString());
            }
            finfreq.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream finLSurvey = openFileInput(LSURVEY_FILENAME);
            int c;
            lsurveydate="";
            while( (c = finLSurvey.read()) != -1){
                lsurveydate = lsurveydate + Character.toString((char)c);
            }
             Log.e("Lifestyle Date",lsurveydate);
            finLSurvey.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.e("Lifestyle Date2",lsurveydate);

        Log.e("medArrayFinal",medicationList.toString());

        Log.e("medFreqFinal",freqList.toString());

        //first notification at 10 AM next day
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        for (int i=0;i<freqList.size();i++){
            if(freqList.get(i).equals("Daily")){
                if(tenAmMeds=="") {
                    tenAmMeds = tenAmMeds.concat(medicationList.get(i));
                }
                else{
                    tenAmMeds = tenAmMeds.concat(", ").concat(medicationList.get(i));
                }
            }
            else if(freqList.get(i).equals("Twice daily")){
                if(tenAmMeds=="") {
                    tenAmMeds = tenAmMeds.concat(medicationList.get(i));
                }
                else{
                    tenAmMeds = tenAmMeds.concat(", ").concat(medicationList.get(i));
                }
                if(eightPmMeds=="") {
                    eightPmMeds = eightPmMeds.concat(medicationList.get(i));
                }
                else{
                    eightPmMeds = eightPmMeds.concat(", ").concat(medicationList.get(i));
                }
            }
            else{
                if(tenAmMeds=="") {
                    tenAmMeds = tenAmMeds.concat(medicationList.get(i));
                }
                else{
                    tenAmMeds = tenAmMeds.concat(", ").concat(medicationList.get(i));
                }
                if(eightPmMeds=="") {
                    eightPmMeds = eightPmMeds.concat(medicationList.get(i));
                }
                else{
                    eightPmMeds = eightPmMeds.concat(", ").concat(medicationList.get(i));
                }
                if(twoPmMeds=="") {
                    twoPmMeds = twoPmMeds.concat(medicationList.get(i));
                }
                else{
                    twoPmMeds = twoPmMeds.concat(", ").concat(medicationList.get(i));
                }
            }
        }
        //**************************** START OF potential 3 a day Alarms
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);

        Intent alarmIntent = new Intent(this, StartMyServiceAtBootReceiver.class);
        alarmIntent.putExtra("type", 1);
        final int _id = (int) cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        if(twoPmMeds!="") {
            //second notification at 2 PM next day
            cal.set(Calendar.HOUR_OF_DAY, 14); //18:32
            cal.set(Calendar.MINUTE, 0);
            alarmIntent = new Intent(this, MyAlarmReceiver.class);
            alarmIntent.putExtra("type", 2);
            final int _id1 = (int) cal.getTimeInMillis();
            pendingIntent = PendingIntent.getBroadcast(this, _id1, alarmIntent, 0);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        if (eightPmMeds!="") {
            //third notification at 8 PM next day
            cal.set(Calendar.HOUR_OF_DAY, 20); //18:32
            cal.set(Calendar.MINUTE, 0);
            alarmIntent = new Intent(this, MyAlarmReceiver.class);
            alarmIntent.putExtra("type", 3);
            final int _id2 = (int) cal.getTimeInMillis();
            pendingIntent = PendingIntent.getBroadcast(this, _id2, alarmIntent, 0);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }

        int typeOfNotification =  intent.getIntExtra("type", 0);

        SharedPreferences settings = getSharedPreferences(PREFS_UID, 0);
        //there should be a second settings for the medications list
        String UIDstored = settings.getString("UID", "Default");
        //Log. d("UID", UIDstored);
        if(typeOfNotification==1){
            printedMeds = tenAmMeds;
        }
        else if(typeOfNotification==2){
            printedMeds = twoPmMeds;
        }
        else if (typeOfNotification==3){
            printedMeds = eightPmMeds;
        }

        if (typeOfNotification==1|typeOfNotification==2|typeOfNotification==3) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.bpnmenotif)
                    .setContentTitle("BP-n-ME")
                    .setContentText("Please take your medication(s): " + printedMeds);

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
        //**********************End of 3 a day*****************************/

        //***********************Create Monthly**********************
        cal.set(Calendar.DAY_OF_YEAR, Integer.parseInt(lsurveydate) + 32);
        cal.set(Calendar.HOUR_OF_DAY, 11); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);

        Intent alarmIntentMonth = new Intent(this, StartMyServiceAtBootReceiver.class);
        alarmIntentMonth.putExtra("type", 0);
        final int _idmonth = (int) cal.getTimeInMillis();
        PendingIntent pendingIntentMonth = PendingIntent.getBroadcast(this, _idmonth, alarmIntentMonth, 0);
        AlarmManager alarmManagerMonth = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManagerMonth.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntentMonth);

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
        //*****************End of Monthly*************************


        stopService(intent);

    }
}