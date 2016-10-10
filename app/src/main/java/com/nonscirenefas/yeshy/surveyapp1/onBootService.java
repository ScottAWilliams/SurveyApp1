package com.nonscirenefas.yeshy.surveyapp1;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class onBootService extends IntentService
{
    public onBootService() {
        super("onBootService");
    }

    public static final String MED_FILENAME = "med_file";
    public static final String FREQ_FILENAME = "freq_file";
    public static final String PREFS_UID = "MyPrefsFile";
    public static final String LSURVEY_FILENAME = "lsurvey_file";
    public static final String MSURVEY_FILENAME = "msurvey_file";
    public static final String HSURVEY_FILENAME = "hsurvey_file";
    ArrayList<String> medicationList = new ArrayList<>();
    ArrayList<String> freqList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<String>();
    Context ctx;
    String USER_FILENAME = "user_file";
    String printedMeds;
    String tenAmMeds="";
    String twoPmMeds="";
    String eightPmMeds="";
    String date;
    String type;
    int dayOfYear;
    int minute = 0;
    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */


    //@Override
    protected void onHandleIntent(Intent intent) {

        Intent intent1 = new Intent(this,MonthlyReminderService.class);
        int received = intent1.getIntExtra("recieved", 0);
        intent1.putExtra("received", received);
        startService(intent1);

        Intent intent2 = new Intent(this,ReminderService.class);
        int type = intent2.getIntExtra("type", 0);
        intent2.putExtra("type", type);
        startService(intent2);

        stopService(intent);
    }
}