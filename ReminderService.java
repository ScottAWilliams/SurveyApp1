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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReminderService extends IntentService
{

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    public static final String MED_FILENAME = "med_file";
    public static final String FREQ_FILENAME = "freq_file";
    public static final String PREFS_UID = "MyPrefsFile";
    ArrayList<String> medicationList = new ArrayList<>();
    ArrayList<String> freqList = new ArrayList<>();
    Context ctx;
    String USER_FILENAME = "user_file";

    public ReminderService() {
        super("ReminderService");
    }

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
        //first notification at 10 AM next day
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);

        Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 1);
        final int _id = (int) cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //second notification at 12 PM next day
        cal.set(Calendar.HOUR_OF_DAY, 12); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 2);
        final int _id1 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id1, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //third notification at 2 PM next day
        cal.set(Calendar.HOUR_OF_DAY, 14); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 3);
        final int _id2 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id2, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        /*
        //third notification at 10 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) -1);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 1);
        final int _id3 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id3, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //second notification at 12 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 12); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 2);
        final int _id4 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id4, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //third notification at 2 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 14); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 3);
        final int _id5 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id5, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
*/

        int typeOfNotification =  intent.getIntExtra("type", 0);

        SharedPreferences settings = getSharedPreferences(PREFS_UID, 0);
        //there should be a second settings for the medications list
        String UIDstored = settings.getString("UID", "Default");
        //Log. d("UID", UIDstored);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentTitle("BP-n-ME")
                    .setContentText("Please take medication.");
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


        /*
        int typeOfNotification =  intent.getIntExtra("type", 0);

        SharedPreferences settings = getSharedPreferences(PREFS_UID, 0);
        //there should be a second settings for the medications list
        String UIDstored = settings.getString("UID", "Default");
        Log.d("UID", UIDstored);

        if(UIDstored.equals("Default")) {
            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
            cal.set(Calendar.MINUTE, 10);
            cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
            cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));


            Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
            alarmIntent.putExtra("type", 1);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentTitle("BP-n-ME")
                    .setContentText("Please log in to receive medication reminders!");
            Intent resultIntent = new Intent(this, MainActivity.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        } else {
            //if I've received something from the previous broadcast, maybe just continue that one.
            SharedPreferences settings1 = getSharedPreferences("Medication", 0);
            String oneDay = settings1.getString("onePerDay", "Default");
            String twoDay = settings1.getString("twoPerDay", "Default");
            String threeDay = settings1.getString("threePerDay", "Default");
            String fourDay = settings1.getString("fourPerDay", "Default");

            //first notification at 10
            Calendar cur_cal = new GregorianCalendar();
            cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal.set(Calendar.HOUR_OF_DAY, 2); //18:32
            cal.set(Calendar.MINUTE, 19);
            cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
            cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

            Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
            alarmIntent.putExtra("type", 1);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

            //second notification at 2
            Calendar cal1 = new GregorianCalendar();
            cal1.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal1.set(Calendar.HOUR_OF_DAY, 14); //18:32
            cal1.set(Calendar.MINUTE, 10);
            cal1.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
            cal1.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
            cal1.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal1.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

            Intent alarmIntent1 = new Intent(this, MyAlarmReceiver.class);
            alarmIntent1.putExtra("type", 2);
            PendingIntent pendingIntent1 = PendingIntent.getService(this, 0, alarmIntent1, 0);
            AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager1.set(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), pendingIntent1);

            //third notification at 8
            Calendar cal2 = new GregorianCalendar();
            cal2.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            cal2.set(Calendar.HOUR_OF_DAY, 20); //18:32
            cal2.set(Calendar.MINUTE, 10);
            cal2.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
            cal2.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
            cal2.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
            cal2.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

            Intent alarmIntent2 = new Intent(this, MyAlarmReceiver.class);
            alarmIntent2.putExtra("type", 3);
            PendingIntent pendingIntent2 = PendingIntent.getService(this, 0, alarmIntent2, 0);
            AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager2.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent2);

            String listOfMeds = "";
            if(typeOfNotification == 1) {
                if(!oneDay.equals("Default")) {
                    listOfMeds = listOfMeds + oneDay;
                }

                if(!twoDay.equals("Default")) {
                    listOfMeds = listOfMeds + twoDay;
                }

                if(!threeDay.equals("Default")) {
                    listOfMeds = listOfMeds + threeDay;
                }
            } else if(typeOfNotification == 2) {
                if(!twoDay.equals("Default")) {
                    listOfMeds = listOfMeds + twoDay;
                }

                if(!threeDay.equals("Default")) {
                    listOfMeds = listOfMeds + threeDay;
                }
            } else if(typeOfNotification == 3) {
                if(!threeDay.equals("Default")) {
                    listOfMeds = listOfMeds + threeDay;
                }
            } else {
                //something went wrong
                //listOfMeds = oneDay + twoDay + threeDay;
                Log.e("error","soemthing went wrong with reminders");
            }

                if(!listOfMeds.equals("")) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("BP-n-ME")
                        .setContentText("It is time to take the following medication: " + listOfMeds);
                Intent resultIntent = new Intent(this, MainActivity.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            //}
        }
        */
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("storage").child("users").child(user.getUid()).child("name").setValue(name);
        //read medication values, then issue a new notification

        //save medication values under different key, then parse through them and create notifications.




        //setExact allows you to test it out

        stopService(intent);
    }
}
