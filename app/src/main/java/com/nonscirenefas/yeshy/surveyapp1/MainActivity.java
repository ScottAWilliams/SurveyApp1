package com.nonscirenefas.yeshy.surveyapp1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PHONE_FILENAME = "phone_file";
    private DatabaseReference mDatabase;
    //private ArrayList<Medication> medicationList = new ArrayList<>();
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        //String attempt2 = ((MyApplication) MainActivity.this.getApplication()).getPhone();

        //Log.e("Phone2",attempt2);

        /*
        mDatabase.child("app").child("users").child(UID).child("medicine").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((MyApplication) MainActivity.this.getApplication()).resetMedicationList();
                        ArrayList<String> onePerDay = new ArrayList<>();
                        ArrayList<String> twoPerDay = new ArrayList<>();
                        ArrayList<String> threePerDay = new ArrayList<>();
                        ArrayList<String> fourPerDay = new ArrayList<>();
                        // Get user value
                        Log.e("reading",dataSnapshot.toString());

                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            Log.e("reading2", medicine.toString());
                            Log.e("reading3", medicine.getKey());
                            Log.e("reading4", medicine.child(medicine.getKey()).toString());


                            //TODO: I believe error occurs here with setting to type Medication (Problem converting String to Medication)
                            Medication med = medicine.getValue(Medication.class);
                            ((MyApplication) MainActivity.this.getApplication()).addMedication(med);

                            Log.e("reading5", med.getName() + " " + med.getDaysSupply() + " " + med.getFrequency());

                            if(med.getFrequency().equals("Daily")) {
                                onePerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Twice daily")) {
                                twoPerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Three times daily")) {
                                threePerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Four times daily")) {
                                fourPerDay.add(med.getName());
                            }
                        }
                        //User user = dataSnapshot.getValue(User.class);

                        // ...
                        medicationList = ((MyApplication) MainActivity.this.getApplication()).getMedicationList();

                        String oneDay = Arrays.toString(onePerDay.toArray());
                        String twoDay = Arrays.toString(twoPerDay.toArray());
                        String threeDay = Arrays.toString(threePerDay.toArray());
                        String fourDay = Arrays.toString(fourPerDay.toArray());

                        SharedPreferences settings = getSharedPreferences("Medication", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("onePerDay", oneDay);
                        editor.putString("twoPerDay", twoDay);
                        editor.putString("threePerDay", threeDay);
                        editor.putString("fourPerDay", fourDay);
                        editor.commit();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
*/

        initializeMessagesList();
        startAlarm(this);
    }


    public void startAlarm(Context context) {
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
    }

    private void updateMessagesList() {

    }

    private void initializeMessagesList(){
        ArrayList<String> messages = new ArrayList<>();
        String [] LifestylePositiveMessages = getResources().getStringArray(R.array.LifestylePositiveMessagesArray);
        String [] LifestyleNegativeMessages = getResources().getStringArray(R.array.LifestyleNegativeMessagesArray);
        int[] LifestyleAnswers = ((MyApplication) MainActivity.this.getApplication()).getLifestyleSurveyAnswers();

        int counter = 0;
        for(int e: LifestyleAnswers) {
            if(e == 0) { //if right
                messages.add(LifestylePositiveMessages[counter]);
            } else if(e == 1) { //if wrong
                messages.add(LifestyleNegativeMessages[counter]);
            }
            counter++;
        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        //Random generator = new Random();
        int num = day % messages.size();
        //Log.e("num",Integer.toString(num));

        //String [] mArrayBefore = new String[messages.size()];
        //mArrayBefore = messages.toArray(mArrayBefore);

        String [] mArray = {"Messages will appear here once the survey data has loaded."};
        if(num < messages.size()) {
            mArray[0] = messages.get(num);
        }

       adapter = new ArrayAdapter<String>(this,R.layout.tip_of_the_day,mArray);
        //setListAdapter(adapter);
        final ListView lv = (ListView) findViewById(R.id.messagesListView);
        lv.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //finish();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        mDatabase.child("app").child("users").child(UID).child("pharmanumber").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phonenumber = dataSnapshot.getValue().toString();
                        Log.e("Phone",phonenumber);
                        ((MyApplication) MainActivity.this.getApplication()).setPharmaPhone(phonenumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, SurveySelectionActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+tel));
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String UIDstored = settings.getString("UID", "Default");
            Log.e("logout", UIDstored);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UID", "Default");
            editor.commit();

            UIDstored = settings.getString("UID", "Default");
            Log.e("logout", UIDstored);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_hipaa) {
            Intent i = new Intent(this, HIPAAActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_informedconsent) {
            Intent i = new Intent(this, InformedConsentActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
